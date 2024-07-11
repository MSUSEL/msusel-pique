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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

public class ProbabilityDensityFunctionUtilityFunction extends UtilityFunction{

    @Getter @Setter
    private double bandwidth = 0.9;
    @Getter
    private int samplingSpace = 1000;


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

            //sort array
            Arrays.sort(thresholds);
            BigDecimal[] evaluationDomain = linSpace(thresholds[0], thresholds[thresholds.length - 1], new BigDecimalWithContext(samplingSpace));
            BigDecimal[] densityArray = getDensityArray(thresholds, evaluationDomain);
            int closestIndex = searchSorted(evaluationDomain, inValue);
            // tedious because java
            BigDecimal[] leftSideOfEvaluationDomain = Arrays.copyOfRange(evaluationDomain, 0, closestIndex);
            BigDecimal[] leftSideOfDensity = Arrays.copyOfRange(densityArray, 0, closestIndex);
            BigDecimal aucAtValueZero = manualTrapezoidalRule(leftSideOfEvaluationDomain, leftSideOfDensity);
            score = new BigDecimalWithContext(1.0).subtract(aucAtValueZero);
        }

        return score;
    }

    /***
     * Gaussian kernel function
     * @param input x value to evaluate with
     * @return corresponding y value from the gaussian kernel
     */
    private BigDecimal gaussianKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(1 / Math.sqrt(2*Math.PI));
        BigDecimal transformed = new BigDecimalWithContext(Math.exp(-0.5 * Math.pow(input.doubleValue(),2)));
        return transformed.multiply(constant, BigDecimalWithContext.getMC());
    }

    public BigDecimal kernelDensityEstimator(BigDecimal input, BigDecimal[] thresholds){
        BigDecimal[] singlePointSummables = new BigDecimal[thresholds.length];
        for (int i = 0; i < thresholds.length; i++){
            //find distance from input to every threshold, normalized by height
            BigDecimal evaluationCriteria = (input.subtract(thresholds[i])).divide(new BigDecimalWithContext(bandwidth), BigDecimalWithContext.getMC());
            singlePointSummables[i] = gaussianKernel(evaluationCriteria);
        }
        BigDecimal constant = new BigDecimalWithContext(thresholds.length * bandwidth);
        //sum the summables
        BigDecimal sum = Arrays.stream(singlePointSummables).reduce(BigDecimalWithContext.ZERO,BigDecimal::add);
        //divide the summables
        return sum.divide(constant,BigDecimalWithContext.getMC());
    }

    public BigDecimal[] getDensityArray(BigDecimal[] thresholds, BigDecimal[] domainRange){
        BigDecimal[] toRet = new BigDecimal[domainRange.length];
        for (int i = 0; i < domainRange.length; i++){
            toRet[i] = kernelDensityEstimator(domainRange[i], thresholds);
        }
        return toRet;
    }

    /***
     * Java equivalent function to perform python's np.linspace(), which generates a list of evenly spaced numbers between
     * two endpoints, with the number of evenly spaced numbers being parameterized.
     *
     * Java does have such a library in the ND4J library, but it feels unnecessary to use the full library (and therefore
     * potentially inherit any vulnerabilities) for one quick function. Additionally, I couldn't track down the runtime
     * complexity of ND4J.Linspace, but I doubt it is faster than linear, which this quick solution is linear.
     *
     * @param min starting value
     * @param max ending value
     * @param range number of evenly spaced values desired
     * @return array of BigDecimals that contains an array of equally spaced values between the parameters min and max
     */
    public BigDecimal[] linSpace(BigDecimal min, BigDecimal max, BigDecimal range){
        BigDecimal[] toRet = new BigDecimal[range.intValue()];
        BigDecimal stepSize = (max.subtract(min)).divide(range, BigDecimalWithContext.getMC());
        for (int i = 0; i < toRet.length; i++){
            //bit awkward to cast BigDecimal to double so that it fits in the BigDecimalWithContext constructor,
            // consider making everything a bigDecimalWithContext
            toRet[i] = new BigDecimalWithContext(min.doubleValue());
            min = min.add(stepSize);
        }
        return toRet;
    }

    /***
     * Java equivalent code to python's numpy.searchSorted() function. Follows default implementation with side=left.
     * Meaning return the value equal OR immediately lesser than the input value.
     *
     * Returns the closest value to an input value, in an input array. One assumption is that if
     *
     * @param array Array to search through for a value
     * @param value value to search for
     * @return index for the value equal to or the index of where the value would be inserted into the array
     */
    public int searchSorted(BigDecimal[] array, BigDecimal value){
        //sort in case it wasn't sorted already.
        int index = Arrays.binarySearch(array, value);
        //ChatGPT helped me with this, note that it covers the edge case presented in the Arrays.binarySearch documentation:
        // "Note that this guarantees that the return value will be >= 0 if and only if the key is found."
        // I spent a good amount of time verifying chatgpt's code acutally works, and it does because the Arrays.binarySearch
        // performs -index -1 on all values of initial index >= 0, and while I am not sure of the cases where index would be <0,
        // the code is the same as what would happen if the index was above 0.
        if (index < 0) {
            index = -index - 1;
        }
        return index;

    }


    /***
     * quick function to calculate the area under a curve using the trapezoid rule
     *
     * @param functionDomain The array of the x domain values of the function, basically x[min] through the x for which we supplied the y value
     * @param densityRange The array of density estimate values, computed from the kernel density estimator
     * @return area under the curve
     */
    public BigDecimal manualTrapezoidalRule(BigDecimal[] functionDomain, BigDecimal[] densityRange){
        BigDecimal area = new BigDecimalWithContext(0);
        for (int i = 1; i < functionDomain.length; i++){
            // x[i] - x[i-1]
            BigDecimal xStep = functionDomain[i].subtract(functionDomain[i-1]);
            // y[i] + y[i-1]
            BigDecimal yStep = densityRange[i].add(densityRange[i-1]);
            // (x[i] - x[i-1]) * (y[i] + y[i-1])
            BigDecimal numerator = xStep.multiply(yStep);
            // area += (x[i] - x[i-1]) * (y[i] + y[i-1])) / 2
            area = area.add(numerator.divide(new BigDecimalWithContext(2.0), BigDecimalWithContext.getMC()));
        }
        return area;
    }
}
