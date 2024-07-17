package experiments.pdf;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PDFExperimentMasqueradeTest {
    private List<PDFUtils.GenerationStrategy> thresholdGenerationStrategies;
    private List<Integer> thresholdBeginIndices, thresholdEndIndices, thresholdCounts;
    private List<PDFUtils.GenerationStrategy> evaluationDomainGenerationStrategies;
    private List<Integer> evaluationDomainBeginIndices, evaluationDomainEndEndices, evaluationDomainCounts;
    private List<PDFUtils.KernelFunction> kernelFunctions;
    private List<Double> bandwidths;


    @Before
    public void initializeParams(){
        String data = PDFUtils.readInputJson("src/test/resources/experiments/input.json");
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(data);
        JsonObject obj = element.getAsJsonObject();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();

        thresholdGenerationStrategies = generationStrategyParserHelper(obj.get("thresholds").getAsJsonObject());
        thresholdBeginIndices = gson.fromJson(generationDataParserHelper(obj, "thresholds", "beginIndices"), type);
        thresholdEndIndices = gson.fromJson(generationDataParserHelper(obj, "thresholds", "endIndices"), type);
        thresholdCounts = gson.fromJson(generationDataParserHelper(obj, "thresholds", "counts"), type);

        evaluationDomainGenerationStrategies = generationStrategyParserHelper(obj.get("evaluationDomain").getAsJsonObject());
        evaluationDomainBeginIndices = gson.fromJson(generationDataParserHelper(obj, "evaluationDomain", "beginIndices"), type);
        evaluationDomainEndEndices = gson.fromJson(generationDataParserHelper(obj, "evaluationDomain", "endIndices"), type);
        evaluationDomainCounts = gson.fromJson(generationDataParserHelper(obj, "evaluationDomain", "counts"), type);

        kernelFunctions = kernelFunctionParserHelper(obj);

        bandwidths = bandwidthParserHelper(obj);
    }


    @Test
    public void runNaiveCase(){
        GenerationData thresholdGeneration =
                new GenerationData(PDFUtils.GenerationStrategy.RANDOMLY_SPACED_WITHIN_INTERVAL, 0, 100, 500);
        GenerationData evaluationDomainGeneration =
                new GenerationData(PDFUtils.GenerationStrategy.EVENLY_SPACED_OVER_INTERVAL, 0, 100, 5000);
        PDFTreatment treatment = new PDFTreatment(thresholdGeneration, evaluationDomainGeneration, PDFUtils.KernelFunction.GAUSSIAN, 0.4);
        long start = System.currentTimeMillis();
        BigDecimal[] densityArray = getDensityArray(treatment);
        long end = System.currentTimeMillis();
        long timeToRunMS = end - start;

        //everything after the density array is just interpolation/extrapolation, I believe I can just use this for my response.
        PDFResponse response = new PDFResponse(treatment.getEvaluationDomain(), densityArray, timeToRunMS);
        visualizeBigDecimalArray(densityArray, treatment.getUuid().toString() + "_density");
        visualizeBigDecimalArray(treatment.getThresholds(), treatment.getUuid().toString() + "_thresholds");
        visualizeBigDecimalArray(treatment.getEvaluationDomain(), treatment.getUuid().toString() + "_evaluationDomain");

        toJSON(treatment, response);
    }

    @Test
    public void testNegativeAUC(){
        GenerationData thresholdGeneration =
                new GenerationData(PDFUtils.GenerationStrategy.RANDOMLY_SPACED_RIGHT_SKEW, 0, 100, 100);
        GenerationData evaluationDomainGeneration =
                new GenerationData(PDFUtils.GenerationStrategy.EVENLY_SPACED_OVER_INTERVAL, 0, 100, 10);
        PDFTreatment treatment = new PDFTreatment(thresholdGeneration, evaluationDomainGeneration, PDFUtils.KernelFunction.TRIWEIGHT, 0.9);
        long start = System.currentTimeMillis();
        BigDecimal[] densityArray = getDensityArray(treatment);
        long end = System.currentTimeMillis();
        long timeToRunMS = end - start;

        //everything after the density array is just interpolation/extrapolation, I believe I can just use this for my response.
        PDFResponse response = new PDFResponse(treatment.getEvaluationDomain(), densityArray, timeToRunMS);
        visualizeBigDecimalArray(densityArray, treatment.getUuid().toString());

        toJSON(treatment, response);
    }

    @Test
    public void runExperiments(){
        //resetExperimentDeleteFiles();
        int expCount = thresholdGenerationStrategies.size() * thresholdBeginIndices.size() * thresholdEndIndices.size() * thresholdCounts.size()
                * evaluationDomainGenerationStrategies.size() + evaluationDomainBeginIndices.size() * evaluationDomainEndEndices.size() * evaluationDomainCounts.size()
                * kernelFunctions.size() * bandwidths.size();
        System.out.println("Experimenting on : " + expCount + " different treatments");
        int counter = 0;
        for (PDFUtils.GenerationStrategy thresholdGenerationStrategy : thresholdGenerationStrategies) {
            for (Integer thresholdBeginIndex : thresholdBeginIndices) {
                for (Integer thresholdEndIndex : thresholdEndIndices) {
                    for (Integer thresholdCount : thresholdCounts) {
                        for (PDFUtils.GenerationStrategy evaluationDomainGenerationStrategy : evaluationDomainGenerationStrategies) {
                            for (Integer evaluationDomainBeginIndex : evaluationDomainBeginIndices) {
                                for (Integer evaluationDomainEndIndex : evaluationDomainEndEndices) {
                                    for (Integer evaluationDomainCount : evaluationDomainCounts) {
                                        for (PDFUtils.KernelFunction kernelFunction : kernelFunctions) {
                                            for (Double bandwidth : bandwidths) {
                                                GenerationData thresholdGeneration =
                                                        new GenerationData(thresholdGenerationStrategy, thresholdBeginIndex, thresholdEndIndex, thresholdCount);
                                                GenerationData evaluationDomainGeneration =
                                                        new GenerationData(evaluationDomainGenerationStrategy, evaluationDomainBeginIndex, evaluationDomainEndIndex, evaluationDomainCount);
                                                PDFTreatment treatment = new PDFTreatment(thresholdGeneration, evaluationDomainGeneration, kernelFunction, bandwidth);
                                                long start = System.currentTimeMillis();
                                                BigDecimal[] densityArray = getDensityArray(treatment);
                                                long end = System.currentTimeMillis();
                                                long timeToRunMS = end - start;

                                                //everything after the density array is just interpolation/extrapolation, I believe I can just use this for my response.
                                                PDFResponse response = new PDFResponse(treatment.getEvaluationDomain(), densityArray, timeToRunMS);
                                                visualizeBigDecimalArray(densityArray, treatment.getUuid().toString());

                                                toJSON(treatment, response);
                                                if (counter % 250 == 0){
                                                    System.out.println("\tSuccessfully ran experiment: " + counter);
                                                }
                                                counter++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void runAnalysis(){
        Analysis analysisPackage = new Analysis();

    }


    @Test
    public void resetExperimentDeleteFiles() {
        String preamble = "src/test/out/";
        try {
            FileUtils.cleanDirectory(new File(preamble + "pdf_graphs"));
            FileUtils.cleanDirectory(new File(preamble + "pdf_output"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void toJSON(PDFTreatment treatment, PDFResponse response){
        PDFOutputterWrapper pdfOutputterWrapper = new PDFOutputterWrapper(treatment, response);
        try (Writer writer = new FileWriter("src/test/out/pdf_output/" + Paths.get(treatment.getUuid().toString() + ".json"))){
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            gson.toJson(pdfOutputterWrapper, writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private BigDecimal[] getDensityArray(PDFTreatment treatment){
        BigDecimal[] toRet = new BigDecimal[treatment.getEvaluationDomain().length];
        for (int i = 0; i < toRet.length; i++){
            toRet[i] = PDFUtils.kernelDensityEstimator(treatment.getEvaluationDomain()[i], treatment);
        }
        return toRet;
    }

    private void visualizeBigDecimalArray(BigDecimal[] inData, String name){
        XYSeriesCollection series = new XYSeriesCollection();
        XYSeries data = new XYSeries("Values");
        for (int i = 0; i < inData.length; i++){
            data.add(i, inData[i]);
        }
        series.addSeries(data);

        JFreeChart chart = ChartFactory.createScatterPlot("nerds at harvard got nothing on this", "x axis", "y axis", series);
        try {
            ChartUtils.saveChartAsPNG(new File("src/test/out/pdf_graphs/" + name  + ".png"), chart, 800, 600);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private List<Double> bandwidthParserHelper(JsonObject obj){
        List<Double> toRet = new ArrayList<>();
        String rangeAsString = obj.get("bandwidths").getAsString();
        if (rangeAsString.matches("\\[\\d*\\.?\\d+-\\d*\\.?\\d+:\\d*\\.?\\d+]")){
            // thank you chatgpt
            String[] parts = rangeAsString.substring(1, rangeAsString.length() - 1).split("[:-]");
            double start = Double.parseDouble(parts[0]);
            double end = Double.parseDouble(parts[1]);
            double step = Double.parseDouble(parts[2]);

            int sigDigits = new Double(step).toString().split("\\.")[1].length();

            for (double i = start; i <= end; i += step) {
                BigDecimal bd = BigDecimal.valueOf(i);
                bd = bd.setScale(sigDigits, RoundingMode.HALF_UP);
                toRet.add(bd.doubleValue());
            }
        }else{
            System.out.println("error with range of bandwidths in input json file");
        }
        return toRet;
    }

    private List<PDFUtils.KernelFunction> kernelFunctionParserHelper(JsonObject obj){
        return List.of(new Gson().fromJson(obj.get("kernelFunctions").getAsJsonArray(), PDFUtils.KernelFunction[].class));
    }

    private List<PDFUtils.GenerationStrategy> generationStrategyParserHelper(JsonObject obj){
        return List.of(new Gson().fromJson(obj.get("generationStrategies").getAsJsonArray(), PDFUtils.GenerationStrategy[].class));
    }

    private JsonArray generationDataParserHelper(JsonObject obj, String parentKey, String childKey){
        return obj.get(parentKey).getAsJsonObject().get(childKey).getAsJsonArray();
    }

}
