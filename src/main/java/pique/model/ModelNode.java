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
import lombok.Setter;
import pique.evaluation.*;
import pique.utility.BigDecimalWithContext;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract representation of a node belonging to the Quality Model
 */
public abstract class ModelNode {

    //region Fields
    @Getter @Setter @Expose
    protected String name;
    @Setter @Expose
    protected BigDecimal value;  // the value this node evaluates to
    @Getter @Setter @Expose
    protected String description;
    // TODO: eventually consider new tree object that combines properties and their weight instead of relying on
    //  String name matching (not enough time for me to solve currently)
    @Getter @Expose
    protected Map<String, ModelNode> children = new HashMap<>();
    @Getter @Setter @Expose
    protected Map<String, BigDecimal> weights = new HashMap<>();
    @Getter @Setter @Expose
    protected BigDecimal[] thresholds;


    //@Expose -- uncomment when we get vis schema working
    @Getter @Setter
    protected IEvaluator eval_strategyObj;

    // remove me once we get vis schema working
    @Expose
    @Getter @Setter
    protected String eval_strategy;

    //@Expose -- uncomment when we get vis schema working
    @Getter @Setter
    protected INormalizer normalizerObj;

    // remove me once we get vis schema working
    @Expose
    @Getter @Setter
    protected String normalizer;

    @Expose
    @Getter @Setter
    protected IUtilityFunction utility_function;

    @Getter @Setter
    protected boolean visited = false;      // Use for BFS traversal

    // Constructor

    public ModelNode(String name, String description, IEvaluator eval_strategyObj, INormalizer normalizerObj) {
        this.name = name;
        this.description = description;
        this.eval_strategyObj = eval_strategyObj;
        this.normalizerObj = normalizerObj;
        this.utility_function = new ProbabilityDensityFunctionUtilityFunction();

        // remove me once we get vis schema working
        eval_strategy = eval_strategyObj.getName();
        normalizer = normalizerObj.getName();

    }

    public ModelNode(String name, String description, IEvaluator eval_strategyObj, INormalizer normalizerObj,
                     IUtilityFunction utility_function, Map<String, BigDecimal> weights, BigDecimal[] thresholds) {
        this.name = name;
        this.description = description;
        this.eval_strategyObj = eval_strategyObj;
        this.normalizerObj = normalizerObj;
        this.utility_function = utility_function;
        if (weights != null) this.weights = weights;
        if (thresholds != null) this.thresholds = thresholds;

        // remove me once we get vis schema working
        eval_strategy = eval_strategyObj.getName();
        normalizer = normalizerObj.getName();
    }

    /**
     * Constructor for cloning.
     */
    public ModelNode(BigDecimal value, String name, String description, IEvaluator eval_strategyObj, INormalizer normalizerObj,
                     IUtilityFunction utility_function, Map<String, BigDecimal> weights, BigDecimal[] thresholds, Map<String,
        ModelNode> children) {
        this.value = value;
        this.name = name;
        this.description = description;
        this.eval_strategyObj = eval_strategyObj;
        this.normalizerObj = normalizerObj;
        this.utility_function = utility_function;
        this.weights = weights;
        this.thresholds = thresholds;
        this.children = children;

        // remove me once we get vis schema working
        eval_strategy = eval_strategyObj.getName();
        normalizer = normalizerObj.getName();
    }
    //endregion

    //region Getters and setters

    public ModelNode getAnyChild() {
        ModelNode anyModelNode = getChildren().values().stream().findAny().orElse(null);
        assert anyModelNode != null;
        return anyModelNode;
    }

    public ModelNode getChild(String name) {
        return getChildren().get(name);
    }

    public void setChild(ModelNode child) {
        getChildren().put(child.getName(), child);
    }

    public void setChildren(Map<String, ModelNode> children) {
        this.children = children;
    }

    public void setChildren(Collection<ModelNode> children) {
        children.forEach(element -> getChildren().putIfAbsent(element.getName(), element));
    }

    public void setNormalizerValue(BigDecimal value) {
        this.getNormalizerObj().setNormalizerValue(value);
    }

    public int getNumChildren() { return getChildren().size(); }

    public BigDecimal getValue() {
        evaluate();
        return this.value;
    }

    public BigDecimal getWeight(String modelNodeName) {
    	return weights.getOrDefault(modelNodeName, new BigDecimalWithContext("0.0"));
    }

    public void setWeight(String modelNodeName, BigDecimal value) {
        this.weights.put(modelNodeName, value);
    }

    //endregion


    //region Methods

    /**
     * A loose way of implementing the Prototype design pattern.
     * Each quality model node must describe how to clone itself.
     *
     * @return A deep clone of the current node object.
     */
    public abstract ModelNode clone();

    /**
     * TODO (1.0): Documentation
     */
    protected void evaluate() {
        setValue(this.getEval_strategyObj().evaluate(this));
    }

    //endregion
}
