package pique.calibration;

import pique.utility.BigDecimalWithContext;
import pique.utility.PDFUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProbabilityDensityFunctionBenchmarker extends AbstractBenchmarker{

    private final PDFUtils.GenerationStrategy generationStrategy = PDFUtils.GenerationStrategy.EVEN_UNIFORM_SPACING;
    private final PDFUtils.KernelFunction kernelFunction = PDFUtils.KernelFunction.GAUSSIAN;
    private final double bandwidth = 0.5;

    /***
     * Thresholds for the PDFBenchmarker will be a list of all the density array data points, for use with the PDFUtilfunction
     *
     * @param measureBenchmarkData the data of the application of tools to the benchmark repository.
     * @return
     */
    @Override
    public Map<String, BigDecimal[]> calculateThresholds(Map<String, ArrayList<BigDecimal>> measureBenchmarkData) {
        Map<String, BigDecimal[]> measureThresholds = new HashMap<>();

        measureBenchmarkData.forEach((measureName, measureValues) -> {
            BigDecimal[] temp = getDensityArray(measureValues);
            measureThresholds.putIfAbsent(measureName, temp);
        });

        return measureThresholds;
    }

    private BigDecimal[] getDensityArray(ArrayList<BigDecimal> measureValues) {
        BigDecimal[] toRet = new BigDecimal[measureValues.size()];
        BigDecimal[] measureValuesArray = new BigDecimal[measureValues.size()];
        measureValuesArray = measureValues.toArray(measureValuesArray);

        BigDecimal min = new BigDecimalWithContext(Double.MAX_VALUE);
        BigDecimal max = new BigDecimalWithContext(Double.MIN_VALUE);
        for (int i = 0; i < measureValues.size(); i++){
            BigDecimal myValue = measureValues.get(i);
            measureValuesArray[i] = myValue;
            //min and max for evaluation domain generation strategies - performance improvement instead of Collections.min/max
            if (min.compareTo(myValue) == 1){
                min = myValue;
            }
            if (max.compareTo(myValue) == -1){
                max = myValue;
            }
        }
        // generate evaluation domain
        BigDecimal[] evaluationDomain = generationStrategy.generateValues(min.intValue(), max.intValue(), measureValues.size());

        for (int i = 0; i < toRet.length; i++){
            toRet[i] = PDFUtils.kernelDensityEstimator(evaluationDomain[i], measureValuesArray, bandwidth, kernelFunction);
        }
        return toRet;
    }

}
