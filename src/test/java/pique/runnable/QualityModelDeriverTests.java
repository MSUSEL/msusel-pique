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
package pique.runnable;

import org.junit.Assert;
import org.junit.Test;
import pique.analysis.ITool;
import pique.model.*;
import pique.utility.MockedIToolQmFull;
import pique.utility.MockedIToolQmSimple;
import pique.utility.MockedLocTool;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Integration tests of model derivation processes
 */
public class QualityModelDeriverTests {

    /**
     * Run the main model derivation method using mocked tool results using an "as simple as possible" quality model
     */
    @Test
    public void testDeriveModel_minimal() {

        Path qmFilePath = Paths.get("src/test/resources/quality_models/qualityModel_minimal_description.json");

        // Initialize objects
        String projectRootFlag = ".txt";
        Path benchmarkRepo = Paths.get("src/test/resources/benchmark_repository");
        ITool mockedTool = new MockedIToolQmSimple();
        ITool locTool = new MockedLocTool();
        Set<ITool> tools = Stream.of(mockedTool, locTool).collect(Collectors.toSet());

        QualityModelImport qmImport = new QualityModelImport(qmFilePath);
        QualityModel qmDescription = qmImport.importQualityModel();

        // The runner method under test: derive a quality model
        QualityModel qualityModel = QualityModelDeriver.deriveModel(qmDescription, tools, benchmarkRepo,
                projectRootFlag);

        //region Assert the derived model, now with weights and thresholds, has the expected form
        // TQI
        Tqi tqi = qualityModel.getTqi();
        Assert.assertEquals("Total Quality", tqi.getName());

        Assert.assertEquals(2, tqi.getChildren().size());
        Assert.assertEquals(2, tqi.getWeights().size());

        Assert.assertTrue(tqi.getChildren().containsKey("QualityAspect 01"));
        Assert.assertTrue(tqi.getChildren().containsKey("QualityAspect 02"));
        Assert.assertTrue(tqi.getWeights().containsKey("QualityAspect 01"));
        Assert.assertTrue(tqi.getWeights().containsKey("QualityAspect 02"));

        Assert.assertEquals(0.5, tqi.getWeight("QualityAspect 01"), 0.0);
        Assert.assertEquals(0.5, tqi.getWeight("QualityAspect 02"), 0.0);

        // Quality Aspects
        ModelNode qa01 = qualityModel.getQualityAspect("QualityAspect 01");
        Assert.assertEquals("QualityAspect 01", qa01.getName());

        Assert.assertEquals(2, qa01.getChildren().size());
        Assert.assertEquals(2, qa01.getWeights().size());

        Assert.assertTrue(qa01.getChildren().containsKey("ProductFactor 01"));
        Assert.assertTrue(qa01.getChildren().containsKey("ProductFactor 02"));
        Assert.assertTrue(qa01.getWeights().containsKey("ProductFactor 01"));
        Assert.assertTrue(qa01.getWeights().containsKey("ProductFactor 02"));

        Assert.assertEquals(0.5, qa01.getWeight("ProductFactor 01"), 0.0);
        Assert.assertEquals(0.5, qa01.getWeight("ProductFactor 02"), 0.0);

        ModelNode qa02 = qualityModel.getQualityAspect("QualityAspect 02");
        Assert.assertEquals("QualityAspect 02", qa02.getName());

        Assert.assertEquals(2, qa02.getChildren().size());
        Assert.assertEquals(2, qa02.getWeights().size());

        Assert.assertTrue(qa02.getChildren().containsKey("ProductFactor 01"));
        Assert.assertTrue(qa02.getChildren().containsKey("ProductFactor 02"));
        Assert.assertTrue(qa02.getWeights().containsKey("ProductFactor 01"));
        Assert.assertTrue(qa02.getWeights().containsKey("ProductFactor 02"));

        Assert.assertEquals(0.5, qa02.getWeight("ProductFactor 01"), 0.0);
        Assert.assertEquals(0.5, qa02.getWeight("ProductFactor 02"), 0.0);

        // Product Factors
        ModelNode pf01 = qa01.getChild("ProductFactor 01");
        Assert.assertSame(pf01, qa02.getChild("ProductFactor 01"));
        Assert.assertEquals("ProductFactor 01", pf01.getName());
        Assert.assertEquals(1, pf01.getChildren().size());

        ModelNode pf02 = qa01.getChild("ProductFactor 02");
        Assert.assertSame(pf02, qa02.getChild("ProductFactor 02"));
        Assert.assertEquals("ProductFactor 02", pf02.getName());
        Assert.assertEquals(1, pf02.getChildren().size());

        // Measures
        Measure measure01 = (Measure)pf01.getChild("Measure 01");
        Assert.assertEquals("Measure 01", measure01.getName());

        Assert.assertFalse(measure01.isPositive());

        Assert.assertEquals(2, measure01.getThresholds().length);
        Assert.assertEquals(0.02, measure01.getThresholds()[0], 0.0);
        Assert.assertEquals(0.06, measure01.getThresholds()[1], 0.0);

        Assert.assertEquals(2, measure01.getChildren().size());
        Assert.assertTrue(measure01.getChildren().containsKey("TST0011"));
        Assert.assertTrue(measure01.getChildren().containsKey("TST0012"));

        Measure measure02 = (Measure)pf02.getChild("Measure 02");
        Assert.assertEquals("Measure 02", measure02.getName());

        Assert.assertFalse(measure02.isPositive());

        Assert.assertEquals(2, measure02.getThresholds().length);
        Assert.assertEquals(0.00, measure02.getThresholds()[0], 0.0);
        Assert.assertEquals(0.06, measure02.getThresholds()[1], 0.0);

        Assert.assertEquals(2, measure02.getChildren().size());
        Assert.assertTrue(measure02.getChildren().containsKey("TST0021"));
        Assert.assertTrue(measure02.getChildren().containsKey("TST0022"));

        // Diagnostics
        Diagnostic diagnostic11 = (Diagnostic)measure01.getChild("TST0011");
        Assert.assertEquals("TST0011", diagnostic11.getName());
        Assert.assertEquals("Test tool", diagnostic11.getToolName());

        Diagnostic diagnostic12 = (Diagnostic)measure01.getChild("TST0012");
        Assert.assertEquals("TST0012", diagnostic12.getName());
        Assert.assertEquals("Test tool", diagnostic12.getToolName());

        Diagnostic diagnostic21 = (Diagnostic)measure02.getChild("TST0021");
        Assert.assertEquals("TST0021", diagnostic21.getName());
        Assert.assertEquals("Test tool", diagnostic21.getToolName());

        Diagnostic diagnostic22 = (Diagnostic)measure02.getChild("TST0022");
        Assert.assertEquals("TST0022", diagnostic22.getName());
        Assert.assertEquals("Test tool", diagnostic22.getToolName());

        //endregion
    }

    /**
     * Run the main model derivation method using mocked tool results using a "fully designed" quality model meant to
     * test many edge cases of model usage.
     */
    @Test
    public void testDeriveModel_full() {
        Path qmFilePath = Paths.get("src/test/resources/quality_models/qualityModel_full_description.json");

        // Initialize objects
        String projectRootFlag = ".txt";
        Path benchmarkRepo = Paths.get("src/test/resources/benchmark_repository");
        ITool mockedTool = new MockedIToolQmFull();
        ITool locTool = new MockedLocTool();
        Set<ITool> tools = Stream.of(mockedTool, locTool).collect(Collectors.toSet());

        QualityModelImport qmImport = new QualityModelImport(qmFilePath);
        QualityModel qmDescription = qmImport.importQualityModel();

        // The runner method under test: derive a quality model
        QualityModel qualityModel = QualityModelDeriver.deriveModel(qmDescription, tools, benchmarkRepo,
                projectRootFlag);

        // Manually set measure threshold for this test
        qualityModel.getMeasure("Measure 01").setThresholds(new Double[]{0.0, 0.1});

        //region Assert the derived model, now with weights and thresholds, has the expected form
        // TQI
        Tqi tqi = qualityModel.getTqi();
        Assert.assertEquals("Total Quality", tqi.getName());

        Assert.assertEquals(2, tqi.getChildren().size());
        Assert.assertEquals(2, tqi.getWeights().size());

        Assert.assertTrue(tqi.getChildren().containsKey("QA11"));
        Assert.assertTrue(tqi.getChildren().containsKey("QA12"));
        Assert.assertTrue(tqi.getWeights().containsKey("QA11"));
        Assert.assertTrue(tqi.getWeights().containsKey("QA12"));

        Assert.assertEquals(0.5, tqi.getWeight("QA11"), 0.0);
        Assert.assertEquals(0.5, tqi.getWeight("QA12"), 0.0);

        // Quality Aspects
        ModelNode qa11 = qualityModel.getQualityAspect("QA11");
        Assert.assertEquals("QA11", qa11.getName());

        Assert.assertEquals(2, qa11.getChildren().size());
        Assert.assertEquals(2, qa11.getWeights().size());

        Assert.assertTrue(qa11.getChildren().containsKey("QA21"));
        Assert.assertTrue(qa11.getChildren().containsKey("QA22"));
        Assert.assertTrue(qa11.getWeights().containsKey("QA21"));
        Assert.assertTrue(qa11.getWeights().containsKey("QA22"));

        Assert.assertEquals(0.5, qa11.getWeight("QA21"), 0.0);
        Assert.assertEquals(0.5, qa11.getWeight("QA22"), 0.0);

        ModelNode qa12 = qualityModel.getQualityAspect("QA12");
        Assert.assertEquals("QA12", qa12.getName());

        Assert.assertEquals(1, qa12.getChildren().size());
        Assert.assertEquals(1, qa12.getWeights().size());

        Assert.assertTrue(qa12.getChildren().containsKey("QA22"));
        Assert.assertTrue(qa12.getWeights().containsKey("QA22"));

        Assert.assertEquals(1.0, qa12.getWeight("QA22"), 0.0);

        ModelNode qa21 = qualityModel.getQualityAspect("QA21");
        Assert.assertEquals("QA21", qa21.getName());

        Assert.assertEquals(1, qa21.getChildren().size());
        Assert.assertEquals(1, qa21.getWeights().size());

        Assert.assertTrue(qa21.getChildren().containsKey("PF11"));
        Assert.assertTrue(qa21.getWeights().containsKey("PF11"));

        Assert.assertEquals(1.0, qa21.getWeight("PF11"), 0.0);

        ModelNode qa22 = qualityModel.getQualityAspect("QA22");
        Assert.assertEquals("QA22", qa22.getName());

        Assert.assertEquals(1, qa22.getChildren().size());
        Assert.assertEquals(1, qa22.getWeights().size());

        Assert.assertTrue(qa22.getChildren().containsKey("PF11"));
        Assert.assertTrue(qa22.getWeights().containsKey("PF11"));

        Assert.assertEquals(1.0, qa22.getWeight("PF11"), 0.0);

        // Product Factors
        ModelNode pf11 = qualityModel.getProductFactor("PF11");
        Assert.assertEquals("PF11", pf11.getName());
        Assert.assertEquals(1, pf11.getChildren().size());
        Assert.assertTrue(pf11.getChildren().containsKey("PF21"));

        ModelNode pf21 = qualityModel.getProductFactor("PF21");
        Assert.assertEquals("PF21", pf21.getName());
        Assert.assertEquals(1, pf21.getChildren().size());
        Assert.assertTrue(pf21.getChildren().containsKey("Measure 01"));


        // Measures
        Measure measure01 = (Measure)qualityModel.getMeasure("Measure 01");
        Assert.assertEquals("Measure 01", measure01.getName());

        Assert.assertFalse(measure01.isPositive());

        Assert.assertEquals(2, measure01.getThresholds().length);
        Assert.assertEquals(0.00, measure01.getThresholds()[0], 0.0);
        Assert.assertEquals(0.10, measure01.getThresholds()[1], 0.0);

        Assert.assertEquals(2, measure01.getChildren().size());
        Assert.assertTrue(measure01.getChildren().containsKey("TST0011"));
        Assert.assertTrue(measure01.getChildren().containsKey("TST0012"));


        // Diagnostics
        Diagnostic diagnostic11 = (Diagnostic)measure01.getChild("TST0011");
        Assert.assertEquals("TST0011", diagnostic11.getName());
        Assert.assertEquals("Test tool", diagnostic11.getToolName());

        Diagnostic diagnostic12 = (Diagnostic)measure01.getChild("TST0012");
        Assert.assertEquals("TST0012", diagnostic12.getName());
        Assert.assertEquals("Test tool", diagnostic12.getToolName());
    }
}
