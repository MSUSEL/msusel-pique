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
package experiments.pdf;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import pique.utility.BigDecimalWithContext;
import pique.utility.PDFUtils;

import java.math.BigDecimal;

public class PDFResponse {

    @Getter
    private BigDecimal[] densityArray;

    @Expose
    @Getter
    private BigDecimal maxAUC;

    @Getter
    private BigDecimal[] transitionValues;

    @Expose
    @Getter
    private TransitionValueSummaryStatistics transitionValueSummaryStatistics;

    @Expose
    @Getter
    private long timeToRunMS;


    public PDFResponse(BigDecimal[] evaluationDomain, BigDecimal[] densityArray, long timeToRunMS){
        this.densityArray = densityArray;
        calculateMaxAUC(evaluationDomain, densityArray);
        calculateTransitionValues(densityArray);
        transitionValueSummaryStatistics = new TransitionValueSummaryStatistics(transitionValues);
        this.timeToRunMS = timeToRunMS;
    }

    private void calculateMaxAUC(BigDecimal[] evaluationDomain, BigDecimal[] densityArray){
        maxAUC = PDFUtils.manualTrapezoidalRule(evaluationDomain, densityArray);
    }

    private void calculateTransitionValues(BigDecimal[] densityArray){
        this.transitionValues = new BigDecimal[densityArray.length - 1]; //one smaller
        for (int i = 0; i < densityArray.length - 1; i++){
            transitionValues[i] = densityArray[i].subtract(densityArray[i+1]).abs();
        }
    }

    @Getter
    public class TransitionValueSummaryStatistics{

        @Expose
        private BigDecimal mean;
        @Expose
        private BigDecimal max;
        @Expose
        private BigDecimal min;
        @Expose
        private BigDecimal std;
        public TransitionValueSummaryStatistics(BigDecimal[] transitionValues){
            BigDecimal transitionArraySize = new BigDecimalWithContext(transitionValues.length);
            //all in 1 loop for fast
            BigDecimal sum = new BigDecimalWithContext(0.0);
            min = new BigDecimalWithContext(Double.MAX_VALUE);
            max = new BigDecimalWithContext(Double.MIN_VALUE);
            for (int i = 0; i < transitionArraySize.doubleValue(); i++){
                BigDecimal value = transitionValues[i];
                sum = sum.add(value);
                max = (max.compareTo(value) == 1) ? max : value;
                min = (min.compareTo(value) == -1) ? min : value;
            }
            mean = sum.divide(transitionArraySize, BigDecimalWithContext.getMC());
            //std calculations
            BigDecimal stdSum = new BigDecimalWithContext(0.0);
            for (int i = 0; i < transitionArraySize.doubleValue(); i++){
                stdSum = stdSum.add(new BigDecimalWithContext(Math.pow(mean.subtract(transitionValues[i]).doubleValue(), 2)));
            }
            std = new BigDecimalWithContext(Math.sqrt(stdSum.divide(transitionArraySize, BigDecimalWithContext.getMC()).doubleValue()));
        }
    }

}
