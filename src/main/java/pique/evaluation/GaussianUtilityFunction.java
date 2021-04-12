package pique.evaluation;

import com.sun.scenario.effect.impl.state.GaussianShadowState;
import org.apache.commons.math3.analysis.function.Gaussian;

public class GaussianUtilityFunction implements IUtilityFunction {


    /**
     * Apply a utility function given an input value and thresholds
     *
     * @param inValue    The value before applying the utility functions
     * @param thresholds The thresholds to use in the utility function. Likely comes from benchmarking
     * @param positive   Whether or not the input value has a positive or negative effect on quality
     * @return The value of inValue after applying the utility function
     */
    @Override
    public double utilityFunction(double inValue, Double[] thresholds, boolean positive) {

        // If no thresholds yet, currently dealing with a non-derived model. Just return 0.
        if (thresholds == null) {
            return 0.0;
        }
        //calculate mean as average between thresholds
        double mean = (thresholds[0] + thresholds[1]) / 2.0;

        //calculate sigma as difference between mean and threshold
        double sigma = mean - thresholds[0];

        Gaussian function = new Gaussian(mean, sigma);
        double output = function.value(inValue);

        return output;

    }
}
