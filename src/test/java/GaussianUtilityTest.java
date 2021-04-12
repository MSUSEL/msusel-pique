import org.junit.Test;
import pique.evaluation.DefaultUtility;
import pique.evaluation.GaussianUtilityFunction;

import static org.junit.Assert.assertEquals;

public class GaussianUtilityTest {


    private GaussianUtilityFunction gaussianUtility;

    public GaussianUtilityTest(){
        gaussianUtility = new GaussianUtilityFunction();
    }

    @Test
    public void testGaussianUtilityFunction(){
        Double[] simpleThresholds = new Double[]{new Double(3), new Double(7)};
        double positiveResult = gaussianUtility.utilityFunction(7, simpleThresholds, true);
        double positiveResult2 = gaussianUtility.utilityFunction(3, simpleThresholds, true);
        double positiveResult3 = gaussianUtility.utilityFunction(5, simpleThresholds, true);
        assertEquals(0.1209, positiveResult, 0.005);
        assertEquals(0.1209, positiveResult2, 0.005);
        assertEquals(0.1994, positiveResult3, 0.005);
    }
}
