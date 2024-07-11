package experiments.pdf;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;

public class PDFExperimentMasqueradeTest {



    @Test
    public void runExperiments(){
        BigDecimal[] thresholds = PDFUtils.GenerationStrategy.RANDOMLY_SPACED_WITHIN_INTERVAL.generateValues(0, 100, 500);
        BigDecimal[] evaluationDomain = PDFUtils.GenerationStrategy.EVENLY_SPACED_OVER_INTERVAL.generateValues(0, 100, 1000);
        PDFUtils.KernelFunction kernelFunction = PDFUtils.KernelFunction.LOGISTIC;
        double bandwidth = .4;
        PDFTreatment t = new PDFTreatment(thresholds, evaluationDomain, kernelFunction, bandwidth);

        long start = System.currentTimeMillis();
        BigDecimal[] densityArray = getDensityArray(t);
        long end = System.currentTimeMillis();
        long timeToRun = end - start;

        //everything after the density array is just interpolation/extrapolation, I believe I can just use this for my response.
        //PDFResponse response = new PDFResponse(t.getEvaluationDomain(), densityArray, timeToRun);

        visualizeBigDecimalArray(densityArray, "density array" + bandwidth);

        Arrays.stream(densityArray).forEach(System.out::println);
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
            ChartUtils.saveChartAsPNG(new File("src/test/out/" + name  + ".png"), chart, 800, 600);
        }catch (Exception e){
            e.printStackTrace();
        }
    }






}
