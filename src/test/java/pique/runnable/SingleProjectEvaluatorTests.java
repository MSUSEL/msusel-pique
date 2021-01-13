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
import pique.evaluation.Project;
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
 * Integration tests for single project evaluation processes
 */
public class SingleProjectEvaluatorTests {

    @Test
    public void testSingleProjectEvaluator_Simple() {

        // First, derive a quality model using the deriver
        Path qmFilePath = Paths.get("src/test/resources/quality_models/qualityModel_minimal_description.json");
        String projectRootFlag = ".txt";
        Path benchmarkRepo = Paths.get("src/test/resources/benchmark_repository");
        ITool mockedTool = new MockedIToolQmSimple();
        ITool locTool = new MockedLocTool();
        Set<ITool> tools = Stream.of(mockedTool, locTool).collect(Collectors.toSet());

        QualityModelImport qmImport = new QualityModelImport(qmFilePath);
        QualityModel qmDescription = qmImport.importQualityModel();
        QualityModel qualityModel = QualityModelDeriver.deriveModel(qmDescription, tools, benchmarkRepo,
                projectRootFlag);

        // Export as artifact
        Path outputDirectory = Paths.get("src/test/out");
        String qmDerivedName = "qualityModel_minimal_derived";
        QualityModelExport qmExport = new QualityModelExport(qualityModel);
        qmExport.exportToJson(qmDerivedName, outputDirectory);
        Path qmDerivedOutputFile = Paths.get(outputDirectory.toString(), qmDerivedName + ".json");

        // Now, run project evaluation using the derived model
        Path projectPath = Paths.get("src/test/resources/fake_project");
        Path evaluationResultsPath = Paths.get("src/test/out/evaluation_results_simple");
        SingleProjectEvaluator evaluator = new SingleProjectEvaluator();
        Path evaluationResults = evaluator.runEvaluator(projectPath, evaluationResultsPath, qmDerivedOutputFile, tools);

        // Get JVM representation of the evaluated project and make assertions
        Project evaluatedProject = evaluator.getEvaluatedProject();

        // Project
        Assert.assertEquals("fake_project", evaluatedProject.getName());
        Assert.assertEquals(100, evaluatedProject.getLinesOfCode());

        // QM Handles
        Tqi tqi = evaluatedProject.getQualityModel().getTqi();
        ModelNode qa01 = tqi.getChild("QualityAspect 01");
        ModelNode qa02 = tqi.getChild("QualityAspect 02");
        ModelNode pf01 = qa01.getChild("ProductFactor 01");
        ModelNode pf02 = qa02.getChild("ProductFactor 02");
        ModelNode measure01 = pf01.getChild("Measure 01");
        ModelNode measure02 = pf02.getChild("Measure 02");
        ModelNode diagnostic11 = measure01.getChild("TST0011");
        ModelNode diagnostic12 = measure01.getChild("TST0012");
        ModelNode diagnostic21 = measure02.getChild("TST0021");
        ModelNode diagnostic22 = measure02.getChild("TST0022");

        // Diagnostics
        Assert.assertEquals(1, diagnostic11.getValue(), 0.0001);
        Assert.assertEquals(4, diagnostic12.getValue(), 0.0001);
        Assert.assertEquals(1, diagnostic21.getValue(), 0.0001);
        Assert.assertEquals(0, diagnostic22.getValue(), 0.0001);

        // Measures
        Assert.assertEquals(0.25, measure01.getValue(), 0.0001);
        Assert.assertEquals(0.8333, measure02.getValue(), 0.0001);

        // Product Factors
        Assert.assertEquals(0.25, pf01.getValue(), 0.0001);
        Assert.assertEquals(0.83333, pf02.getValue(), 0.0001);

        // Quality Aspects
        Assert.assertEquals(0.5416, qa01.getValue(), 0.0001);
        Assert.assertEquals(0.5416, qa02.getValue(), 0.0001);

        // TQI
        Assert.assertEquals(0.5416, tqi.getValue(), 0.0001);

    }

    @Test
    public void testSingleProjectEvaluator_Full() {

        // First, derive a quality model using the deriver
        Path qmFilePath = Paths.get("src/test/resources/quality_models/qualityModel_full_description.json");
        String projectRootFlag = ".txt";
        Path benchmarkRepo = Paths.get("src/test/resources/benchmark_repository");
        ITool mockedTool = new MockedIToolQmFull();
        ITool locTool = new MockedLocTool();
        Set<ITool> tools = Stream.of(mockedTool, locTool).collect(Collectors.toSet());

        QualityModelImport qmImport = new QualityModelImport(qmFilePath);
        QualityModel qmDescription = qmImport.importQualityModel();
        QualityModel qualityModel = QualityModelDeriver.deriveModel(qmDescription, tools,benchmarkRepo,
                projectRootFlag);

        // Manually set measure threshold for this test
        qualityModel.getMeasure("Measure 01").setThresholds(new Double[]{0.0, 0.1});

        // Export as artifact
        Path outputDirectory = Paths.get("src/test/out");
        String qmDerivedName = "qualityModel_full_derived";
        QualityModelExport qmExport = new QualityModelExport(qualityModel);
        qmExport.exportToJson(qmDerivedName, outputDirectory);
        Path qmDerivedOutputFile = Paths.get(outputDirectory.toString(), qmDerivedName + ".json");

        // Now, run project evaluation using the derived model
        Path projectPath = Paths.get("src/test/resources/fake_project");
        Path evaluationResultsPath = Paths.get("src/test/out/evaluation_results_full");
        SingleProjectEvaluator evaluator = new SingleProjectEvaluator();
        Path evaluationResults = evaluator.runEvaluator(projectPath, evaluationResultsPath, qmDerivedOutputFile, tools);

        // Get JVM representation of the evaluated project and make assertions
        Project evaluatedProject = evaluator.getEvaluatedProject();

        // Project
        Assert.assertEquals("fake_project", evaluatedProject.getName());
        Assert.assertEquals(100, evaluatedProject.getLinesOfCode());

        // QM Handles
        QualityModel qm = evaluatedProject.getQualityModel();

        Tqi tqi = qm.getTqi();

        ModelNode qa11 = qm.getQualityAspect("QA11");
        ModelNode qa12 = qm.getQualityAspect("QA12");
        ModelNode qa21 = qm.getQualityAspect("QA21");
        ModelNode qa22 = qm.getQualityAspect("QA22");

        ModelNode pf11 = qm.getProductFactor("PF11");
        ModelNode pf21 = qm.getProductFactor("PF21");

        ModelNode measure01 = qm.getMeasure("Measure 01");

        ModelNode diagnostic11 = qm.getDiagnostic("TST0011");
        ModelNode diagnostic12 = qm.getDiagnostic("TST0012");

        // Diagnostics
        Assert.assertEquals(1, diagnostic11.getValue(), 0.0001);
        Assert.assertEquals(4, diagnostic12.getValue(), 0.0001);

        // Measures
        Assert.assertEquals(0.5, measure01.getValue(), 0.0001);

        // Product Factors
        Assert.assertEquals(0.25, pf21.getValue(), 0.0001);
        Assert.assertEquals(0.25, pf11.getValue(), 0.0001);

        // Quality Aspects
        Assert.assertEquals(0.25, qa21.getValue(), 0.0001);
        Assert.assertEquals(0.25, qa22.getValue(), 0.0001);
        Assert.assertEquals(0.25, qa11.getValue(), 0.0001);
        Assert.assertEquals(0.25, qa12.getValue(), 0.0001);

        // TQI
        Assert.assertEquals(0.25, tqi.getValue(), 0.0001);
    }
}
