package evaluatorTests;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.BeforeClass;
import org.junit.Test;
import pique.evaluation.ProbabilityDensityFunctionUtilityFunction;
import pique.utility.BigDecimalWithContext;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProbabilityDensityFunctionUtilityFunctionTests {

    private static HashMap<String, BigDecimal[]> columns;

    private double delta = 0.0005;

    public ProbabilityDensityFunctionUtilityFunctionTests(){

    }

    @BeforeClass
    public static void loadTestCSV(){
        try{
            Reader in = new FileReader("src/test/resources/utilityFunctionData/pique-bin-full.csv");

            columns = new HashMap<>();
            List<CSVRecord> records = IterableUtils.toList(CSVFormat.DEFAULT.withHeader().parse(in));
            int dataPoints = IterableUtils.size(records);
            int bigDecimalArrayIterator = 0;
            //somewhat awkward CSV transpose
            for (CSVRecord record : records) {
                for (String key : record.toMap().keySet()) {
                    //skip Project key, because we only care about the values of the data, not the project
                    if (!key.equalsIgnoreCase("Project")) {
                        //first time through, initialize array of BigDecimals with funky conversions. Sometimes I hate java...
                        if (!columns.containsKey(key)) {
                            columns.put(key, new BigDecimalWithContext[dataPoints]);
                        }
                        //add value, converting to BigDecimal in the process
                        columns.get(key)[bigDecimalArrayIterator] = new BigDecimalWithContext(record.toMap().get(key));
                    }
                }
                bigDecimalArrayIterator++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testNonTrivialCase(){
        //interesting data
        BigDecimal[] data = columns.get("CVE-Unknown-Other Diagnostic");
        assertEquals(650, data.length);
        ProbabilityDensityFunctionUtilityFunction pdf = new ProbabilityDensityFunctionUtilityFunction();
        BigDecimal outputScore = pdf.utilityFunction(new BigDecimalWithContext(40), data, false);

        assertEquals(new BigDecimalWithContext(0.6677223507742107).doubleValue(), outputScore.doubleValue(), delta);
    }

    @Test
    public void testAllZeroesCase(){
        //all zeroes
        BigDecimal[] data = columns.get("CVE-CWE-915 Diagnostic");
        assertEquals(650, data.length);
        BigDecimal lowScore = new BigDecimalWithContext(0.001);
        BigDecimal highScore = new BigDecimalWithContext(0.999);
        ProbabilityDensityFunctionUtilityFunction pdf = new ProbabilityDensityFunctionUtilityFunction();
        BigDecimal outputScore = pdf.utilityFunction(new BigDecimal(3), data, false);
        //ideally get a difference of 0, but precision can be funky
        assertEquals(0.0, outputScore.subtract(lowScore).abs().doubleValue(), delta);

        BigDecimal outputScore2 = pdf.utilityFunction(new BigDecimal(3), data, true);
        assertEquals(0.0, outputScore2.subtract(highScore).abs().doubleValue(), delta);

        BigDecimal outputScore3 = pdf.utilityFunction(new BigDecimal(-1), data, false);
        assertEquals(0.0, outputScore3.subtract(highScore).abs().doubleValue(), delta);

        BigDecimal outputScore4 = pdf.utilityFunction(new BigDecimal(-1), data, true);
        assertEquals(0.0, outputScore4.subtract(lowScore).abs().doubleValue(), delta);
    }

    @Test
    public void testRightSkewedCase(){
        //most zeroes, but right skewed
        BigDecimal[] data = columns.get("CWE-190 Weakness Diagnostic");
        assertEquals(650, data.length);
        ProbabilityDensityFunctionUtilityFunction pdf = new ProbabilityDensityFunctionUtilityFunction();
        BigDecimal outputScore = pdf.utilityFunction(new BigDecimal(5), data, false);

        assertEquals(new BigDecimal(0.16302670275858278), outputScore);
    }

}
