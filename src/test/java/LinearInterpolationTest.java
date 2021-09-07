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
import org.junit.Before;
import org.junit.Test;
import pique.evaluation.DefaultUtility;
import pique.utility.BigDecimalWithContext;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

public class LinearInterpolationTest {

    private DefaultUtility linearInterpolationUtility;

    public LinearInterpolationTest(){
        linearInterpolationUtility = new DefaultUtility();
    }

    @Test
    public void testPositive(){
        BigDecimal[] simpleThresholds = new BigDecimalWithContext[]{new BigDecimalWithContext("5"), new BigDecimalWithContext("7")};
        BigDecimal positiveResult = linearInterpolationUtility.utilityFunction(new BigDecimalWithContext("6"), simpleThresholds, true);
        assertEquals(0.5, positiveResult.doubleValue(), 0.005);

        BigDecimal negativeResult = linearInterpolationUtility.utilityFunction(new BigDecimalWithContext("6"), simpleThresholds, false);
        assertEquals(0.5, negativeResult.doubleValue(), 0.005);
 
        BigDecimal floatPositiveResult = linearInterpolationUtility.utilityFunction(new BigDecimalWithContext("6.3"), simpleThresholds, true);
        assertEquals(0.650, floatPositiveResult.doubleValue(), 0.005);

        BigDecimal floatNegativeResult = linearInterpolationUtility.utilityFunction(new BigDecimalWithContext("6.3"), simpleThresholds, false);
        assertEquals(0.350, floatNegativeResult.doubleValue(), 0.005);

    }

}
