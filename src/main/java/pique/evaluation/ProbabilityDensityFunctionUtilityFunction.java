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

import com.google.common.base.Preconditions;
import jep.Interpreter;
import jep.SharedInterpreter;
import pique.utility.BigDecimalWithContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ProbabilityDensityFunctionUtilityFunction extends UtilityFunction{

    private double bandwidth = 0.4;
    private int samplingSpace = 1000;


    public ProbabilityDensityFunctionUtilityFunction() {
        super("pique.evaluation.ProbabilityDensityFunctionUtilityFunction", "TODO - Write description in ProbabilityDensityFunctionUtilityFunction class");
    }

    @Override
    public BigDecimal utilityFunction(BigDecimal inValue, BigDecimal[] thresholds, boolean positive) {
        BigDecimalWithContext score = new BigDecimalWithContext(-10000);

        //are all values the same? - in O(n) apparently, said someone on stack overflow
        if (Arrays.stream(thresholds).distinct().count() == 1) {
            //one distinct value across the entire array
            BigDecimal compareValue = thresholds[0];
            //clean up if I get time
            if (inValue.compareTo(compareValue) == -1){
                //invalue is less than compareValue
                if (positive){
                    score = new BigDecimalWithContext(0.001);
                }else{
                    score = new BigDecimalWithContext(0.999);
                }
            }else{
                //inValue is greater than or equal to compareValue
                if (positive){
                    score = new BigDecimalWithContext(0.999);
                }else{
                    score = new BigDecimalWithContext(0.001);
                }
            }
        }else {
            //python call here
            try (Interpreter interp = new SharedInterpreter()) {
                interp.exec("from java.lang import System");
                interp.exec("import sys");
                interp.exec("import math");
                interp.exec("import numpy as np");
                interp.exec("import pandas as pd");
                interp.exec("import seaborn as sns");
                interp.exec("import matplotlib.pyplot as plt");
                interp.exec("from scipy import stats");
                interp.exec("from sklearn.preprocessing import MinMaxScaler");
                interp.exec("from IPython.display import Image");


                //////// start code here
                //convert thresholds to decimal[] objects
                double[] converted = new double[thresholds.length];
                for (int i = 0; i < thresholds.length; i++){
                    converted[i] = thresholds[i].doubleValue();
                }

                //set variables from java -> python
                interp.set("thresholds", converted);
                interp.set("new_data_point", inValue.doubleValue());

                //begin logic for analytics
                //interp.exec("for t in thresholds:\n\tSystem.out.println(t)");
                interp.exec("ax = sns.kdeplot(thresholds)");
                interp.exec("kde_lines = ax.get_lines()[-1]");
                interp.exec("kde_x,kde_y = kde_lines.get_data()");
                interp.exec("mask = kde_x < new_data_point");
                interp.exec("xx ,yy = kde_x[mask], kde_y[mask]");
                interp.exec("A = 0");
                interp.exec("N = len(xx)");
                interp.exec("for i in range(N-1):\n\tdx = xx[i+1] - xx[i]\n\tA = A + (dx/2)*(yy[i] + yy[i+1])");


                /** do plots, note that plots are an approximation of the value. The plots use a kernel density estimator (KDE) which
                * might produce a different value than the trapezoid method
                 *
                 * commented for now because we don't want to incorporate visuals in this graph

                // initial histogram plot
                interp.exec("plt.figure(figsize=(6,6))");
                interp.exec("plt.subplot(211)");
                interp.exec("sns.histplot(thresholds, bins=10, kde=False)");
                interp.exec("plt.xlim(left=0, right=max(thresholds))");

                // second spline with red dot plot
                interp.exec("plt.subplot(212)");
                interp.exec("sns.kdeplot(thresholds)");
                interp.exec("plt.fill_between(xx, yy, where=(xx <= new_data_point), color='gray', alpha=0.5)");
                interp.exec("plt.scatter(xx[-1], yy[-1], color='red', s=60)");
                interp.exec("plt.xlim(left=0, right=max(thresholds))");
                interp.exec("plt.tight_layout()");
                interp.exec("plt.savefig('testeser.png')");

                 */
                //back to java-land, our score value returns as a masked Double object, need to turn into a BigDecimal
                Object oScore = interp.getValue("1 - A");
                score = new BigDecimalWithContext((Double) oScore);

            }catch (Exception e){
                e.printStackTrace();
                System.out.println("unable to initialize python interpreter call");
            }
        }

        return score;
    }

    /***
     * Gaussian kernel function
     * @param input x value to evaluate with
     * @return corresponding y value from the gaussian kernel
     */
    private BigDecimal gaussianKernel(BigDecimal input){
        BigDecimal constant = new BigDecimalWithContext(1 / Math.sqrt(2*Math.PI));
        BigDecimal exponent = new BigDecimalWithContext(Math.exp(-0.5 * Math.pow(input.doubleValue(), 2)));
        return constant.multiply(exponent);
    }

    private BigDecimal[] kernelDensityEstimator(BigDecimal[] thresholds, BigDecimal[] evaluateRange){
        int size = thresholds.length;
        BigDecimal[] density = new BigDecimal[size];
        Arrays.fill(density, new BigDecimalWithContext(0));




        return density;
    }

    /***
     * Java equivalent function to perform python's np.linspace(), which generates a list of evenly spaced numbers between
     * two endpoints, with the number of evenly spaced numbers being parameterized.
     *
     * Java does have such a library in the ND4J library, but it feels unnecessary to use the full library (and therefore
     * potentially inherit any vulnerabilities) for one quick function. Additionally, I couldn't track down the runtime
     * complexity of ND4J.Linspace, but I doubt it is faster than linear, which this quick solution is linear.
     *
     * @param min starting value
     * @param max ending value
     * @param range number of evenly spaced values desired
     * @return array of BigDecimals that contains an array of equally spaced values between the parameters min and max
     */
    private BigDecimal[] linSpace(BigDecimal min, BigDecimal max, BigDecimal range){
        BigDecimal[] toRet = new BigDecimal[range.intValue()];
        BigDecimal stepSize = (max.subtract(min)).divide(range);
        for (int i = 0; i < toRet.length; i++){
            //bit awkward to cast BigDecimal to double so that it fits in the BigDecimalWithContext constructor,
            // consider making everything a bigDecimalWithContext
            toRet[i] = new BigDecimalWithContext((min.add(stepSize)).doubleValue());
        }
        return toRet;
    }

    /***
     * Java equivalent code to python's numpy.searchSorted() function. Follows default implementation with side=left.
     * Meaning return the value equal OR immediately lesser than the input value.
     *
     * Returns the closest value to an input value, in an input array. One assumption is that if
     *
     * @param array Array to search through for a value
     * @param value value to search for
     * @return index for the value equal to or the index of where the value would be inserted into the array
     */
        private int searchSorted(BigDecimal[] array, BigDecimal value){
            //sort in case it wasn't sorted already.
            Arrays.sort(array);

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


    /***
     * quick function to calculate the area under a curve using the trapezoid rule
     *
     * @param functionDomain The array of the x domain values of the function, basically x[min] through the x for which we supplied the y value
     * @param densityRange The array of density estimate values, computed from the kernel density estimator
     * @return area under the curve
     */
    private BigDecimal manualTrapezoidalRule(BigDecimal[] functionDomain, BigDecimal[] densityRange){
        BigDecimal area = new BigDecimalWithContext(0);
        for (int i = 1; i < functionDomain.length; i++){
            // x[i] - x[i-1]1
            BigDecimal xStep = functionDomain[i].subtract(functionDomain[i-1]);
            // y[i] - y[i-1]
            BigDecimal yStep = densityRange[i].subtract(densityRange[i-1]);
            // (x[i] - x[i-1]) * (y[i] - y[i-1])
            BigDecimal numerator = xStep.multiply(yStep);
            // area += (x[i] - x[i-1]) * (y[i] - y[i-1])) / 2
            area = area.add(numerator.divide(new BigDecimalWithContext(2.0)));
        }
        return area;
    }
}
