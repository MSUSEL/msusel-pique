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

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pique.analysis.ITool;
import pique.calibration.AbstractBenchmarker;
import pique.calibration.IBenchmarker;
import pique.evaluation.BenchmarkMeasureEvaluator;
import pique.evaluation.Project;
import pique.model.Diagnostic;
import pique.model.Measure;
import pique.model.QualityModel;
import pique.utility.BigDecimalWithContext;

public class MeanSDBenchmarker extends AbstractBenchmarker implements IBenchmarker {
	
	@Override
	public Map<String, BigDecimal[]> calculateThresholds(Map<String, ArrayList<BigDecimal>> measureBenchmarkData) {
		// Identify the mean+-sd of each measure value
        Map<String, BigDecimal[]> measureThresholds = new HashMap<>();
        measureBenchmarkData.forEach((measureName, measureValues) -> {
        	BigDecimal[] values = new BigDecimal[2];
            
        	values[0] = mean(measureValues).subtract(calculateSD(measureValues));
            values[1] = mean(measureValues).add(calculateSD(measureValues));
            
            if (values[0].compareTo(new BigDecimal("0.0")) < 0) { //measureThresholds.get(measureName)[0] < 0.0
            	values[0] = new BigDecimal("0.0");
            }
            measureThresholds.put(measureName, values);
        });

        return measureThresholds;
	}
    
    @Override
    public String getName() {
        return this.getClass().getCanonicalName();
    }
    
    /**
     * Take mean of a BigDecimal ArrayList
     * @param measureValues The ArrayList<BigDecimal> to take the mean of
     * @return mean value of the passed parameter
     */
    private BigDecimal mean(ArrayList<BigDecimal> measureValues) {
    	BigDecimal sum = new BigDecimal("0.0");
        for (int i = 0; i < measureValues.size(); i++) {
            sum = sum.add(measureValues.get(i));
        }
        return sum.divide(new BigDecimal(""+measureValues.size()),BigDecimalWithContext.getMC()); 
    }

    /**
     * Calculates the standard deviation of an ArrayList<BigDecimal>.
     * @param measureValues values of BigDecimals
     * @return Standard deviation of the passed BigDecimals
     */
    private BigDecimal calculateSD(ArrayList<BigDecimal> measureValues)
    {
    	BigDecimal sum = new BigDecimal("0.0"), standardDeviation = new BigDecimal("0.0");
        int length = measureValues.size();

        for(BigDecimal num : measureValues) {
            sum = sum.add(num);
        }

        BigDecimal mean = sum.divide(new BigDecimal(""+length),BigDecimalWithContext.getMC());

        for(BigDecimal num: measureValues) {
            standardDeviation = standardDeviation.add(num.subtract(mean).pow(2));
        }
        //divide by n-1 degrees of freedom
        BigDecimal df = new BigDecimal(length).subtract(new BigDecimal("1"));
        if (df.compareTo(BigDecimal.ZERO)<=0) {
        	return BigDecimal.ZERO; 
        	//this is the case where we have 0 or -1 degrees of freedom, meaning n=0 or 1, in which 
        	//case the sd is zero, so we may return zero. If we don't do this here, we get an
        	//ArithmeticException in the next line.
        }
        standardDeviation = standardDeviation.divide(df,BigDecimalWithContext.getMC());
        standardDeviation = sqrt(standardDeviation,BigDecimalWithContext.getMC().getPrecision());
        return standardDeviation;
    }

    
    private BigDecimal sqrt(BigDecimal A, final int SCALE) {
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, SCALE, BigDecimal.ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(new BigDecimal("2"), SCALE, BigDecimal.ROUND_HALF_UP);
        }
        return x1;
    }


}
