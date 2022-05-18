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

import pique.analysis.ITool;
import pique.evaluation.BenchmarkMeasureEvaluator;
import pique.evaluation.Project;
import pique.model.Diagnostic;
import pique.model.Measure;
import pique.model.ModelNode;
import pique.model.QualityModel;
import pique.utility.BigDecimalWithContext;
import pique.utility.FileUtility;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;

public class AdvancedBenchmarker extends AbstractBenchmarker implements IBenchmarker {
    @Override
    public String getName() {
        return this.getClass().getCanonicalName();
    }

	@Override
	public Map<String, BigDecimal[]> calculateThresholds(Map<String, ArrayList<BigDecimal>> measureBenchmarkData) {
		// might need refactorign in the future, I am not sure why I can't just use measureBenchmarkData, but this is waht David did...
        //I just removed the min and max (which is present in NaiveBenchmarker) and put in the full set of benchmark data.
        Map<String, BigDecimal[]> measureThresholds = new HashMap<>();
        measureBenchmarkData.forEach((measureName, measureValues) -> {

            int measureValuesSize = measureBenchmarkData.get(measureName).size();
            measureThresholds.putIfAbsent(measureName, new BigDecimal[measureValuesSize]);
            for (int i = 0; i < measureValuesSize; i++) {
                //lol need to fix this: TODO: fixme.
                measureThresholds.get(measureName)[i] = measureBenchmarkData.get(measureName).get(i);
            }

        });

        return measureThresholds;
	}
}
