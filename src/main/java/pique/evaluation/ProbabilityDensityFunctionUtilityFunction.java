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
        //are all values the same?
        BigDecimalWithContext score;
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

                interp.set("thresholds", converted);
                Object a = interp.getValue("thresholds");
                //this works, surprisingly
//                for (Object b : (BigDecimal[]) a){
//                    System.out.println(b);
//                }
                interp.exec("ax = sns.kdeplot(thresholds)");
                interp.exec("System.out.println(ax)");


                interp.close();

            }catch (Exception e){
                e.printStackTrace();
                System.out.println("unable to initialize python interpreter call");
            }
            score = new BigDecimalWithContext(-10000);
        }

        return score;
    }
}
