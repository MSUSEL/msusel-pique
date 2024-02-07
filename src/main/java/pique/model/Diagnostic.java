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

import com.google.gson.annotations.Expose;
import lombok.Getter;
import pique.evaluation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * A diagnostic object contains a collection of Findings that matches this object's ID and
 * defines how to evaluate its Finding collection -- for example, a count.
 *
 * A diagnostic can also be thought of as a "rule" or the ID of a finding a static analysis tool
 * attempts to find.
 *
 * Diagnostic objects are the components a Measure object needs to run its evaluation.
 *
 * Metrics example: for a metrics Measure "MIF" (Method Inheritance Factor) that is evaluated by the formula
 *          MIF = inherited methods / total methods available
 * and you have a static analysis tool that can find IM (inherited methods) and TMA (total methods available),
 * IM and TMA are Diagnostic objects, and the Measure object MIF's eval function is IM / TMA.
 *
 * Rule example: for a rule Measure "Encryption Faults" that is evaluated by the sum of all tool findings that relate
 * to encryption, imagine a tool named Security Code Scan that has 3 rules relating to encryption, "SCS001", "SCS002",
 * and "SCS003".  SCS001, SCS002, and SCS003 are Diagnostic objects whose eval function should be to sum the findings
 * with their Diagnostic name.  The Measure "Encryption Faults" would then eval by suming its diagnostic object's evals.
 */
public class Diagnostic extends ModelNode {

    // Instance variables
    @Getter
    @Expose
    private String toolName;


    // Constructors
    public Diagnostic(String id, String description, String toolName) {
        super(id, description, new DefaultDiagnosticEvaluator(), new DefaultNormalizer());
        this.toolName = toolName;
    }

    /**
     * Constructor with custom evaluation function
     *
     * @param id
     *      The diagnostic name as it appears in the tool's output file (e.g. "SCS0005")
     * @param description
     *      Description of the diagnostic according to its tool's or metric's documentation
     * @param toolName
     *      The name of the tool that generates this Diagnostic in its output file.  Should match the name of
     *      its associated ITool::name object
     */
    public Diagnostic(String id, String description, String toolName, IEvaluator evaluator) {
        super(id, description, evaluator, new DefaultNormalizer());
        this.toolName = toolName;
    }

    public Diagnostic(String id, String description, String toolName, IEvaluator evaluator, INormalizer normalizer,
                      IUtilityFunction utilityFunction, Map<String, BigDecimal> weights, BigDecimal[] thresholds) {
        super(id, description, evaluator, normalizer, utilityFunction, weights, thresholds);
        this.toolName = toolName;
    }

    // Used for cloning
    public Diagnostic(BigDecimal value, String name, String description, IEvaluator evaluator, INormalizer normalizer,
                      IUtilityFunction utilityFunction, Map<String, BigDecimal> weights, BigDecimal[] thresholds, Map<String,
        ModelNode> children) {
        super(value, name, description, evaluator, normalizer, utilityFunction, weights, thresholds, children);
    }

    // Methods

    @Override
    public ModelNode clone() {

        Map<String, ModelNode> clonedChildren = new HashMap<>();
        getChildren().forEach((k, v) -> clonedChildren.put(k, v.clone()));

        return new Diagnostic(getValue(), getName(), getDescription(), this.getEval_strategyObj(), this.getNormalizerObj(),
                this.getUtility_function(), getWeights(), getThresholds(), clonedChildren);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Diagnostic)) { return false; }
        Diagnostic otherDiagnostic = (Diagnostic) other;

        return getName().equals(otherDiagnostic.getName())
                && getToolName().equals(otherDiagnostic.getToolName());
    }
}
