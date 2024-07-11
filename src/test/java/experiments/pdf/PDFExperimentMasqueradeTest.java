package experiments.pdf;

import org.junit.Test;
import pique.utility.BigDecimalWithContext;

import java.math.BigDecimal;
import java.util.Arrays;

public class PDFExperimentMasqueradeTest {



    @Test
    public void runExperiments(){
        BigDecimal[] thresholds = PDFUtils.GenerationStrategy.RANDOMLY_SPACED_WITHIN_INTERVAL.generateValues(0, 100, 500);
        BigDecimal[] evaluationDomain = PDFUtils.GenerationStrategy.EVENLY_SPACED_OVER_INTERVAL.generateValues(0, 100, 1000);
        PDFUtils.KernelFunction kernelFunction = PDFUtils.KernelFunction.GAUSSIAN;
        double bandwidth = 0.4;
        PDFTreatment t = new PDFTreatment(thresholds, evaluationDomain, kernelFunction, bandwidth);
        BigDecimal[] densityArray = getDensityArray(t);
        Arrays.stream(densityArray).forEach(System.out::println);
    }

    private BigDecimal[] getDensityArray(PDFTreatment treatment){
        BigDecimal[] toRet = new BigDecimal[treatment.getEvaluationDomain().length];
        for (int i = 0; i < toRet.length; i++){
            toRet[i] = PDFUtils.kernelDensityEstimator(treatment.getEvaluationDomain()[i], treatment);
        }
        return toRet;
    }






}
