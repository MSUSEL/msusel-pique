package pique.calibration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProbabilityDensityFunctionBenchmarker extends AbstractBenchmarker{

    /***
     * Thresholds for the PDFBenchmarker will be a list of data points, for use with the PDFUtilfunction
     *
     * @param measureBenchmarkData the data of the application of tools to the benchmark repository.
     * @return
     */
    @Override
    public Map<String, BigDecimal[]> calculateThresholds(Map<String, ArrayList<BigDecimal>> measureBenchmarkData) {
        Map<String, BigDecimal[]> measureThresholds = new HashMap<>();

        measureBenchmarkData.forEach((measureName, measureValues) -> {
            BigDecimal[] measureValuesArray = new BigDecimal[measureValues.size()];
            //potential FIXME: if we run into performance issues, need to devise a better method to offload performance intensive operations to the benchmarking process
            measureThresholds.putIfAbsent(measureName, measureValues.toArray(measureValuesArray));
        });

        return measureThresholds;
    }

}
