package experiments.pdf;

import lombok.Getter;
import pique.utility.BigDecimalWithContext;

import java.math.BigDecimal;

public class PDFResponse {

    @Getter
    private BigDecimal[] densityArray;

    @Getter
    private BigDecimal maxAUC;

    @Getter
    private BigDecimal[] transitionValues;

    @Getter
    private TransitionValueSummaryStatistics transitionValueSummaryStatistics;


    public PDFResponse(BigDecimal[] evaluationDomain, BigDecimal[] densityArray){
        this.densityArray = densityArray;
        calculateMaxAUC(evaluationDomain, densityArray);
        calculateTransitionValues(densityArray);
        transitionValueSummaryStatistics = new TransitionValueSummaryStatistics(transitionValues);
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

        private BigDecimal mean;
        private BigDecimal max;
        private BigDecimal min;
        private BigDecimal std;
        public TransitionValueSummaryStatistics(BigDecimal[] transitionValues){
            BigDecimal transitionArraySize = new BigDecimalWithContext(transitionValues.length);
            //all in 1 loop for fast
            BigDecimal sum = new BigDecimalWithContext(0.0);
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
