package experiments.pdf;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class PDFExperimentMasqueradeTest {
    private PDFUtils.GenerationStrategy thresholdGenerationStrategy;
    private List<Integer> thresholdBeginIndicies, thresholdEndIndicies, thresholdCounts;
    private PDFUtils.GenerationStrategy evaluationDomainGenerationStrategy;
    private List<Integer> evaluationDomainBeginIndicies, evaluationDomainEndEndicies, evaluationDomainCounts;
    private List<PDFUtils.KernelFunction> kernelFunctions;
    private List<Double> bandwidths;


    @Before
    public void initializeParams(){
        String data = readInputJson("src/test/resources/experiments/input.json");
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(data);
        JsonObject obj = element.getAsJsonObject();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {}.getType();

        thresholdGenerationStrategy = gson.fromJson(generationStrategyParserHelper(obj, "thresholds"), PDFUtils.GenerationStrategy.class);
        thresholdBeginIndicies = gson.fromJson(generationDataParserHelper(obj, "thresholds", "beginIndicies"), type);
        thresholdEndIndicies = gson.fromJson(generationDataParserHelper(obj, "thresholds", "endIndicies"), type);
        thresholdCounts = gson.fromJson(generationDataParserHelper(obj, "thresholds", "counts"), type);

        evaluationDomainGenerationStrategy = gson.fromJson(generationStrategyParserHelper(obj, "evaluationDomain"), PDFUtils.GenerationStrategy.class);
        evaluationDomainBeginIndicies = gson.fromJson(generationDataParserHelper(obj, "evaluationDomain", "beginIndicies"), type);
        evaluationDomainEndEndicies = gson.fromJson(generationDataParserHelper(obj, "evaluationDomain", "endIndicies"), type);
        evaluationDomainCounts = gson.fromJson(generationDataParserHelper(obj, "evaluationDomain", "counts"), type);

        kernelFunctions = kernelFunctionParserHelper(obj);

        bandwidths = bandwidthParserHelper(obj);
    }

    private List<Double> bandwidthParserHelper(JsonObject obj){
        List<Double> toRet = new ArrayList<>();
        String rangeAsString = obj.get("bandwidths").getAsString();
        System.out.println(rangeAsString);
        if (rangeAsString.matches("\\[\\d*\\.?\\d+-\\d*\\.?\\d+:\\d*\\.?\\d+]")){
            // thank you chatgpt
            String[] parts = rangeAsString.substring(1, rangeAsString.length() - 1).split("[:-]");
            double start = Double.parseDouble(parts[0]);
            double end = Double.parseDouble(parts[1]);
            double step = Double.parseDouble(parts[2]);

            for (double i = start; i <= end; i += step) {
                toRet.add(i);
            }
        }else{
            System.out.println("error with range of bandwidths in input json file");
        }
        return toRet;
    }

    private List<PDFUtils.KernelFunction> kernelFunctionParserHelper(JsonObject obj){
        return List.of(new Gson().fromJson(obj.get("kernelFunctions").getAsJsonArray(), PDFUtils.KernelFunction[].class));
    }

    private JsonElement generationStrategyParserHelper(JsonObject obj, String parentKey){
       return obj.get(parentKey).getAsJsonObject().get("generationStrategy");
    }

    private JsonArray generationDataParserHelper(JsonObject obj, String parentKey, String childKey){
        return obj.get(parentKey).getAsJsonObject().get(childKey).getAsJsonArray();
    }

    private String readInputJson(String filePath){
        StringBuilder builder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Path.of(filePath), StandardCharsets.UTF_8)){
            stream.forEach(s -> builder.append(s).append("\n"));
        }catch (IOException e){
            e.printStackTrace();
        }
        return builder.toString();
    }

    @Test
    public void runExperiments(){
        GenerationData thresholdGeneration = new GenerationData(PDFUtils.GenerationStrategy.RANDOMLY_SPACED_WITHIN_INTERVAL, 0, 100, 500);
        GenerationData evaluationDomainGeneration = new GenerationData(PDFUtils.GenerationStrategy.EVENLY_SPACED_OVER_INTERVAL, 0, 100, 1000);
        PDFUtils.KernelFunction kernelFunction = PDFUtils.KernelFunction.LOGISTIC;
        double bandwidth = .4;
        PDFTreatment treatment = new PDFTreatment(thresholdGeneration, evaluationDomainGeneration, kernelFunction, bandwidth);

        long start = System.currentTimeMillis();
        BigDecimal[] densityArray = getDensityArray(treatment);
        long end = System.currentTimeMillis();
        long timeToRunMS = end - start;

        //everything after the density array is just interpolation/extrapolation, I believe I can just use this for my response.
        PDFResponse response = new PDFResponse(treatment.getEvaluationDomain(), densityArray, timeToRunMS);

        visualizeBigDecimalArray(densityArray, treatment.getUuid().toString());

        toJSON(treatment, response);
        //Arrays.stream(densityArray).forEach(System.out::println);
    }

    private void toJSON(PDFTreatment treatment, PDFResponse response){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        PDFOutputter pdfOutputter = new PDFOutputter(treatment, response);
        System.out.println(gson.toJson(pdfOutputter));
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






}
