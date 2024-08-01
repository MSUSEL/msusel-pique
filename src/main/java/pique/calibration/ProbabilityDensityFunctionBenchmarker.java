package pique.calibration;

import pique.utility.PDFUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProbabilityDensityFunctionBenchmarker extends AbstractBenchmarker{

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
            temp[0] = Collections.min(measureValues);
            temp[1] = Collections.max(measureValues);
            measureThresholds.putIfAbsent(measureName, temp);
        });

        return measureThresholds;
    }

    private BigDecimal[] getDensityArray(ArrayList<BigDecimal> measureValues) {
        BigDecimal[] toRet = new BigDecimal[measureValues.size()];
        // generate evaluation domain
        PDFUtils.GenerationStrategy evaluationDomainGenerationStrategy = PDFUtils.GenerationStrategy.EVEN_UNIFORM_SPACING;
        BigDecimal[] evaluationDomain = ;

        for (int i = 0; i < toRet.length; i++){
            toRet[i] = PDFUtils.kernelDensityEstimator(treatment.getEvaluationDomain()[i], treatment);
        }
        return toRet;
    }

}
