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

import pique.utility.BigDecimalWithContext;

import java.math.BigDecimal;

import org.apache.commons.math3.analysis.function.Gaussian;

public class GaussianUtilityFunction extends UtilityFunction {


    public GaussianUtilityFunction() {
        super("pique.evaluation.GaussianUtilityFunction", "Basic GaussianUtility function that fills a Gaussian distribution based on two threshold values (min and max)");
    }

    public GaussianUtilityFunction(String name, String description) {
        super(name, description);
    }

    /**
     * Apply a utility function given an input value and thresholds
     *
     * @param inValue    The value before applying the utility functions
     * @param thresholds The thresholds to use in the utility function. Likely comes from benchmarking
     * @param positive   Whether or not the input value has a positive or negative effect on quality
     * @return The value of inValue after applying the utility function
     */
    @Override
    public BigDecimal utilityFunction(BigDecimal inValue, BigDecimal[] thresholds, boolean positive) {

    	// Note: we have to use Doubles here due to the Gaussian function taking Double as input. 
    	// We would have to implement a BigDecimal Guassian to use BigDecimal in this function
    	
        // If no thresholds yet, currently dealing with a non-derived model. Just return 0.
        if (thresholds == null) {
            return new BigDecimalWithContext("0.0");
        }
        //calculate mean as average between thresholds
        Double mean = (thresholds[0].doubleValue() + thresholds[1].doubleValue()) / 2.0;

        //calculate sigma as difference between mean and threshold
        Double sigma = mean.doubleValue() - thresholds[0].doubleValue();

            Gaussian function = new Gaussian(mean, sigma);
        Double output = function.value(inValue.doubleValue());

        return new BigDecimalWithContext("" + output);

    }
}
