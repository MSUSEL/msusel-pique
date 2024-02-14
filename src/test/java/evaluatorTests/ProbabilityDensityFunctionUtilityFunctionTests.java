package evaluatorTests;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProbabilityDensityFunctionUtilityFunctionTests {

    private static HashMap<String, BigDecimal[]> columns;

    public ProbabilityDensityFunctionUtilityFunctionTests(){

    }

    @BeforeClass
    public static void loadTestCSV(){
        try{
            Reader in = new FileReader("src/test/resources/utilityFunctionData/pique-bin-full.csv");

            columns = new HashMap<>();
            List<CSVRecord> records = IterableUtils.toList(CSVFormat.DEFAULT.withHeader().parse(in));
            int dataPoints = IterableUtils.size(records);
            //somewhat awkward CSV transpose
            for (CSVRecord record : records) {
                int bigDecimalArrayIterator = 0;
                for (String key : record.toMap().keySet()) {
                    //skip Project key, because we only care about the values of the data, not the project
                    if (!key.equalsIgnoreCase("Project")) {
                        //first time through, initialize array of BigDecimals with funky conversions. Sometimes I hate java...
                        if (!columns.containsKey(key)) {
                            columns.put(key, new BigDecimal[dataPoints]);
                        }
                        //add value, converting to BigDecimal in the process
                        columns.get(key)[bigDecimalArrayIterator] = new BigDecimal(record.toMap().get(key));
                    }
                }
                bigDecimalArrayIterator++;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testNonTrivialCases(){
        BigDecimal[] data = columns.get("CVE-Unknown-Other Diagnostic");
        System.out.println(columns);
        assertEquals(650, data.length);
    }

}
