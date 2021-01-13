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
import pique.calibration.NaiveBenchmarker;
import pique.calibration.NaiveWeighter;
import pique.evaluation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class QualityModelImportTests {

    /**
     * Integration test due to dependency on {@link pique.model.QualityModel} and external quality model .json file.
     */
    @Test
    public void testQualityModelImport_description() {
        Path qmFilePath = Paths.get("src/test/resources/quality_models/qualityModel_minimal_description.json");
        QualityModelImport qmImport = new QualityModelImport(qmFilePath);
        QualityModel qm = qmImport.importQualityModel();

        // Assert basic structure
        // Top Level
        Assert.assertEquals("Test QM", qm.getName());
        Assert.assertEquals(NaiveBenchmarker.class, qm.getBenchmarker().getClass());
        Assert.assertEquals(NaiveWeighter.class, qm.getWeighter().getClass());

        // TQI
        Tqi tqi = qm.getTqi();
        Assert.assertEquals(0.0, tqi.getValue(), 0.0);
        Assert.assertEquals("Total Quality", tqi.getName());
        Assert.assertEquals(DefaultFactorEvaluator.class, tqi.getEvaluatorObject().getClass());
        Assert.assertEquals(DefaultNormalizer.class, tqi.getNormalizerObject().getClass());
        Assert.assertEquals(DefaultUtility.class, tqi.getUtilityFunctionObject().getClass());
        Assert.assertEquals(2, tqi.getNumChildren());
        Assert.assertTrue(tqi.getChildren().containsKey("QualityAspect 01"));
        Assert.assertTrue(tqi.getChildren().containsKey("QualityAspect 02"));
        Assert.assertEquals(0, tqi.getWeights().size(), 0.0);

        // Quality Aspects
        QualityAspect qa01 = (QualityAspect)qm.getTqi().getChild("QualityAspect 01");
        Assert.assertEquals(0.0, qa01.getValue(), 0.0);
        Assert.assertEquals("QualityAspect 01", qa01.getName());
        Assert.assertEquals(DefaultFactorEvaluator.class, qa01.getEvaluatorObject().getClass());
        Assert.assertEquals(DefaultNormalizer.class, qa01.getNormalizerObject().getClass());
        Assert.assertEquals(DefaultUtility.class, qa01.getUtilityFunctionObject().getClass());
        Assert.assertEquals(2, qa01.getNumChildren());
        Assert.assertTrue(qa01.getChildren().containsKey("ProductFactor 01"));
        Assert.assertTrue(qa01.getChildren().containsKey("ProductFactor 02"));
        Assert.assertEquals(0, qa01.getWeights().size(), 0.0);

        QualityAspect qa02 = qm.getQualityAspect("QualityAspect 01");
        Assert.assertEquals(0.0, qa02.getValue(), 0.0);
        Assert.assertEquals("QualityAspect 01", qa02.getName());
        Assert.assertEquals(DefaultFactorEvaluator.class, qa02.getEvaluatorObject().getClass());
        Assert.assertEquals(DefaultNormalizer.class, qa02.getNormalizerObject().getClass());
        Assert.assertEquals(DefaultUtility.class, qa02.getUtilityFunctionObject().getClass());
        Assert.assertEquals(2, qa02.getNumChildren());
        Assert.assertTrue(qa02.getChildren().containsKey("ProductFactor 01"));
        Assert.assertTrue(qa02.getChildren().containsKey("ProductFactor 02"));
        Assert.assertEquals(0, qa02.getWeights().size(), 0.0);

        // Product factors
        ProductFactor pf01 = qm.getProductFactor("ProductFactor 01");
        Assert.assertEquals(0.0, pf01.getValue(), 0.0);
        Assert.assertEquals("ProductFactor 01", pf01.getName());
        Assert.assertEquals(DefaultProductFactorEvaluator.class, pf01.getEvaluatorObject().getClass());
        Assert.assertEquals(DefaultNormalizer.class, pf01.getNormalizerObject().getClass());
        Assert.assertEquals(DefaultUtility.class, pf01.getUtilityFunctionObject().getClass());
        Assert.assertEquals(1, pf01.getNumChildren());
        Assert.assertTrue(pf01.getChildren().containsKey("Measure 01"));
        Assert.assertEquals(0, pf01.getWeights().size(), 0.0);

        ProductFactor pf02 = qm.getProductFactor("ProductFactor 02");
        Assert.assertEquals(0.0, pf02.getValue(), 0.0);
        Assert.assertEquals("ProductFactor 02", pf02.getName());
        Assert.assertEquals(DefaultProductFactorEvaluator.class, pf02.getEvaluatorObject().getClass());
        Assert.assertEquals(DefaultNormalizer.class, pf02.getNormalizerObject().getClass());
        Assert.assertEquals(DefaultUtility.class, pf02.getUtilityFunctionObject().getClass());
        Assert.assertEquals(1, pf02.getNumChildren());
        Assert.assertTrue(pf02.getChildren().containsKey("Measure 02"));
        Assert.assertEquals(0, pf02.getWeights().size(), 0.0);

        // Measures
        Measure m01 = (Measure)qm.getMeasure("Measure 01");
        Assert.assertEquals(0.0, m01.getValue(), 0.0);
        Assert.assertEquals("Measure 01", m01.getName());
        Assert.assertFalse(m01.isPositive());
        Assert.assertEquals(DefaultMeasureEvaluator.class, m01.getEvaluatorObject().getClass());
        Assert.assertEquals(DefaultNormalizer.class, m01.getNormalizerObject().getClass());
        Assert.assertEquals(DefaultUtility.class, m01.getUtilityFunctionObject().getClass());
        Assert.assertEquals(2, m01.getNumChildren());
        Assert.assertTrue(m01.getChildren().containsKey("TST0011"));
        Assert.assertTrue(m01.getChildren().containsKey("TST0012"));
        Assert.assertEquals(0, m01.getWeights().size(), 0.0);

        Measure m02 = (Measure)qm.getMeasure("Measure 02");
        Assert.assertEquals(0.0, m02.getValue(), 0.0);
        Assert.assertEquals("Measure 02", m02.getName());
        Assert.assertFalse(m02.isPositive());
        Assert.assertEquals(DefaultMeasureEvaluator.class, m02.getEvaluatorObject().getClass());
        Assert.assertEquals(DefaultNormalizer.class, m02.getNormalizerObject().getClass());
        Assert.assertEquals(DefaultUtility.class, m02.getUtilityFunctionObject().getClass());
        Assert.assertEquals(2, m02.getNumChildren());
        Assert.assertTrue(m02.getChildren().containsKey("TST0021"));
        Assert.assertTrue(m02.getChildren().containsKey("TST0022"));
        Assert.assertEquals(0, m02.getWeights().size(), 0.0);

        // Diagnostics
        Diagnostic tst0011 = (Diagnostic)m01.getChild("TST0011");
        Assert.assertEquals(0.0, tst0011.getValue(), 0.0);
        Assert.assertEquals("TST0011", tst0011.getName());
        Assert.assertEquals(DefaultDiagnosticEvaluator.class, tst0011.getEvaluatorObject().getClass());
        Assert.assertEquals(DefaultNormalizer.class, tst0011.getNormalizerObject().getClass());
        Assert.assertEquals(DefaultUtility.class, tst0011.getUtilityFunctionObject().getClass());
        Assert.assertEquals(0, tst0011.getNumChildren());
        Assert.assertEquals(0, tst0011.getWeights().size(), 0.0);

        Diagnostic tst0012 = (Diagnostic)m01.getChild("TST0012");
        Assert.assertEquals(0.0, tst0012.getValue(), 0.0);
        Assert.assertEquals("TST0012", tst0012.getName());
        Assert.assertEquals(DefaultDiagnosticEvaluator.class, tst0012.getEvaluatorObject().getClass());
        Assert.assertEquals(DefaultNormalizer.class, tst0012.getNormalizerObject().getClass());
        Assert.assertEquals(DefaultUtility.class, tst0012.getUtilityFunctionObject().getClass());
        Assert.assertEquals(0, tst0012.getNumChildren());
        Assert.assertEquals(0, tst0012.getWeights().size(), 0.0);

        Diagnostic tst0021 = (Diagnostic)m02.getChild("TST0021");
        Assert.assertEquals(0.0, tst0021.getValue(), 0.0);
        Assert.assertEquals("TST0021", tst0021.getName());
        Assert.assertEquals(DefaultDiagnosticEvaluator.class, tst0021.getEvaluatorObject().getClass());
        Assert.assertEquals(DefaultNormalizer.class, tst0021.getNormalizerObject().getClass());
        Assert.assertEquals(DefaultUtility.class, tst0021.getUtilityFunctionObject().getClass());
        Assert.assertEquals(0, tst0021.getNumChildren());
        Assert.assertEquals(0, tst0021.getWeights().size(), 0.0);

        Diagnostic tst0022 = (Diagnostic)m02.getChild("TST0022");
        Assert.assertEquals(0.0, tst0022.getValue(), 0.0);
        Assert.assertEquals("TST0022", tst0022.getName());
        Assert.assertEquals(DefaultDiagnosticEvaluator.class, tst0022.getEvaluatorObject().getClass());
        Assert.assertEquals(DefaultNormalizer.class, tst0022.getNormalizerObject().getClass());
        Assert.assertEquals(DefaultUtility.class, tst0022.getUtilityFunctionObject().getClass());
        Assert.assertEquals(0, tst0022.getNumChildren());
        Assert.assertEquals(0, tst0022.getWeights().size(), 0.0);
    }
}
