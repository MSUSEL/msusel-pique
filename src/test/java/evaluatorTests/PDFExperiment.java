package evaluatorTests;

import org.junit.Test;
import pique.utility.BigDecimalWithContext;

import java.math.BigDecimal;

public class PDFExperiment {

    public enum KernelFunction {
        GAUSSIAN,
        LINEAR,
        LAPLACIAN,
        POLYNOMIAL,
        EXPONENTIAL,
        MAHALONIBUS
    }


    @Test
    public void runExperiments(){
        for (KernelFunction d : KernelFunction.values()){

        }
    }


    //https://en.wikipedia.org/wiki/Kernel_(statistics)
    private BigDecimal uniformKernel(BigDecimal input){
        return new BigDecimalWithContext(0.5);
    }
    private BigDecimal triangularKernel(BigDecimal input){
        return new BigDecimalWithContext(1.0).subtract(input.abs());
    }
    private BigDecimal epanechnikovKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(0.75);  //  3/4
        BigDecimal variation = new BigDecimalWithContext(1 - Math.pow(input.doubleValue(), 2));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }
    private BigDecimal quarticKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(0.9375 ); //    15/16
        BigDecimal variation = new BigDecimalWithContext(Math.pow(1 - Math.pow(input.doubleValue(), 2), 2));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }

    private BigDecimal triweightKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(0.9375 ); //    15/16
        BigDecimal variation = new BigDecimalWithContext(Math.pow(1 - Math.pow(input.doubleValue(), 2), 3));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }

    private BigDecimal tricubeKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(0.86419753086);   //  70/81
        BigDecimal variation = new BigDecimalWithContext(Math.pow(1 - Math.pow(input.abs().doubleValue(), 3), 3));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }

    private BigDecimal gaussianKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(1 / Math.sqrt(2*Math.PI));
        BigDecimal variation = new BigDecimalWithContext(Math.exp(-0.5 * Math.pow(input.doubleValue(),2)));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }

    private BigDecimal cosineKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(Math.PI / 4);
        BigDecimal variation = new BigDecimalWithContext(Math.cos(Math.PI * input.doubleValue() / 2 ));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }

    private BigDecimal logisticKernel(BigDecimal input){
        BigDecimal variation = new BigDecimalWithContext(1 / (Math.exp(input.doubleValue()) + 2 + Math.exp(-1 * input.doubleValue())));
        return variation;
    }

    private BigDecimal sigmoidKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(2 / Math.PI);
        BigDecimal variation = new BigDecimalWithContext(1 / (Math.exp(input.doubleValue()) + Math.exp(-1 * input.doubleValue())));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }

    private BigDecimal silvermanKernel(BigDecimal input){
        double inputAbs = input.abs().doubleValue();
        BigDecimal constant = new BigDecimalWithContext(0.5);
        BigDecimal firstVariation = new BigDecimalWithContext(Math.exp(-1 * (inputAbs / Math.sqrt(2))));
        BigDecimal secondVariation = new BigDecimalWithContext(Math.sin((inputAbs / Math.sqrt(2)) + (Math.PI / 4)));
        return constant.multiply(firstVariation, BigDecimalWithContext.getMC()).multiply(secondVariation, BigDecimalWithContext.getMC());
    }

}
