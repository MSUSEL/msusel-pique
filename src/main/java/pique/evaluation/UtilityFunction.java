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
