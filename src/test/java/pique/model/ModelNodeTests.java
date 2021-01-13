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
package pique.model;

import org.junit.Assert;
import org.junit.Test;
import pique.evaluation.Project;
import pique.utility.Builder;

/**
 * Test abstract {@link ModelNode} class using a concrete representation of it.
 */
public class ModelNodeTests {

    //region Construction

    /**
     * Verify a simple Finding object instances correctly using a Builder
     */
    @Test
    public void testFindingConstruction() {
        Finding node = Builder.buildFinding("file/path", 1, 1);
        Assert.assertSame(node.getClass(), Finding.class);
    }

    /**
     * Verify a simple Diagnostic object instances correctly using a Builder
     */
    @Test
    public void testDiagnosticConstruction() {
        Diagnostic node = Builder.buildDiagnostic("Diagnostic ID");
        Assert.assertSame(node.getClass(), Diagnostic.class);
    }

    /**
     * Verify a simple Measure object instances correctly using a Builder
     */
    @Test
    public void testMeasureConstruction() {
        Measure node = Builder.buildMeasure("Measure Name");
        Assert.assertSame(node.getClass(), Measure.class);
    }

    /**
     * Verify a simple Product Factor object instances correctly using a Builder
     */
    @Test
    public void testProductFactorConstruction() {
        ProductFactor node = Builder.buildProductFactor("Product Factor Name");
        Assert.assertSame(node.getClass(), ProductFactor.class);
    }

    /**
     * Verify a simple Quality Aspect object instances correctly using a Builder
     */
    @Test
    public void testQualityAspectConstruction() {
        QualityAspect node = Builder.buildQualityAspect("Quality Aspect Name");
        Assert.assertSame(node.getClass(), QualityAspect.class);
    }

    /**
     * Verify a simple TQI object instances correctly using a Builder
     */
    @Test
    public void testTQIConstruction() {
        Tqi node = Builder.buildTqi("TQI Name");
        Assert.assertSame(node.getClass(), Tqi.class);
    }

    /**
     * Verify a simple Quality Model object instances correctly using a Builder
     */
    @Test
    public void testQualityModelConstruction() {
        QualityModel node = Builder.buildQualityModel("QM Name");
        Assert.assertSame(node.getClass(), QualityModel.class);
    }

    /**
     * Verify a simple Project object instances correctly using a Builder
     */
    @Test
    public void testProjectConstruction() {
        Project node = Builder.buildProject("Project Name");
        Assert.assertSame(node.getClass(), Project.class);
    }

    //endregion
}
