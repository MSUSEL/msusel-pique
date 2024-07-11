package experiments.pdf;

import pique.utility.BigDecimalWithContext;

import java.math.BigDecimal;

public class KernelFunctions {

    //https://en.wikipedia.org/wiki/Kernel_(statistics)
    protected static BigDecimal uniformKernel(BigDecimal input){
        return new BigDecimalWithContext(0.5);
    }
    protected static BigDecimal triangularKernel(BigDecimal input){
        return new BigDecimalWithContext(1.0).subtract(input.abs());
    }
    protected static BigDecimal epanechnikovKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(0.75);  //  3/4
        BigDecimal variation = new BigDecimalWithContext(1 - Math.pow(input.doubleValue(), 2));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }
    protected static BigDecimal quarticKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(0.9375 ); //    15/16
        BigDecimal variation = new BigDecimalWithContext(Math.pow(1 - Math.pow(input.doubleValue(), 2), 2));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }

    protected static BigDecimal triweightKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(0.9375 ); //    15/16
        BigDecimal variation = new BigDecimalWithContext(Math.pow(1 - Math.pow(input.doubleValue(), 2), 3));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }

    protected static BigDecimal tricubeKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(0.86419753086);   //  70/81
        BigDecimal variation = new BigDecimalWithContext(Math.pow(1 - Math.pow(input.abs().doubleValue(), 3), 3));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }

    protected static BigDecimal gaussianKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(1 / Math.sqrt(2*Math.PI));
        BigDecimal variation = new BigDecimalWithContext(Math.exp(-0.5 * Math.pow(input.doubleValue(),2)));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }

    protected static BigDecimal cosineKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(Math.PI / 4);
        BigDecimal variation = new BigDecimalWithContext(Math.cos(Math.PI * input.doubleValue() / 2 ));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }

    protected static BigDecimal logisticKernel(BigDecimal input){
        BigDecimal variation = new BigDecimalWithContext(1 / (Math.exp(input.doubleValue()) + 2 + Math.exp(-1 * input.doubleValue())));
        return variation;
    }

    protected static BigDecimal sigmoidKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(2 / Math.PI);
        BigDecimal variation = new BigDecimalWithContext(1 / (Math.exp(input.doubleValue()) + Math.exp(-1 * input.doubleValue())));
        return constant.multiply(variation, BigDecimalWithContext.getMC());
    }

    protected static BigDecimal silvermanKernel(BigDecimal input){
        double inputAbs = input.abs().doubleValue();
        BigDecimal constant = new BigDecimalWithContext(0.5);
        BigDecimal firstVariation = new BigDecimalWithContext(Math.exp(-1 * (inputAbs / Math.sqrt(2))));
        BigDecimal secondVariation = new BigDecimalWithContext(Math.sin((inputAbs / Math.sqrt(2)) + (Math.PI / 4)));
        return constant.multiply(firstVariation, BigDecimalWithContext.getMC()).multiply(secondVariation, BigDecimalWithContext.getMC());
    }
}
