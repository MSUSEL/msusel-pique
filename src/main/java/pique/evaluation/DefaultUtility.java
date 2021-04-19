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

// TODO (1.0): default utility function
public class    DefaultUtility implements IUtilityFunction {

    /**
     * Apply linear interpolation to a 2-threshold array represeting the min and max values seen.
     * TODO: Investigate genericising this formula to n dimensions
     */
    @Override
    public double utilityFunction(double inValue, Double[] thresholds, boolean positive) {

        // TODO (1.0): Major fix needed as revealed here. Need to think about how to better go about cloning an
        //  instancing quality models and "Project" objects

        // If no thresholds yet, currently dealing with a non-derived model. Just return 0.
        if (thresholds == null) return 0.0;

        if (!positive) {
            if (inValue <= thresholds[0]) {
                return 1.0;
            } else if (inValue >= thresholds[1]) {
                return 0.0;
            }else {
                return 1 - linearInterpolationTwoPoints(inValue, thresholds);
            }
        }

        else {
            if (inValue <= thresholds[0]) {
                return 0.0;
            } else if (inValue >= thresholds[1]) {
                return  1.0;
            } else {
                return linearInterpolationTwoPoints(inValue, thresholds);
            }
        }
    }

    private double linearInterpolationTwoPoints(double inValue, Double[] thresholds) {
        return (inValue - thresholds[0]) * (1 / (thresholds[1] - thresholds[0]));
    }

}