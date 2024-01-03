package pique.model;


import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/***
 * Basically a data class to hold information pertinent to utility functions.
 */
public class UtilityFunction {

    @Expose
    @Getter @Setter
    private String benchmarkTag;

    @Expose
    @Getter @Setter
    private String description;

    // arraylist is a little funky, consider hashmap with the key being the descriptor or something. Needs to be dynamic in size
    @Expose
    @Getter @Setter
    private HashMap<String, String> utilityFunctionImageURIs;

    // as of the time of writing this, we don't have any benchmark quality metrics, but a hashmap<string, string> should (tm) cover everything.
    // it was also suggested that having a "tips to interpret" feature would be cool. Maybe we can package that in the hashmap, in the future.
    @Expose
    @Getter @Setter
    private HashMap<String, String> benchmarkQualityMetrics;

    // as of the time of writing this, we don't have any p.d.f quality metrics, but a hashmap<string, string> should (tm) cover everything.
    // it was also suggested that having a "tips to interpret" feature would be cool. Maybe we can package that in the hashmap, in the future.
    @Expose
    @Getter @Setter
    private HashMap<String, String> utilityFunctionQualityMetrics;

    // as of the time of writing this, we don't have a result or even a data format for sensitivity analysis, but a hashmap<string, string> should (tm) cover everything.
    // it was also suggested that having a "tips to interpret" feature would be cool. Maybe we can package that in the hashmap, in the future.
    @Expose
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
    public UtilityFunction(String benchmarkTag, String description, HashMap<String, String> utilityFunctionImageURIs) {
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
     * @param utilityFunctionQualityMetrics
     * @param sensitivityAnalysisResults
     */
    public UtilityFunction(String benchmarkTag, String description, HashMap<String, String> utilityFunctionImageURIs, HashMap<String, String> benchmarkQualityMetrics, HashMap<String, String> utilityFunctionQualityMetrics, HashMap<String, String> sensitivityAnalysisResults) {
        this.benchmarkTag = benchmarkTag;
        this.description = description;
        this.utilityFunctionImageURIs = utilityFunctionImageURIs;
        this.benchmarkQualityMetrics = benchmarkQualityMetrics;
        this.utilityFunctionQualityMetrics = utilityFunctionQualityMetrics;
        this.sensitivityAnalysisResults = sensitivityAnalysisResults;
    }
}
