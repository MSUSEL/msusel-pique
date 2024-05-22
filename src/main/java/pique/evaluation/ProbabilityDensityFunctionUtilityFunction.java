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

import jep.Interpreter;
import jep.SharedInterpreter;
import pique.utility.BigDecimalWithContext;

import java.math.BigDecimal;
import java.util.Arrays;

public class ProbabilityDensityFunctionUtilityFunction extends UtilityFunction{


    public ProbabilityDensityFunctionUtilityFunction() {
        super("Probability Density Function (PDF) Utility Function", "TODO -- Redempta - Write description in ProbabilityDensityFunctionUtilityFunction class");
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
                interp.exec("ax = sns.kdeplot(thresholds)");
                interp.exec("kde_lines = ax.get_lines()[-1]");
                interp.exec("kde_x,kde_y = kde_lines.get_data()");
                interp.exec("mask = kde_x < new_data_point");
                interp.exec("xx ,yy = kde_x[mask], kde_y[mask]");
                interp.exec("A = 0");
                interp.exec("N = len(xx)");
                interp.exec("for i in range(N-1):\n\tdx = xx[i+1] - xx[i]\n\tA = A + (dx/2)*(yy[i] + yy[i+1])");


                /* do plots, note that plots are an approximation of the value. The plots use a kernel density estimator (KDE) which
                * might produce a different value than the trapezoid method
                */
                // initial histogram plot
                interp.exec("plt.figure(figsize=(6,6))");
                interp.exec("plt.subplot(211)");
                interp.exec("sns.histplot(thresholds, bins=10, kde=False)");
                interp.exec("plt.xlim(left=0, right=max(thresholds))");

                // second spline with red dot plot
                interp.exec("plt.subplot(212)");
                interp.exec("plt.fill_between(xx, yy, where=(xx <= new_data_point), color='gray', alpha=0.5)");
                interp.exec("plt.scatter(xx[-1], yy[-1], color='red', s=60)");
                interp.exec("plt.ylabel('Density')");
                interp.exec("plt.xlim(left=0, right=max(thresholds))");
                interp.exec("plt.tight_layout()");
                interp.exec("plt.savefig('testeser.png')");


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
}
