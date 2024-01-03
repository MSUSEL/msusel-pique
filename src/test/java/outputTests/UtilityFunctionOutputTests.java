package outputTests;

import org.junit.BeforeClass;
import org.junit.Test;
import pique.model.UtilityFunction;
import pique.utility.DefaultExclusionStrategy;
import pique.utility.FileUtility;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class UtilityFunctionOutputTests {

    private static UtilityFunction utilityFunction;


    @BeforeClass
    public static void fillObject(){
        String benchmarkTag = "benchmarkTag";
        String description = "description";
        HashMap<String, String> utilityFunctionImageURIs = new HashMap<>();
        utilityFunctionImageURIs.put("imageURIDescription1", "/image/uri/image1.json");
        utilityFunctionImageURIs.put("imageURIDescription2", "/image/uri/image2.json");
        HashMap<String, String> benchmarkQualityMetrics = new HashMap<>();
        benchmarkQualityMetrics.put("benchmarkQualityMetric1", "42");
        benchmarkQualityMetrics.put("benchmarkQualityMetric2", "moderate");
        HashMap<String, String> utilityFunctionQualityMetrics = new HashMap<>();
        utilityFunctionQualityMetrics.put("utilityFunctionQualityMetric1", "42");
        utilityFunctionQualityMetrics.put("utilityFunctionQualityMetric2", "low");
        HashMap<String, String> sensitivityAnalysisResults = new HashMap<>();
        sensitivityAnalysisResults.put("sensitivityAnalysisResult1", "42");
        sensitivityAnalysisResults.put("sensitivityAnalysisResult2", "high");
        utilityFunction = new UtilityFunction(benchmarkTag, description, utilityFunctionImageURIs, benchmarkQualityMetrics, utilityFunctionQualityMetrics, sensitivityAnalysisResults);
    }

    @Test
    public void runOutput(){
        FileUtility.exportObjectToJson(utilityFunction, Paths.get("src/test/out/"), "utilityFunctionOutputTest.json", new DefaultExclusionStrategy());

    }
}
