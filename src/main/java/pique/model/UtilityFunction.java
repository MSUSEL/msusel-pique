package pique.model;


import lombok.Getter;
import lombok.Setter;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/***
 * Basically a data class to hold information pertinent to utility functions.
 */
public class UtilityFunction {

    @Getter @Setter
    private String benchmarkTag;

    @Getter @Setter
    private String description;

    // arraylist is a little funky, consider hashmap with the key being the descriptor or something. Needs to be dynamic in size
    @Getter @Setter
    private ArrayList<Paths> utilityFunctionImageURIs;

    // as of the time of writing this, we don't have any benchmark quality metrics, but a hashmap<string, string> should (tm) cover everything.
    // it was also suggested that having a "tips to interpret" feature would be cool. Maybe we can package that in the hashmap, in the future.
    @Getter @Setter
    private HashMap<String, String> benchmarkQualityMetrics;

    // as of the time of writing this, we don't have any p.d.f quality metrics, but a hashmap<string, string> should (tm) cover everything.
    // it was also suggested that having a "tips to interpret" feature would be cool. Maybe we can package that in the hashmap, in the future.
    @Getter @Setter
    private HashMap<String, String> probabilityDensityFunctionQualityMetrics;

    // as of the time of writing this, such results, but a hashmap<string, string> should (tm) cover everything.
    // it was also suggested that having a "tips to interpret" feature would be cool. Maybe we can package that in the hashmap, in the future.
    @Getter @Setter
    private HashMap<String, String> sensitivityAnalysisResults;


    /***
     * empty constructor, delete if never used
     */
    public UtilityFunction(){

    }

    /***
     * Constructor to supply what we have implemented at this moment in time (12/26/2023)
     *
     * @param benchmarkTag
     * @param description
     * @param utilityFunctionImageURIs
     */
    public UtilityFunction(String benchmarkTag, String description, ArrayList<Paths> utilityFunctionImageURIs) {
        this.benchmarkTag = benchmarkTag;
        this.description = description;
        this.utilityFunctionImageURIs = utilityFunctionImageURIs;
    }

    /***
     * full constructor with all fields, just to have it :)
     *
     * @param benchmarkTag
     * @param description
     * @param utilityFunctionImageURIs
     * @param benchmarkQualityMetrics
     * @param probabilityDensityFunctionQualityMetrics
     * @param sensitivityAnalysisResults
     */
    public UtilityFunction(String benchmarkTag, String description, ArrayList<Paths> utilityFunctionImageURIs, HashMap<String, String> benchmarkQualityMetrics, HashMap<String, String> probabilityDensityFunctionQualityMetrics, HashMap<String, String> sensitivityAnalysisResults) {
        this.benchmarkTag = benchmarkTag;
        this.description = description;
        this.utilityFunctionImageURIs = utilityFunctionImageURIs;
        this.benchmarkQualityMetrics = benchmarkQualityMetrics;
        this.probabilityDensityFunctionQualityMetrics = probabilityDensityFunctionQualityMetrics;
        this.sensitivityAnalysisResults = sensitivityAnalysisResults;
    }
}
