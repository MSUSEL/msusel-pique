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
package pique.evaluation;

import jep.Interpreter;
import jep.SharedInterpreter;
import lombok.Getter;
import lombok.Setter;
import pique.utility.BigDecimalWithContext;
import pique.utility.PDFUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

public class ProbabilityDensityFunctionUtilityFunction extends UtilityFunction{

    @Getter
    private int samplingSpace = 10000;


    public ProbabilityDensityFunctionUtilityFunction() {
        super("pique.evaluation.ProbabilityDensityFunctionUtilityFunction", "TODO - Write description in ProbabilityDensityFunctionUtilityFunction class");
    }

    @Override
    public BigDecimal utilityFunction(BigDecimal inValue, BigDecimal[] thresholds, boolean positive) {
        BigDecimal score = new BigDecimalWithContext(-10000);

        //are all values the same? - in O(n) apparently, said someone on stack overflow
        if (Arrays.stream(thresholds).distinct().count() == 1) {
            //one distinct value across the entire array
            BigDecimal compareValue = thresholds[0];
            //clean up if I get time
            if (inValue.compareTo(compareValue) == -1){
                //invalue is less than compareValue
                if (positive){
                    score = new BigDecimalWithContext(0.001);
                }else{
                    score = new BigDecimalWithContext(0.999);
                }
            }else{
                //inValue is greater than or equal to compareValue
                if (positive){
                    score = new BigDecimalWithContext(0.999);
                }else{
                    score = new BigDecimalWithContext(0.001);
                }
            }
        }else {
            //assume 'positive' flag is false, so more of x means less quality

            //thresholds is my density array, calculated from teh benchmarking process -- INDEX values from density array
            BigDecimal[] evaluationDomain = PDFUtils.linSpace(BigDecimal.ZERO, new BigDecimalWithContext(thresholds.length - 1), new BigDecimalWithContext(samplingSpace));
            int closestIndex = PDFUtils.searchSorted(evaluationDomain, inValue);
            // tedious because java
            BigDecimal[] leftSideOfEvaluationDomain = Arrays.copyOfRange(evaluationDomain, 0, closestIndex);
            BigDecimal[] leftSideOfDensity = Arrays.copyOfRange(thresholds, 0, closestIndex);
            BigDecimal aucAtValueZero = PDFUtils.manualTrapezoidalRule(leftSideOfEvaluationDomain, leftSideOfDensity);
            score = new BigDecimalWithContext(1.0).subtract(aucAtValueZero);
        }

        return score;
    }


}
