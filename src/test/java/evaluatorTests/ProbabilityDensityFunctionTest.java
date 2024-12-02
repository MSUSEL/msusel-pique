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
package evaluatorTests;

import org.junit.Test;
import pique.calibration.AbstractBenchmarker;
import pique.calibration.ProbabilityDensityFunctionBenchmarker;
import pique.evaluation.ProbabilityDensityFunctionUtilityFunction;
import pique.utility.BigDecimalWithContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProbabilityDensityFunctionTest {

    private ProbabilityDensityFunctionBenchmarker probabilityDensityFunctionBenchmarker;
    private ProbabilityDensityFunctionUtilityFunction probabilityDensityFunctionUtilityFunction;

    public ProbabilityDensityFunctionTest(){
        probabilityDensityFunctionBenchmarker = new ProbabilityDensityFunctionBenchmarker();
        probabilityDensityFunctionUtilityFunction = new ProbabilityDensityFunctionUtilityFunction();
    }

    @Test
    public void endToEndUtilitizingTest(){
        //perform benchmarking
        Map<String, ArrayList<BigDecimal>> measureBenchmarkData = new HashMap<>();

        ArrayList<BigDecimal> t1 = new ArrayList<>();
        t1.add(new BigDecimal("0"));
        t1.add(new BigDecimal("1"));
        t1.add(new BigDecimal("2"));
        t1.add(new BigDecimal("3"));
        t1.add(new BigDecimal("4"));
        t1.add(new BigDecimal("5"));
        t1.add(new BigDecimal("6"));
        t1.add(new BigDecimal("7"));
        t1.add(new BigDecimal("8"));
        t1.add(new BigDecimal("9"));
        t1.add(new BigDecimal("40"));
        measureBenchmarkData.put("t1",t1);

        Map<String,BigDecimal[]> thresholds = probabilityDensityFunctionBenchmarker.calculateThresholds(measureBenchmarkData);
        BigDecimal[] t1Thresholds = thresholds.get("t1");
        Arrays.stream(t1Thresholds).forEach(System.out::println);

        //perform utilitizing
        BigDecimal inValue = new BigDecimalWithContext(100.0);

        BigDecimal score = probabilityDensityFunctionUtilityFunction.utilityFunction(inValue, t1Thresholds, false);
        System.out.println(score);


    }
}
