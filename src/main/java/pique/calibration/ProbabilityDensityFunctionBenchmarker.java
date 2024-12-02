/**
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
