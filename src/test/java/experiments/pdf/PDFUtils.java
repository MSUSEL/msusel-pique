package experiments.pdf;

import pique.utility.BigDecimalWithContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

public class PDFUtils {


    public static BigDecimal kernelDensityEstimator(BigDecimal input, PDFTreatment treatment){
        BigDecimal sum = new BigDecimalWithContext(0.0);
        //for convienence
        BigDecimal[] thresholds = treatment.getThresholds();
        double bandwidth = treatment.getBandwidth();
        for (int i = 0; i < thresholds.length; i++){
            //find distance from input to every threshold, normalized by height
            BigDecimal evaluationCriteria = (input.subtract(thresholds[i])).divide(new BigDecimalWithContext(bandwidth), BigDecimalWithContext.getMC());
            sum = sum.add(treatment.getKernelFunction().evaluateKernel(evaluationCriteria));
        }
        BigDecimal constant = new BigDecimalWithContext(thresholds.length * bandwidth);
        return sum.divide(constant,BigDecimalWithContext.getMC());
    }

    public static BigDecimal manualTrapezoidalRule(BigDecimal[] functionDomain, BigDecimal[] densityRange){
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

    public static int searchSorted(BigDecimal[] array, BigDecimal value){
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

    public static BigDecimal[] linSpace(BigDecimal min, BigDecimal max, BigDecimal range){
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

    public enum GenerationStrategy{
        EVENLY_SPACED_OVER_INTERVAL {
            @Override
            BigDecimal[] generateValues(int beginIndex, int endIndex, int count){
                BigDecimal[] toRet = new BigDecimal[count];
                double stepSize = (endIndex - beginIndex) / (double) count;
                double current = beginIndex;
                for (int i = beginIndex; current < endIndex; i++){
                    toRet[i] = new BigDecimalWithContext(current);
                    current += stepSize;
                }
                return toRet;
            }
        },
        RANDOMLY_SPACED_WITHIN_INTERVAL {
            Random rand = new Random(11235813);
            @Override
            BigDecimal[] generateValues(int beginIndex, int endIndex, int count){
                BigDecimal[] toRet = new BigDecimal[count];
                for (int i = 0; i < count; i++){
                    toRet[i] = new BigDecimalWithContext(beginIndex + (endIndex - beginIndex) * rand.nextDouble());
                }
                return toRet;
            }
        };

        abstract BigDecimal[] generateValues(int beginIndex, int endIndex, int count);
    }

    public enum KernelFunction {
        UNIFORM {
            @Override
            BigDecimal evaluateKernel(BigDecimal input) {
                return new BigDecimalWithContext(0.5);
            }
        },
        TRIANGULAR {
            @Override
            BigDecimal evaluateKernel(BigDecimal input) {
                return new BigDecimalWithContext(1.0).subtract(input.abs());
            }
        },
        EPANECHNIKOV {
            @Override
            BigDecimal evaluateKernel(BigDecimal input) {
                BigDecimal constant = new BigDecimalWithContext(0.75);  //  3/4
                BigDecimal variation = new BigDecimalWithContext(1 - Math.pow(input.doubleValue(), 2));
                return constant.multiply(variation, BigDecimalWithContext.getMC());
            }
        },
        QUARTIC {
            @Override
            BigDecimal evaluateKernel(BigDecimal input) {
                BigDecimal constant = new BigDecimalWithContext(0.9375 ); //    15/16
                BigDecimal variation = new BigDecimalWithContext(Math.pow(1 - Math.pow(input.doubleValue(), 2), 2));
                return constant.multiply(variation, BigDecimalWithContext.getMC());
            }
        },
        TRIWEIGHT {
            @Override
            BigDecimal evaluateKernel(BigDecimal input) {
                BigDecimal constant = new BigDecimalWithContext(0.9375 ); //    15/16
                BigDecimal variation = new BigDecimalWithContext(Math.pow(1 - Math.pow(input.doubleValue(), 2), 3));
                return constant.multiply(variation, BigDecimalWithContext.getMC());
            }
        },
        TRICUBE {
            @Override
            BigDecimal evaluateKernel(BigDecimal input) {
                BigDecimal constant = new BigDecimalWithContext(0.86419753086);   //  70/81
                BigDecimal variation = new BigDecimalWithContext(Math.pow(1 - Math.pow(input.abs().doubleValue(), 3), 3));
                return constant.multiply(variation, BigDecimalWithContext.getMC());
            }
        },
        GAUSSIAN {
            @Override
            BigDecimal evaluateKernel(BigDecimal input) {
                BigDecimal constant = new BigDecimalWithContext(1 / Math.sqrt(2*Math.PI));
                BigDecimal variation = new BigDecimalWithContext(Math.exp(-0.5 * Math.pow(input.doubleValue(),2)));
                return constant.multiply(variation, BigDecimalWithContext.getMC());
            }
        },
        COSINE {
            @Override
            BigDecimal evaluateKernel(BigDecimal input) {
                BigDecimal constant = new BigDecimalWithContext(Math.PI / 4);
                BigDecimal variation = new BigDecimalWithContext(Math.cos(Math.PI * input.doubleValue() / 2 ));
                return constant.multiply(variation, BigDecimalWithContext.getMC());
            }
        },
        LOGISTIC {
            @Override
            BigDecimal evaluateKernel(BigDecimal input) {
                BigDecimal variation = new BigDecimalWithContext(1 / (Math.exp(input.doubleValue()) + 2 + Math.exp(-1 * input.doubleValue())));
                return variation;
            }
        },
        SIGMOID {
            @Override
            BigDecimal evaluateKernel(BigDecimal input) {
                BigDecimal constant = new BigDecimalWithContext(2 / Math.PI);
                BigDecimal variation = new BigDecimalWithContext(1 / (Math.exp(input.doubleValue()) + Math.exp(-1 * input.doubleValue())));
                return constant.multiply(variation, BigDecimalWithContext.getMC());
            }
        },
        SILVERMAN {
            @Override
            BigDecimal evaluateKernel(BigDecimal input) {
                double inputAbs = input.abs().doubleValue();
                BigDecimal constant = new BigDecimalWithContext(0.5);
                BigDecimal firstVariation = new BigDecimalWithContext(Math.exp(-1 * (inputAbs / Math.sqrt(2))));
                BigDecimal secondVariation = new BigDecimalWithContext(Math.sin((inputAbs / Math.sqrt(2)) + (Math.PI / 4)));
                return constant.multiply(firstVariation, BigDecimalWithContext.getMC()).multiply(secondVariation, BigDecimalWithContext.getMC());
            }
        };
        abstract BigDecimal evaluateKernel(BigDecimal input);
    }
}