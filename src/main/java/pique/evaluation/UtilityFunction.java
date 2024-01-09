package pique.evaluation;


import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/***
 * Basically a data class to hold information pertinent to utility functions.
 */
public abstract class UtilityFunction implements IUtilityFunction{

    @Expose
    @Getter @Setter
    private String name;

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
     * basic constructor to build object in model building run, object will be filled as other data is calculated and fed to it.
     * Other data will come from benchmark and evaluate phase.
     *
     * Initializing other attributes because I do want them output to a built model json file.
     *
     */
    public UtilityFunction(String name, String description){
        this.name = name;
        this.description = description;
    }

    public UtilityFunction(String name, String description, String benchmarkTag, HashMap<String, String> utilityFunctionImageURIs) {
        this.name = name;
        this.description = description;
        this.benchmarkTag = benchmarkTag;
        this.utilityFunctionImageURIs = utilityFunctionImageURIs;
    }

    public UtilityFunction(String name, String description, String benchmarkTag, HashMap<String, String> utilityFunctionImageURIs, HashMap<String, String> benchmarkQualityMetrics, HashMap<String, String> utilityFunctionQualityMetrics, HashMap<String, String> sensitivityAnalysisResults) {
        this.name = name;
        this.description = description;
        this.benchmarkTag = benchmarkTag;
        this.utilityFunctionImageURIs = utilityFunctionImageURIs;
        this.benchmarkQualityMetrics = benchmarkQualityMetrics;
        this.utilityFunctionQualityMetrics = utilityFunctionQualityMetrics;
        this.sensitivityAnalysisResults = sensitivityAnalysisResults;
    }
}
