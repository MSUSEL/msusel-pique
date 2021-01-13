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

import org.junit.Assert;
import org.junit.Test;
import pique.model.*;
import pique.utility.Builder;

/**
 * Test the default evaluation at each level of a basic quality model
 * Note: These tests make assumption of state based off of the constructions done by the {@link Builder} class.
 */
public class DefaultEvaluatorTests {

    @Test
    public void testDefaultFindingEvaluator() {
        Finding node = Builder.buildFinding("file/path", 22, 3);
        double value = node.getValue();

        Assert.assertEquals(3, value, 0.0);
    }

    @Test
    public void testDefaultDiagnosticEvaluator() {
        Diagnostic node = Builder.buildDiagnostic("Diagnostic 01");
        double value = node.getValue();

        Assert.assertEquals(5, value, 0.0);
    }

    @Test
    public void testDefaultMeasureEvaluator() {
        Measure node = Builder.buildMeasure("Measure 01");
        double value = node.getValue();

        Assert.assertEquals(0.9, value, 0.0);
    }

    @Test
    public void testDefaultProductFactorEvaluator() {
        ProductFactor node = Builder.buildProductFactor("Product Factor 01");
        double value = node.getValue();

        Assert.assertEquals(0.9, value, 0.0);
    }

    @Test
    public void testDefaultQualityAspectEvaluator() {
        QualityAspect node = Builder.buildQualityAspect("Quality Aspect 01");
        double value = node.getValue();

        Assert.assertEquals(0.9, value, 0.0);
    }

    @Test
    public void testDefaultTqiEvaluator() {
        Tqi node = Builder.buildTqi("TQI 01");
        double value = node.getValue();

        Assert.assertEquals(0.9, value, 0.0);
    }
}
