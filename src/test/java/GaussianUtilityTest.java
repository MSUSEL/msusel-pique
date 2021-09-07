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
import org.junit.Test;
import pique.evaluation.DefaultUtility;
import pique.evaluation.GaussianUtilityFunction;
import pique.utility.BigDecimalWithContext;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

public class GaussianUtilityTest {


    private GaussianUtilityFunction gaussianUtility;

    public GaussianUtilityTest(){
        gaussianUtility = new GaussianUtilityFunction();
    }

    @Test
    public void testGaussianUtilityFunction(){
        BigDecimal[] simpleThresholds = new BigDecimalWithContext[]{new BigDecimalWithContext(3), new BigDecimalWithContext(7)};
        BigDecimal positiveResult = gaussianUtility.utilityFunction(new BigDecimalWithContext("7"), simpleThresholds, true);
        BigDecimal positiveResult2 = gaussianUtility.utilityFunction(new BigDecimalWithContext("3"), simpleThresholds, true);
        BigDecimal positiveResult3 = gaussianUtility.utilityFunction(new BigDecimalWithContext("5"), simpleThresholds, true);
        assertEquals(0.1209, positiveResult.doubleValue(), 0.005);
        assertEquals(0.1209, positiveResult2.doubleValue(), 0.005);
        assertEquals(0.1994, positiveResult3.doubleValue(), 0.005);
    }
}
