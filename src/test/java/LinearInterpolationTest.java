import org.junit.Before;
import org.junit.Test;
import pique.evaluation.DefaultUtility;

import static org.junit.Assert.assertEquals;

public class LinearInterpolationTest {

    private DefaultUtility linearInterpolationUtility;

    public LinearInterpolationTest(){
        linearInterpolationUtility = new DefaultUtility();
    }

    @Test
    public void testPositive(){
        Double[] simpleThresholds = new Double[]{new Double(5), new Double(7)};
        double positiveResult = linearInterpolationUtility.utilityFunction(6, simpleThresholds, true);
        assertEquals(0.5, positiveResult, 0.005);

        double negativeResult = linearInterpolationUtility.utilityFunction(6, simpleThresholds, false);
        assertEquals(0.5, negativeResult, 0.005);

        double floatPositiveResult = linearInterpolationUtility.utilityFunction(6.3, simpleThresholds, true);
        assertEquals(0.650, floatPositiveResult, 0.005);

        double floatNegativeResult = linearInterpolationUtility.utilityFunction(6.3, simpleThresholds, false);
        assertEquals(0.350, floatNegativeResult, 0.005);

    }

}
