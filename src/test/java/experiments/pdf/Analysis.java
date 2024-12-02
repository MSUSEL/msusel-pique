/*
 * MIT License
 * Copyright (c) 2019 Montana State University Software Engineering Labs
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package experiments.pdf;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pique.utility.PDFUtils;

import java.io.File;
import java.util.*;
import java.util.List;

public class Analysis {

    @Getter
private Map<UUID, ImmutablePair<PDFTreatment, PDFResponse>> data;
    private String fileLocation = "src/test/out/pdf_output_jsons";

    public Analysis(){
        data = new HashMap<>();
        parseJsonsToData();
        generateGraphs();
        printMetaSummaryStatistics();
    }

    private void parseJsonsToData(){
        File[] listOfFiles = new File(fileLocation).listFiles();
        for (File f : listOfFiles) {
            String individualFile = PDFUtils.readInputJson(f.getPath());
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(individualFile);
            JsonObject obj = element.getAsJsonObject();
            Gson gson = new Gson();
            PDFTreatment treatment = gson.fromJson(obj.get("treatment").getAsJsonObject(), PDFTreatment.class);
            PDFResponse response = gson.fromJson(obj.get("response").getAsJsonObject(), PDFResponse.class);
            data.put(treatment.getUuid(), new ImmutablePair<>(treatment, response));
        }
    }

    private void printMetaSummaryStatistics(){
        printMaxAUC();
        printMaxMeanTransition();
        printMinMeanTransition();
    }

    private void generateGraphs(){
        maxAUCOverTime();
        histogramOfDensityTransitionMeans();
        histogramOfDensityTransitionSTDs();
        bandwidthOverTime();
        kernelFuncOverTime();
        meanTransitionValuesOverTime();
    }

    private void histogramOfDensityTransitionMeans() {
        int numBins = 100;
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);

        List<Double> transitionMeans = new ArrayList<>();
        for (ImmutablePair<PDFTreatment, PDFResponse> value : data.values()){
            double currentMean = value.getRight().getTransitionValueSummaryStatistics().getMean().doubleValue();
            transitionMeans.add(currentMean);
        }

        dataset.addSeries("Histogram", transitionMeans.stream().mapToDouble(Double::doubleValue).toArray(), numBins);

        JFreeChart chart = ChartFactory.createHistogram("Histogram of Density Transition Mean values BETWEEN 0 AND 0.1",
                "Density Transition Mean values", "Frequency", dataset);

        try {
            ChartUtils.saveChartAsPNG(new File("src/test/out/pdf_analysis/mean_density_transition_histogram.png"), chart, 800, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void histogramOfDensityTransitionSTDs() {
        int numBins = 100;
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);

        List<Double> transitionSTDs = new ArrayList<>();
        for (ImmutablePair<PDFTreatment, PDFResponse> value : data.values()){
            double currentSTD = value.getRight().getTransitionValueSummaryStatistics().getStd().doubleValue();
            transitionSTDs.add(currentSTD);

        }

        dataset.addSeries("Histogram", transitionSTDs.stream().mapToDouble(Double::doubleValue).toArray(), numBins);

        JFreeChart chart = ChartFactory.createHistogram("Histogram of Density Transition STD values",
                "Density Transition STD values", "Frequency", dataset);

        try {
            ChartUtils.saveChartAsPNG(new File("src/test/out/pdf_analysis/std_density_transition_histogram.png"), chart, 800, 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void meanTransitionValuesOverTime(){
        XYSeriesCollection series = new XYSeriesCollection();
        XYSeries seriesData = new XYSeries("Values");
        for (ImmutablePair<PDFTreatment, PDFResponse> p : data.values()){
            seriesData.add(p.getRight().getTimeToRunMS(), p.getRight().getTransitionValueSummaryStatistics().getMean());
        }
        series.addSeries(seriesData);

        JFreeChart chart = ChartFactory.createScatterPlot("Mean over time", "Time to run (ms)", "Mean Transition value", series);
        try {
            ChartUtils.saveChartAsPNG(new File("src/test/out/pdf_analysis/mean_over_time.png"), chart, 800, 600);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void kernelFuncOverTime(){
        XYSeriesCollection series = new XYSeriesCollection();
        XYSeries seriesData = new XYSeries("Values");
        for (ImmutablePair<PDFTreatment, PDFResponse> p : data.values()){
            seriesData.add(p.getRight().getTimeToRunMS(), p.getLeft().getKernelFunction().ordinal());
            //System.out.println(p.getLeft().getKernelFunction() + " " + p.getLeft().getKernelFunction().ordinal());
        }
        series.addSeries(seriesData);

        JFreeChart chart = ChartFactory.createScatterPlot("Kernel Functions over time", "Time to run (ms)", "Kernel functions as ordinal values", series);
        try {
            ChartUtils.saveChartAsPNG(new File("src/test/out/pdf_analysis/kernel_functions_over_time.png"), chart, 800, 600);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void bandwidthOverTime(){
        XYSeriesCollection series = new XYSeriesCollection();
        XYSeries seriesData = new XYSeries("Values");
        for (ImmutablePair<PDFTreatment, PDFResponse> p : data.values()){
            seriesData.add(p.getRight().getTimeToRunMS(), p.getLeft().getBandwidth());
        }
        series.addSeries(seriesData);

        JFreeChart chart = ChartFactory.createScatterPlot("Bandwidth over time",
                "Time to run (ms)",
                "Bandwidth value",
                series,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        try {
            ChartUtils.saveChartAsPNG(new File("src/test/out/pdf_analysis/bandwidth_over_time.png"), chart, 800, 600);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void maxAUCOverTime(){
        XYSeriesCollection series = new XYSeriesCollection();
        XYSeries seriesData = new XYSeries("Values");
        for (ImmutablePair<PDFTreatment, PDFResponse> p : data.values()){
            seriesData.add(p.getRight().getTimeToRunMS(), p.getRight().getMaxAUC());
        }
        series.addSeries(seriesData);

        JFreeChart chart = ChartFactory.createScatterPlot("Max AUC over time", "Time to run (ms)", "Max AUC", series);
        try {
            ChartUtils.saveChartAsPNG(new File("src/test/out/pdf_analysis/max_auc_over_time.png"), chart, 800, 600);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void printMaxAUC() {
        double maxAUC = 0.0;
        UUID correspondingUUID = null;
        for (ImmutablePair<PDFTreatment, PDFResponse> p : data.values()) {
            if (p.getRight().getMaxAUC().doubleValue() > maxAUC) {
                maxAUC = p.getRight().getMaxAUC().doubleValue();
                correspondingUUID = p.getLeft().getUuid();
            }
        }
        System.out.println("Max of max AUCs (want something close to 1 to full the entire domain, but a manual trapezoidal method makes this practically very difficult): " + maxAUC + " \n\tfrom treatment/config with ID: " + correspondingUUID.toString());

    }

    private void printMaxMeanTransition(){
        double maxMean = 0.0;
        UUID correspondingUUID = null;
        for (ImmutablePair<PDFTreatment, PDFResponse> p : data.values()){
            double compareValue = p.getRight().getTransitionValueSummaryStatistics().getMean().doubleValue();
            if (compareValue > maxMean) {
                maxMean = compareValue;
                correspondingUUID = p.getLeft().getUuid();
            }
        }
        System.out.println("Max of mean density transition value (want to minimize this for a cleaner interpolation range): " + maxMean + " \n\tfrom treatment/config with ID: " + correspondingUUID.toString());
    }
    private void printMinMeanTransition(){
        double minMean = Double.MAX_VALUE;
        UUID correspondingUUID = null;
        for (ImmutablePair<PDFTreatment, PDFResponse> p : data.values()){
            double compareValue = p.getRight().getTransitionValueSummaryStatistics().getMean().doubleValue();
            if (compareValue < minMean) {
                minMean = compareValue;
                correspondingUUID = p.getLeft().getUuid();
            }
        }
        System.out.println("Min of mean density transition value (want to minimize this for a cleaner interpolation range): " + minMean + " \n\tfrom treatment/config with ID: " + correspondingUUID.toString());
    }



}
