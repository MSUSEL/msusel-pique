package outputTests;

import org.junit.BeforeClass;
import org.junit.Test;
import pique.evaluation.DefaultDiagnosticEvaluator;
import pique.evaluation.IEvaluator;
import pique.evaluation.ProbabilityDensityFunctionUtilityFunction;
import pique.evaluation.UtilityFunction;
import pique.utility.DefaultExclusionStrategy;
import pique.utility.FileUtility;

import java.nio.file.Paths;
import java.util.HashMap;

public class UtilityFunctionOutputTests {

    /***
     * creating three of these for the hell of it. Don't use now but I might in future
     */
    private static UtilityFunction buildUtilityFunction;
    private static UtilityFunction benchmarkUtilityFunction;
    private static UtilityFunction evaluateUtilityFunction;


    /***
     * Build the initial utilityFunction object, as you would see in the model build phase
     */
    @BeforeClass
    public static void buildUtilityFunctionModelBuild(){
        buildUtilityFunction = new ProbabilityDensityFunctionUtilityFunction();
        FileUtility.exportObjectToJson(buildUtilityFunction, Paths.get("src/test/out/"), "buildUtilityFunctionOutputTest.json", new DefaultExclusionStrategy());
    }

    /***
     * Expected utility function output after model benchmark phase
     *
     * It is a bit of an integration test, whatever.
     */
    @BeforeClass
    public static void fillUtilityFunctionModelBenchmark(){
        //building the model.
        buildUtilityFunction = new ProbabilityDensityFunctionUtilityFunction();
        HashMap<String, String> utilityFunctionImageURIs = new HashMap<>();
        utilityFunctionImageURIs.put("imageURIDescription1", "/image/uri/image1.json");
        buildUtilityFunction.setUtilityFunctionImageURIs(utilityFunctionImageURIs);
        HashMap<String, String> benchmarkQualityMetrics = new HashMap<>();
        benchmarkQualityMetrics.put("benchmarkQualityMetric1", "42");
        buildUtilityFunction.setBenchmarkQualityMetrics(benchmarkQualityMetrics);
        HashMap<String, String> utilityFunctionQualityMetrics = new HashMap<>();
        utilityFunctionQualityMetrics.put("utilityFunctionQualityMetric1", "42");
        buildUtilityFunction.setUtilityFunctionQualityMetrics(utilityFunctionQualityMetrics);
        HashMap<String, String> sensitivityAnalysisResults = new HashMap<>();
        sensitivityAnalysisResults.put("sensitivityAnalysisResult1", "42");
        buildUtilityFunction.setSensitivityAnalysisResults(sensitivityAnalysisResults);
        benchmarkUtilityFunction = buildUtilityFunction;
        FileUtility.exportObjectToJson(benchmarkUtilityFunction, Paths.get("src/test/out/"), "utilityFunctionModelBenchmarkOutputTest.json", new DefaultExclusionStrategy());
    }

    /***
     * Expected utility function output after model evaluate phase
     */
    @BeforeClass
    public static void fillUtilityFunctionModelEvaluate(){
        //building the model.
        buildUtilityFunction = new ProbabilityDensityFunctionUtilityFunction();
        //end of building

        HashMap<String, String> utilityFunctionImageURIs = new HashMap<>();
        utilityFunctionImageURIs.put("imageURIDescription1", "/image/uri/image1.json");
        buildUtilityFunction.setUtilityFunctionImageURIs(utilityFunctionImageURIs);
        HashMap<String, String> benchmarkQualityMetrics = new HashMap<>();
        benchmarkQualityMetrics.put("benchmarkQualityMetric1", "42");
        buildUtilityFunction.setBenchmarkQualityMetrics(benchmarkQualityMetrics);
        HashMap<String, String> utilityFunctionQualityMetrics = new HashMap<>();
        utilityFunctionQualityMetrics.put("utilityFunctionQualityMetric1", "42");
        buildUtilityFunction.setUtilityFunctionQualityMetrics(utilityFunctionQualityMetrics);
        HashMap<String, String> sensitivityAnalysisResults = new HashMap<>();
        sensitivityAnalysisResults.put("sensitivityAnalysisResult1", "42");
        buildUtilityFunction.setSensitivityAnalysisResults(sensitivityAnalysisResults);
        benchmarkUtilityFunction = buildUtilityFunction;
        //end of benchmarking


        utilityFunctionImageURIs.put("imageURIDescription2", "/image/uri/image2.json");
        benchmarkUtilityFunction.setUtilityFunctionImageURIs(utilityFunctionImageURIs);
        benchmarkQualityMetrics.put("benchmarkQualityMetric2", "moderate");
        benchmarkUtilityFunction.setBenchmarkQualityMetrics(benchmarkQualityMetrics);
        utilityFunctionQualityMetrics.put("utilityFunctionQualityMetric2", "low");
        benchmarkUtilityFunction.setUtilityFunctionQualityMetrics(utilityFunctionQualityMetrics);
        sensitivityAnalysisResults.put("sensitivityAnalysisResult2", "high");
        benchmarkUtilityFunction.setSensitivityAnalysisResults(sensitivityAnalysisResults);
        evaluateUtilityFunction = benchmarkUtilityFunction;
        FileUtility.exportObjectToJson(evaluateUtilityFunction, Paths.get("src/test/out/"), "utilityFunctionModelEvaluateOutputTest.json", new DefaultExclusionStrategy());
    }

    @Test
    public void runOutput(){
    }
}
