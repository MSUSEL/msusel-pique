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
import org.junit.Ignore;
import org.junit.Test;
import pique.evaluation.DefaultUtility;
import pique.evaluation.GAMUtilityFunction;
import pique.utility.BigDecimalWithContext;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * ignoring test because this class was used for GAM testing/research purposes
 */
@Ignore
public class GAMUtilityTest {

    private GAMUtilityFunction GAMUtility;
    private DefaultUtility defaultUtility;

    public GAMUtilityTest(){
        GAMUtility = new GAMUtilityFunction();
        defaultUtility = new DefaultUtility();
    }


    /***
     * To make this functional in an acceptance testing light I made changes to
     */
    @Test
    public void testGAMUtilityFunction(){
        BigDecimal[] values = getBigDecimal(new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,23,0,0,0,0,0,0,0,0,0,0,30,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,
            0,0,0,0,0,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,4,0,0,0,0,1,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,9,0,0,0,0,8,
            0,0,3,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,43,1,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,
            0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,87,
            0,0,0,0,0,0,0,0,0,0,951,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,19,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});


        BigDecimal test1 = GAMUtility.utilityFunction(new BigDecimalWithContext(2), values, true);


        System.out.println(test1);

//        BigDecimal[] simpleThresholds = new BigDecimalWithContext[]{new BigDecimalWithContext(3), new BigDecimalWithContext(7)};
//        BigDecimal positiveResult = GAMUtility.utilityFunction(new BigDecimalWithContext("7"), simpleThresholds, true);
//        BigDecimal positiveResult2 = GAMUtility.utilityFunction(new BigDecimalWithContext("3"), simpleThresholds, true);
//        BigDecimal positiveResult3 = GAMUtility.utilityFunction(new BigDecimalWithContext("5"), simpleThresholds, true);
//        assertEquals(0.1209, positiveResult.doubleValue(), 0.005);
//        assertEquals(0.1209, positiveResult2.doubleValue(), 0.005);
//        assertEquals(0.1994, positiveResult3.doubleValue(), 0.005);
    }

    /**
     * Creates the GAM charts for all the vulnerabilities
     * in the PIQUE bin and PIQUE c# csv files.
     *
     * Note: I'm fairly certain if you try to create a graph
     * with a column of only zeroes, then the R code runs into
     * some errors. So it only creates graphs for columns that
     * contain at least one non-zero value.
     *
     * @throws FileNotFoundException
     */
    @Test
    public void createGraphs() throws FileNotFoundException {
        // PIQUE bin csv
        CSVReaderTool bin_reader = new CSVReaderTool("./src/test/java/pique-bin-full.csv");
        bin_reader.setArrays();

        // Create graphs for every vulnerability in PIQUE bin csv
        for (String vulnerability : bin_reader.getColNames()) {
            int[] vals1 = bin_reader.extractOneColumnValues(vulnerability);
            for (int i = 0; i < vals1.length; i++) {
                if (vals1[i] != 0) {
                    createChart(vals1, "Binary", vulnerability);
                    break;
                }
            }
        }

        // -----------------------------------------------------------------------------------------------

        // PIQUE C# csv
        CSVReaderTool csharp_reader = new CSVReaderTool("./src/test/java/pique-csharp-sec-full.csv");
        csharp_reader.setArrays();

        // Create graphs for every vulnerability in PIQUE C# csv
        for (String vulnerability : csharp_reader.getColNames()) {
            int[] vals2 = csharp_reader.extractOneColumnValues(vulnerability);
            for (int i = 0; i < vals2.length; i++) {
                if (vals2[i] != 0) {
                    System.out.println(vulnerability);
                    createChart(vals2, "CSharp",vulnerability);
                    break;
                }
            }
        }
    }

    /**
     * Creates the GAM charts for the vulnerabilities.
     *
     * @param intVals The occurrences of a certain vulnerability.
     */
    public void createChart(int[] intVals, String language, String vulnerability) {
        this.GAMUtility = new GAMUtilityFunction(language, vulnerability);
        BigDecimal[] gamValues = getBigDecimal(intVals);

        double[] quartiles = quartiles(Arrays.stream(intVals).asDoubleStream().toArray());

        BigDecimal meanEvaluation = new BigDecimalWithContext(Arrays.stream(intVals).average().getAsDouble());
        BigDecimal q1Evaluation = new BigDecimalWithContext(quartiles[0]);
        BigDecimal q2Evaluation = new BigDecimalWithContext(quartiles[1]);
        BigDecimal q3Evaluation = new BigDecimalWithContext(quartiles[2]);

        BigDecimal meanGAMResult = GAMUtility.utilityFunction(meanEvaluation, gamValues, true);
        BigDecimal q1GAMResult = GAMUtility.utilityFunction(q1Evaluation, gamValues, true);
        BigDecimal q2GAMResult = GAMUtility.utilityFunction(q2Evaluation, gamValues, true);
        BigDecimal q3GAMResult = GAMUtility.utilityFunction(q3Evaluation, gamValues, true);


        int min = Arrays.stream(intVals).min().getAsInt();
        int max = Arrays.stream(intVals).max().getAsInt();

        BigDecimal[] linearInterpValues = getBigDecimal(new int[]{min, max});

        BigDecimal meanLinearInterpResult = defaultUtility.utilityFunction(meanEvaluation, linearInterpValues, true);
        BigDecimal q1LinearInterpResult = defaultUtility.utilityFunction(q1Evaluation, linearInterpValues, true);
        BigDecimal q2LinearInterpResult = defaultUtility.utilityFunction(q2Evaluation, linearInterpValues, true);
        BigDecimal q3LinearInterpResult = defaultUtility.utilityFunction(q3Evaluation, linearInterpValues, true);

        System.out.println("Data stats");
        System.out.println("\tMean: " + meanEvaluation);
        System.out.println("\tQ1: " + q1Evaluation);
        System.out.println("\tQ2 (Median): " + q2Evaluation);
        System.out.println("\tQ3: " + q3Evaluation);


        System.out.println("GAM results");
        System.out.println("\tMean: " + meanGAMResult);
        System.out.println("\tQ1: " + q1GAMResult);
        System.out.println("\tQ2 (Median): " + q2GAMResult);
        System.out.println("\tQ3: " + q3GAMResult);
        System.out.println("Linear Interp results");
        System.out.println("\tMean: " + meanLinearInterpResult);
        System.out.println("\tQ1: " + q1LinearInterpResult);
        System.out.println("\tQ2 (Median): " + q2LinearInterpResult);
        System.out.println("\tQ3: " + q3LinearInterpResult);
    }

    /**
     * good example
     */
    @Test
    public void compareCWE_676(){
        int[] intVals = new int[]{0,8,0,88,53,38,84,379,27,0,0,0,0,0,0,265,189,0,0,68,48,0,47,62,54,52,0,48,0,0,29,22,13,
            0,30,32,6,35,31,4,5,73,0,0,1149,140,0,27,53,19,25,23,0,25,29,27,14,29,20,9,38,86,4,89,0,0,26,0,89,124,87,66,
            52,18,2,103,40,70,33,15,91,56,0,0,64,162,39,125,0,67,376,86,18,26,37,5,89,169,89,83,99,76,25,27,102,26,0,31,
            128,7,22,33,0,0,23,10,347,1,69,41,26,0,60,0,0,9,0,0,65,105,0,79,0,2,89,85,85,85,89,89,2,0,0,0,85,47,0,300,127,
            149,0,77,336,64,118,259,169,54,170,210,140,77,0,0,77,54,83,141,0,179,80,78,0,184,0,0,83,0,0,55,86,0,32,34,30,
            29,0,21,48,34,26,0,0,0,0,0,0,37,61,0,0,84,34,149,15,37,17,5,127,0,0,0,0,0,0,0,0,22,44,22,24,22,24,23,19,19,19,
            19,192,21,21,0,21,19,20,20,19,22,34,18,11,189,87,15,0,85,47,0,19,258,25,42,0,62,0,27,0,34,29,19,18,10,170,7,
            22,22,0,2,0,54,48,0,647,0,640,228,676,644,197,640,650,190,0,188,161,0,0,64,0,0,0,56,0,144,58,31,0,34,81,65,8,
            16,18,0,0,1,54,89,89,78,81,0,385,404,403,189,273,194,0,0,27,0,43,102,10,73,0,15,3,0,24,122,66,33,30,31,68,11,
            4,111,15,127,119,24,86,0,22,0,0,0,9,81,113,0,82,32,3,0,0,3,0,41,23,149,2,0,0,0,0,80,41,0,0,12,30,87,89,89,0,
            85,0,53,12,47,37,0,362,96,2,0,0,86,86,192,204,192,194,192,192,192,192,0,192,192,192,0,192,10,38,329,6,53,0,
            22,24,49,224,47,0,1,174,27,21,188,188,20,2,31,24,0,0,0,10,10,110,21,41,104,3,20,32,0,79,389,9,10,47,62,88,0,
            106,0,13,14,0,14,14,14,0,38,16,0,537,24,20,20,0,56,50,26,91,0,0,0,80,230,116,102,162,141,78,86,73,87,30,0,0,
            30,0,20,29,0,86,16,2,1,1,0,9,3,35,226,62,0,3,19,6,3,68,22,26,359,0,0,0,0,0,0,0,0,0,54,28,125,22,0,0,0,0,0,69,
            16,14,10,217,44,0,0,0,0,0,12,0,9,16,17,5,148,29,69,40,12,16,157,0,0,0,0,0,114,0,224,14,85,0,392,29,0,39,67,4,
            53,0,87,28,0,0,0,0,0,0,0,82,502,0,0,0,28,0,0,0,40,0,8,13,10,0,2,0,0,9,1,1,1,0,0,0,0,0,0,0,0,0,2,5,0,2,0,6,0,3,
            12,0,0,0,0,0,1,59,9,25,13,14,0,77,0,37,0,10,15,0,0,0,269,183,69,191,182,0,28};

        BigDecimal[] gamValues = getBigDecimal(intVals);

        double[] quartiles = quartiles(Arrays.stream(intVals).asDoubleStream().toArray());

        BigDecimal meanEvaluation = new BigDecimalWithContext(Arrays.stream(intVals).average().getAsDouble());
        BigDecimal q1Evaluation = new BigDecimalWithContext(quartiles[0]);
        BigDecimal q2Evaluation = new BigDecimalWithContext(quartiles[1]);
        BigDecimal q3Evaluation = new BigDecimalWithContext(quartiles[2]);

        BigDecimal meanGAMResult = GAMUtility.utilityFunction(meanEvaluation, gamValues, true);
        BigDecimal q1GAMResult = GAMUtility.utilityFunction(q1Evaluation, gamValues, true);
        BigDecimal q2GAMResult = GAMUtility.utilityFunction(q2Evaluation, gamValues, true);
        BigDecimal q3GAMResult = GAMUtility.utilityFunction(q3Evaluation, gamValues, true);


        int min = Arrays.stream(intVals).min().getAsInt();
        int max = Arrays.stream(intVals).max().getAsInt();

        BigDecimal[] linearInterpValues = getBigDecimal(new int[]{min, max});

        BigDecimal meanLinearInterpResult = defaultUtility.utilityFunction(meanEvaluation, linearInterpValues, true);
        BigDecimal q1LinearInterpResult = defaultUtility.utilityFunction(q1Evaluation, linearInterpValues, true);
        BigDecimal q2LinearInterpResult = defaultUtility.utilityFunction(q2Evaluation, linearInterpValues, true);
        BigDecimal q3LinearInterpResult = defaultUtility.utilityFunction(q3Evaluation, linearInterpValues, true);

        System.out.println("Data stats");
        System.out.println("\tMean: " + meanEvaluation);
        System.out.println("\tQ1: " + q1Evaluation);
        System.out.println("\tQ2 (Median): " + q2Evaluation);
        System.out.println("\tQ3: " + q3Evaluation);


        System.out.println("GAM results");
        System.out.println("\tMean: " + meanGAMResult);
        System.out.println("\tQ1: " + q1GAMResult);
        System.out.println("\tQ2 (Median): " + q2GAMResult);
        System.out.println("\tQ3: " + q3GAMResult);
        System.out.println("Linear Interp results");
        System.out.println("\tMean: " + meanLinearInterpResult);
        System.out.println("\tQ1: " + q1LinearInterpResult);
        System.out.println("\tQ2 (Median): " + q2LinearInterpResult);
        System.out.println("\tQ3: " + q3LinearInterpResult);
    }


    /***
     * linear ?!? (bad example)
     */
    @Test
    public void compareCVE_CWE_399(){
        int[] intVals = new int[]{0,4,0,3,4,4,4,4,4,0,1,1,1,1,1,6,4,4,5,4,4,4,4,4,4,4,4,4,5,5,3,4,4,6,4,5,5,4,1,5,5,4,4,
            5,4,4,4,3,4,1,4,4,1,5,4,4,4,4,5,5,4,4,4,4,1,5,4,0,1,4,1,4,3,4,5,1,6,4,4,4,4,4,5,5,4,4,4,4,4,5,3,1,4,4,4,5,4,
            6,6,6,6,6,6,6,4,3,4,6,1,5,4,4,4,4,4,4,4,5,4,4,4,6,4,0,4,4,0,4,1,4,1,4,1,5,4,4,4,4,4,4,5,1,1,4,4,4,4,1,1,1,1,
            1,1,1,1,1,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,1,4,4,4,4,4,4,4,0,4,4,4,4,0,0,0,0,1,4,3,3,4,1,1,5,1,4,
            4,4,4,1,4,4,4,4,4,4,4,3,3,1,3,3,3,3,3,3,3,3,3,4,4,4,0,5,5,5,4,5,5,5,5,4,1,6,1,5,4,1,4,3,3,5,4,0,4,4,4,0,6,4,
            4,4,4,6,4,1,1,4,4,1,1,1,1,4,1,4,3,4,4,4,4,4,4,4,4,4,1,1,1,4,4,3,1,4,6,4,4,5,4,4,4,5,4,4,5,4,5,4,4,4,4,1,1,1,
            1,1,4,4,4,0,4,5,4,6,4,1,4,0,4,5,5,4,3,4,4,1,5,4,4,4,4,4,4,4,3,3,4,5,0,0,4,3,3,12,1,1,4,4,4,0,5,5,4,5,4,4,1,1,
            4,1,1,4,4,4,6,4,4,4,4,4,4,4,4,4,3,4,4,3,4,5,0,4,4,4,4,1,4,4,4,4,4,4,0,4,4,4,4,4,4,4,4,4,4,0,4,4,4,1,4,0,5,4,4,
            4,4,4,4,5,4,4,4,4,3,4,4,4,4,3,4,6,4,1,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,1,1,0,4,4,4,5,0,1,4,4,5,1,1,4,4,2,4,
            4,4,4,12,4,1,4,4,0,0,4,13,4,1,1,4,4,5,5,5,5,5,6,5,3,1,5,6,4,4,6,4,4,5,4,4,4,4,0,4,4,4,4,4,3,4,1,4,5,4,4,1,4,4,
            4,4,4,4,3,4,4,4,4,4,4,1,5,1,4,5,4,4,4,4,6,4,4,3,4,0,3,4,4,0,1,4,4,0,4,1,4,4,4,4,4,5,4,4,4,4,4,4,4,4,4,4,1,6,4,
            4,4,4,4,4,4,5,4,5,3,4,5,5,5,5,5,5,5,5,1,5,5,5,4,5,5,1,5,5,5,5,5,5,4,5,4,1,1,5,5,4,3,4,5,5,5,4,4,4,4,1,1,4,4,0,
            0,5,4,4,4,4,4,1,4};

        BigDecimal[] gamValues = getBigDecimal(intVals);

        double[] quartiles = quartiles(Arrays.stream(intVals).asDoubleStream().toArray());

        BigDecimal meanEvaluation = new BigDecimalWithContext(Arrays.stream(intVals).average().getAsDouble());
        BigDecimal q1Evaluation = new BigDecimalWithContext(quartiles[0]);
        BigDecimal q2Evaluation = new BigDecimalWithContext(quartiles[1]);
        BigDecimal q3Evaluation = new BigDecimalWithContext(quartiles[2]);

        BigDecimal meanGAMResult = GAMUtility.utilityFunction(meanEvaluation, gamValues, true);
        BigDecimal q1GAMResult = GAMUtility.utilityFunction(q1Evaluation, gamValues, true);
        BigDecimal q2GAMResult = GAMUtility.utilityFunction(q2Evaluation, gamValues, true);
        BigDecimal q3GAMResult = GAMUtility.utilityFunction(q3Evaluation, gamValues, true);


        int min = Arrays.stream(intVals).min().getAsInt();
        int max = Arrays.stream(intVals).max().getAsInt();

        BigDecimal[] linearInterpValues = getBigDecimal(new int[]{min, max});

        BigDecimal meanLinearInterpResult = defaultUtility.utilityFunction(meanEvaluation, linearInterpValues, true);
        BigDecimal q1LinearInterpResult = defaultUtility.utilityFunction(q1Evaluation, linearInterpValues, true);
        BigDecimal q2LinearInterpResult = defaultUtility.utilityFunction(q2Evaluation, linearInterpValues, true);
        BigDecimal q3LinearInterpResult = defaultUtility.utilityFunction(q3Evaluation, linearInterpValues, true);

        System.out.println("Data stats");
        System.out.println("\tMean: " + meanEvaluation);
        System.out.println("\tQ1: " + q1Evaluation);
        System.out.println("\tQ2 (Median): " + q2Evaluation);
        System.out.println("\tQ3: " + q3Evaluation);


        System.out.println("GAM results");
        System.out.println("\tMean: " + meanGAMResult);
        System.out.println("\tQ1: " + q1GAMResult);
        System.out.println("\tQ2 (Median): " + q2GAMResult);
        System.out.println("\tQ3: " + q3GAMResult);
        System.out.println("Linear Interp results");
        System.out.println("\tMean: " + meanLinearInterpResult);
        System.out.println("\tQ1: " + q1LinearInterpResult);
        System.out.println("\tQ2 (Median): " + q2LinearInterpResult);
        System.out.println("\tQ3: " + q3LinearInterpResult);
    }


    /**
     * nice W curve
     */
    @Test
    public void compareCVE_CWE_94(){
        int[] intVals = new int[]{0,1,0,1,1,1,1,1,1,0,0,0,0,0,0,2,1,1,2,1,1,1,1,1,1,1,1,1,2,2,1,1,1,2,1,2,2,1,0,2,2,1,1,
            2,1,1,1,1,1,0,1,1,0,2,1,1,1,1,2,2,1,1,1,1,0,2,1,0,0,1,0,1,1,1,2,0,2,1,1,1,1,1,2,2,1,1,1,1,1,2,1,0,1,1,1,2,1,
            2,2,2,2,2,2,2,1,1,1,2,0,2,1,1,1,1,1,1,1,2,1,1,1,2,1,0,1,1,0,1,0,1,0,1,0,2,1,1,1,1,1,1,2,0,0,1,1,1,1,0,0,0,0,
            0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,0,0,0,0,0,1,1,1,1,0,0,2,0,1,
            1,1,1,0,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,0,2,2,2,1,2,2,2,2,1,0,2,0,2,1,0,1,1,1,2,1,0,1,1,1,0,2,1,
            1,1,1,2,1,0,0,1,1,0,0,0,0,1,0,1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,0,1,2,1,1,2,1,1,1,2,1,1,2,1,2,1,1,1,1,0,0,0,
            0,0,1,1,1,0,1,2,1,2,1,0,1,0,1,2,2,1,1,1,1,0,2,1,1,1,1,1,1,1,1,1,1,2,0,0,1,1,1,1,0,0,1,1,1,0,2,2,1,2,1,1,0,0,
            1,0,0,1,1,1,2,1,1,1,1,1,1,1,1,1,1,1,1,2,1,2,0,1,1,1,1,0,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1,0,2,1,
            1,1,1,1,1,2,1,1,1,1,1,1,1,1,1,1,1,2,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,2,0,0,1,1,2,0,0,1,1,
            0,1,1,1,1,1,1,0,1,1,0,0,1,1,1,0,0,1,1,2,2,2,2,2,2,2,1,0,2,2,1,1,2,1,1,2,1,1,1,1,0,1,1,1,1,1,1,1,0,1,2,1,1,0,
            1,1,1,1,1,1,1,1,1,1,1,1,1,0,2,0,1,2,1,1,1,1,2,1,1,1,1,0,1,1,1,0,0,1,1,0,1,0,1,1,1,1,1,2,1,1,1,1,1,1,1,1,1,1,
            0,2,1,1,1,1,1,1,1,2,1,2,1,1,2,2,2,2,2,2,2,2,0,2,2,2,1,2,2,0,2,2,2,2,2,2,1,2,1,0,0,2,2,1,1,1,2,2,2,1,1,1,1,0,
            0,1,1,0,0,2,1,1,1,1,1,0,1};

        BigDecimal[] gamValues = getBigDecimal(intVals);

        double[] quartiles = quartiles(Arrays.stream(intVals).asDoubleStream().toArray());

        BigDecimal meanEvaluation = new BigDecimalWithContext(Arrays.stream(intVals).average().getAsDouble());
        BigDecimal q1Evaluation = new BigDecimalWithContext(quartiles[0]);
        BigDecimal q2Evaluation = new BigDecimalWithContext(quartiles[1]);
        BigDecimal q3Evaluation = new BigDecimalWithContext(quartiles[2]);

        BigDecimal meanGAMResult = GAMUtility.utilityFunction(meanEvaluation, gamValues, true);
        BigDecimal q1GAMResult = GAMUtility.utilityFunction(q1Evaluation, gamValues, true);
        BigDecimal q2GAMResult = GAMUtility.utilityFunction(q2Evaluation, gamValues, true);
        BigDecimal q3GAMResult = GAMUtility.utilityFunction(q3Evaluation, gamValues, true);


        int min = Arrays.stream(intVals).min().getAsInt();
        int max = Arrays.stream(intVals).max().getAsInt();

        BigDecimal[] linearInterpValues = getBigDecimal(new int[]{min, max});

        BigDecimal meanLinearInterpResult = defaultUtility.utilityFunction(meanEvaluation, linearInterpValues, true);
        BigDecimal q1LinearInterpResult = defaultUtility.utilityFunction(q1Evaluation, linearInterpValues, true);
        BigDecimal q2LinearInterpResult = defaultUtility.utilityFunction(q2Evaluation, linearInterpValues, true);
        BigDecimal q3LinearInterpResult = defaultUtility.utilityFunction(q3Evaluation, linearInterpValues, true);

        System.out.println("Data stats");
        System.out.println("\tMean: " + meanEvaluation);
        System.out.println("\tQ1: " + q1Evaluation);
        System.out.println("\tQ2 (Median): " + q2Evaluation);
        System.out.println("\tQ3: " + q3Evaluation);


        System.out.println("GAM results");
        System.out.println("\tMean: " + meanGAMResult);
        System.out.println("\tQ1: " + q1GAMResult);
        System.out.println("\tQ2 (Median): " + q2GAMResult);
        System.out.println("\tQ3: " + q3GAMResult);
        System.out.println("Linear Interp results");
        System.out.println("\tMean: " + meanLinearInterpResult);
        System.out.println("\tQ1: " + q1LinearInterpResult);
        System.out.println("\tQ2 (Median): " + q2LinearInterpResult);
        System.out.println("\tQ3: " + q3LinearInterpResult);
    }

    /**
     * nice sinusoidal curve, but it dips below 0... I wonder if we can constrain the fittings to be > 0
     */
    @Test
    public void compareCWE_415(){
        int[] intVals = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,1,0,0,5,5,0,4,8,4,3,0,8,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,4,0,0,1,0,0,1,0,0,0,0,0,0,0,0,1,0,13,0,0,7,0,0,0,1,0,11,0,0,0,11,0,0,11,0,1,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,13,0,0,0,0,0,0,0,0,0,10,3,0,0,0,0,0,0,0,0,3,16,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,9,13,13,13,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,12,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,
            0,19,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,2,3,0,0,0,0,0,1,0,0,0,0,1,
            0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,14,0,14,6,13,0,6,14,14,6,0,6,6,0,0,0,0,0,0,1,0,0,11,0,0,0,5,3,0,0,2,0,0,
            0,0,1,1,0,13,0,6,6,6,6,22,0,0,0,0,0,0,0,0,7,0,0,0,0,1,0,0,0,0,0,2,0,0,2,0,0,0,0,0,0,0,0,0,0,0,10,0,0,0,2,0,0,
            0,0,0,0,0,2,0,0,0,0,0,15,0,0,0,0,1,0,0,0,0,13,0,0,0,0,0,0,0,9,0,0,0,0,0,15,15,15,15,15,15,15,15,0,15,15,15,0,
            15,0,0,2,0,2,0,0,0,4,20,0,0,0,6,0,0,6,6,0,0,0,13,0,0,0,0,0,0,0,9,0,0,0,0,0,10,0,0,0,0,0,1,0,52,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,2,1,0,1,0,0,0,2,68,13,8,19,9,2,0,15,0,13,0,0,11,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,0,0,0,0,0,7,1,
            0,0,0,0,0,0,0,0,0,0,0,0,13,1,0,0,0,0,0,0,11,0,0,0,13,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,20,0,
            0,0,1,0,0,14,0,0,1,0,0,2,0,0,0,0,0,0,0,0,11,0,0,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1,0,0,0,0,8,4,0,4,4,0,13};

        BigDecimal[] gamValues = getBigDecimal(intVals);

        double[] quartiles = quartiles(Arrays.stream(intVals).asDoubleStream().toArray());

        BigDecimal meanEvaluation = new BigDecimalWithContext(Arrays.stream(intVals).average().getAsDouble());
        BigDecimal q1Evaluation = new BigDecimalWithContext(quartiles[0]);
        BigDecimal q2Evaluation = new BigDecimalWithContext(quartiles[1]);
        BigDecimal q3Evaluation = new BigDecimalWithContext(quartiles[2]);

        BigDecimal meanGAMResult = GAMUtility.utilityFunction(meanEvaluation, gamValues, true);
        BigDecimal q1GAMResult = GAMUtility.utilityFunction(q1Evaluation, gamValues, true);
        BigDecimal q2GAMResult = GAMUtility.utilityFunction(q2Evaluation, gamValues, true);
        BigDecimal q3GAMResult = GAMUtility.utilityFunction(q3Evaluation, gamValues, true);


        int min = Arrays.stream(intVals).min().getAsInt();
        int max = Arrays.stream(intVals).max().getAsInt();

        BigDecimal[] linearInterpValues = getBigDecimal(new int[]{min, max});

        BigDecimal meanLinearInterpResult = defaultUtility.utilityFunction(meanEvaluation, linearInterpValues, true);
        BigDecimal q1LinearInterpResult = defaultUtility.utilityFunction(q1Evaluation, linearInterpValues, true);
        BigDecimal q2LinearInterpResult = defaultUtility.utilityFunction(q2Evaluation, linearInterpValues, true);
        BigDecimal q3LinearInterpResult = defaultUtility.utilityFunction(q3Evaluation, linearInterpValues, true);

        System.out.println("Data stats");
        System.out.println("\tMean: " + meanEvaluation);
        System.out.println("\tQ1: " + q1Evaluation);
        System.out.println("\tQ2 (Median): " + q2Evaluation);
        System.out.println("\tQ3: " + q3Evaluation);


        System.out.println("GAM results");
        System.out.println("\tMean: " + meanGAMResult);
        System.out.println("\tQ1: " + q1GAMResult);
        System.out.println("\tQ2 (Median): " + q2GAMResult);
        System.out.println("\tQ3: " + q3GAMResult);
        System.out.println("Linear Interp results");
        System.out.println("\tMean: " + meanLinearInterpResult);
        System.out.println("\tQ1: " + q1LinearInterpResult);
        System.out.println("\tQ2 (Median): " + q2LinearInterpResult);
        System.out.println("\tQ3: " + q3LinearInterpResult);


    }


    /**
     * binary findings --
     */
    @Test
    public void compareCWE_835(){
        int[] intVals = new int[]{0,1,0,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,0,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1,1,1,0,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,1,1,0,0,1,1,1,1,1,1,1,1};

        BigDecimal[] gamValues = getBigDecimal(intVals);

        double[] quartiles = quartiles(Arrays.stream(intVals).asDoubleStream().toArray());

        BigDecimal meanEvaluation = new BigDecimalWithContext(Arrays.stream(intVals).average().getAsDouble());
        BigDecimal q1Evaluation = new BigDecimalWithContext(quartiles[0]);
        BigDecimal q2Evaluation = new BigDecimalWithContext(quartiles[1]);
        BigDecimal q3Evaluation = new BigDecimalWithContext(quartiles[2]);

        BigDecimal meanGAMResult = GAMUtility.utilityFunction(meanEvaluation, gamValues, true);
        BigDecimal q1GAMResult = GAMUtility.utilityFunction(q1Evaluation, gamValues, true);
        BigDecimal q2GAMResult = GAMUtility.utilityFunction(q2Evaluation, gamValues, true);
        BigDecimal q3GAMResult = GAMUtility.utilityFunction(q3Evaluation, gamValues, true);


        int min = Arrays.stream(intVals).min().getAsInt();
        int max = Arrays.stream(intVals).max().getAsInt();

        BigDecimal[] linearInterpValues = getBigDecimal(new int[]{min, max});

        BigDecimal meanLinearInterpResult = defaultUtility.utilityFunction(meanEvaluation, linearInterpValues, true);
        BigDecimal q1LinearInterpResult = defaultUtility.utilityFunction(q1Evaluation, linearInterpValues, true);
        BigDecimal q2LinearInterpResult = defaultUtility.utilityFunction(q2Evaluation, linearInterpValues, true);
        BigDecimal q3LinearInterpResult = defaultUtility.utilityFunction(q3Evaluation, linearInterpValues, true);

        System.out.println("Data stats");
        System.out.println("\tMean: " + meanEvaluation);
        System.out.println("\tQ1: " + q1Evaluation);
        System.out.println("\tQ2 (Median): " + q2Evaluation);
        System.out.println("\tQ3: " + q3Evaluation);


        System.out.println("GAM results");
        System.out.println("\tMean: " + meanGAMResult);
        System.out.println("\tQ1: " + q1GAMResult);
        System.out.println("\tQ2 (Median): " + q2GAMResult);
        System.out.println("\tQ3: " + q3GAMResult);
        System.out.println("Linear Interp results");
        System.out.println("\tMean: " + meanLinearInterpResult);
        System.out.println("\tQ1: " + q1LinearInterpResult);
        System.out.println("\tQ2 (Median): " + q2LinearInterpResult);
        System.out.println("\tQ3: " + q3LinearInterpResult);


    }

    /**
     * nice sinusoidal curve
     */
    @Test
    public void compareYaraMalwareDiagnostic(){
        int[] intVals = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,0,0,0,0,0,0,0,0,0,0,0,0,
            0,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,2,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,2,2,0,0,0,0,0,0,0,0};

        BigDecimal[] gamValues = getBigDecimal(intVals);

        double[] quartiles = quartiles(Arrays.stream(intVals).asDoubleStream().toArray());

        BigDecimal meanEvaluation = new BigDecimalWithContext(Arrays.stream(intVals).average().getAsDouble());
        BigDecimal q1Evaluation = new BigDecimalWithContext(quartiles[0]);
        BigDecimal q2Evaluation = new BigDecimalWithContext(quartiles[1]);
        BigDecimal q3Evaluation = new BigDecimalWithContext(quartiles[2]);

        BigDecimal meanGAMResult = GAMUtility.utilityFunction(meanEvaluation, gamValues, true);
        BigDecimal q1GAMResult = GAMUtility.utilityFunction(q1Evaluation, gamValues, true);
        BigDecimal q2GAMResult = GAMUtility.utilityFunction(q2Evaluation, gamValues, true);
        BigDecimal q3GAMResult = GAMUtility.utilityFunction(q3Evaluation, gamValues, true);


        int min = Arrays.stream(intVals).min().getAsInt();
        int max = Arrays.stream(intVals).max().getAsInt();

        BigDecimal[] linearInterpValues = getBigDecimal(new int[]{min, max});

        BigDecimal meanLinearInterpResult = defaultUtility.utilityFunction(meanEvaluation, linearInterpValues, true);
        BigDecimal q1LinearInterpResult = defaultUtility.utilityFunction(q1Evaluation, linearInterpValues, true);
        BigDecimal q2LinearInterpResult = defaultUtility.utilityFunction(q2Evaluation, linearInterpValues, true);
        BigDecimal q3LinearInterpResult = defaultUtility.utilityFunction(q3Evaluation, linearInterpValues, true);

        System.out.println("Data stats");
        System.out.println("\tMean: " + meanEvaluation);
        System.out.println("\tQ1: " + q1Evaluation);
        System.out.println("\tQ2 (Median): " + q2Evaluation);
        System.out.println("\tQ3: " + q3Evaluation);


        System.out.println("GAM results");
        System.out.println("\tMean: " + meanGAMResult);
        System.out.println("\tQ1: " + q1GAMResult);
        System.out.println("\tQ2 (Median): " + q2GAMResult);
        System.out.println("\tQ3: " + q3GAMResult);
        System.out.println("Linear Interp results");
        System.out.println("\tMean: " + meanLinearInterpResult);
        System.out.println("\tQ1: " + q1LinearInterpResult);
        System.out.println("\tQ2 (Median): " + q2LinearInterpResult);
        System.out.println("\tQ3: " + q3LinearInterpResult);


    }




    private BigDecimal[] getBigDecimal(int[] values){
        BigDecimal[] result = new BigDecimal[values.length];

        for (int i = 0; i < values.length; i++){
            result[i] = new BigDecimalWithContext(values[i]);
        }

        return result;
    }



    //https://stackoverflow.com/questions/42381759/finding-first-quartile-and-third-quartile-in-integer-array-using-java
    private double[] quartiles(double[] val) {
        double ans[] = new double[3];

        for (int quartileType = 1; quartileType < 4; quartileType++) {
            float length = val.length + 1;
            double quartile;
            float newArraySize = (length * ((float) (quartileType) * 25 / 100)) - 1;
            Arrays.sort(val);
            if (newArraySize % 1 == 0) {
                quartile = val[(int) (newArraySize)];
            } else {
                int newArraySize1 = (int) (newArraySize);
                quartile = (val[newArraySize1] + val[newArraySize1 + 1]) / 2;
            }
            ans[quartileType - 1] =  quartile;
        }
        return ans;
    }
}
