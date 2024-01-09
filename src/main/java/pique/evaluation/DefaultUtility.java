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

import java.math.BigDecimal;

import pique.utility.BigDecimalWithContext;

// TODO (1.0): default utility function
public class DefaultUtility extends UtilityFunction {


    public DefaultUtility(){
        super("pique.evaluation.DefaultUtility", "Simple linear utility function based on min and max of threshold data");
    }

    public DefaultUtility(String name, String description){
        super(name, description);
    }
    /**
     * Apply linear interpolation to a 2-threshold array represeting the min and max values seen.
     * TODO: Investigate genericising this formula to n dimensions
     */
    @Override
    public BigDecimal utilityFunction(BigDecimal inValue, BigDecimal[] thresholds, boolean positive) {

        // TODO (1.0): Major fix needed as revealed here. Need to think about how to better go about cloning an
        //  instancing quality models and "Project" objects

        // If no thresholds yet, currently dealing with a non-derived model. Just return 0.
        if (thresholds == null) return new BigDecimalWithContext("0.0");

        if (!positive) {
            if (inValue.compareTo(thresholds[0]) <= 0  ) {//inValue <= thresholds[0]
            	return new BigDecimalWithContext("1.0");
            } else if (inValue.compareTo(thresholds[1]) >= 0) {//inValue >= thresholds[1]
            	return new BigDecimalWithContext("0.0");
            }else {
                return new BigDecimalWithContext("1.0").subtract(linearInterpolationTwoPoints(inValue, thresholds));
            }
        }

        else {
            if (inValue.compareTo(thresholds[0]) <= 0  ) {//inValue <= thresholds[0]
            	return new BigDecimalWithContext("0.0");
            } else if (inValue.compareTo(thresholds[1]) >= 0) {//inValue >= thresholds[1]
            	return new BigDecimalWithContext("1.0");
            } else {
                return linearInterpolationTwoPoints(inValue, thresholds);
            }
        }
    }

    private BigDecimal linearInterpolationTwoPoints(BigDecimal inValue, BigDecimal[] thresholds) {
        return (inValue.subtract(thresholds[0])).divide((thresholds[1].subtract(thresholds[0])),BigDecimalWithContext.getMC()); 
    }

}