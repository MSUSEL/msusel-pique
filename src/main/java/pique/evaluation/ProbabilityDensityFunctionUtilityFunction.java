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


import lombok.Getter;
import pique.utility.BigDecimalWithContext;
import pique.utility.PDFUtils;

import java.math.BigDecimal;
import java.util.Arrays;

public class ProbabilityDensityFunctionUtilityFunction extends UtilityFunction{

    @Getter
    private final PDFUtils.GenerationStrategy generationStrategy = PDFUtils.GenerationStrategy.EVEN_UNIFORM_SPACING;
    @Getter
    private final PDFUtils.KernelFunction kernelFunction = PDFUtils.KernelFunction.GAUSSIAN;
    @Getter
    private final double bandwidth = 0.4;

    @Getter
    private int samplingSpace = 10000;


    public ProbabilityDensityFunctionUtilityFunction() {
        super("pique.evaluation.ProbabilityDensityFunctionUtilityFunction", "A Probability Density Function to model the distribution of findings");
    }

    @Override
    public BigDecimal utilityFunction(BigDecimal inValue, BigDecimal[] thresholds, boolean positive) {
        BigDecimal score;

        // If no thresholds yet, currently dealing with a non-derived model. Just return 0.
        if (thresholds == null) return new BigDecimalWithContext("0.0");

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

            //potential FIXME: if we run into performance issues during runtime or my json is too large, we need to offload
            BigDecimal[] evaluationDomain = generateEvaluationDomain(thresholds);
            BigDecimal[] densityArray = getDensityArray(evaluationDomain, thresholds);

            int closestIndex = PDFUtils.searchSorted(evaluationDomain, inValue);
            // tedious because java
            BigDecimal[] leftSideOfEvaluationDomain = Arrays.copyOfRange(evaluationDomain, 0, closestIndex);
            BigDecimal[] leftSideOfDensity = Arrays.copyOfRange(densityArray, 0, closestIndex);
            BigDecimal aucAtValueZero = PDFUtils.manualTrapezoidalRule(leftSideOfEvaluationDomain, leftSideOfDensity);

            if (!positive){
                //negative relationship with quality (majority of cases)
                score = new BigDecimalWithContext(1.0).subtract(aucAtValueZero);;
            }else{
                //positive relationship with quality
                score = aucAtValueZero;
            }
        }

        return score;
    }

    private BigDecimal[] generateEvaluationDomain(BigDecimal[] measureValues){
        //min and max for evaluation domain generation strategies - performance improvement instead of Collections.min/max
        BigDecimal min = new BigDecimalWithContext(Double.MAX_VALUE);
        BigDecimal max = new BigDecimalWithContext(Double.MIN_VALUE);
        for (int i = 0; i < measureValues.length; i++){
            BigDecimal myValue = measureValues[i];
            //min and max calculations, more efficient to do this in the for loop
            if (min.compareTo(myValue) == 1){
                min = myValue;
            }
            if (max.compareTo(myValue) == -1){
                max = myValue;
            }
        }
        // generate evaluation domain
        // FIXME better strategies for size of evaluation domain (Affects ability to generate accurate AUCs at the expense of time)
        BigDecimal[] evaluationDomain = generationStrategy.generateValues(min.intValue(), max.intValue(), measureValues.length);
        return evaluationDomain;
    }

    private BigDecimal[] getDensityArray(BigDecimal[] evaluationDomain, BigDecimal[] measureValues) {
        BigDecimal[] toRet = new BigDecimal[measureValues.length];

        for (int i = 0; i < toRet.length; i++){
            toRet[i] = PDFUtils.kernelDensityEstimator(evaluationDomain[i], measureValues, bandwidth, kernelFunction);
        }
        return toRet;
    }


}
