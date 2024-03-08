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

import pique.evaluation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the total quality index of a project.
 * It is used for the evaluation (quality assessment - estimation) of a
 * certain project or a benchmark of projects.
 *
 * @author Miltos, Rice
 */
public class Tqi extends ModelNode {

    // Constructor

    public Tqi(String name, String description, Map<String, BigDecimal> weights) {
        super(name, description, new DefaultFactorEvaluator(), new DefaultNormalizer());
        this.weights = (weights == null) ? new HashMap<>() : weights;
    }

    public Tqi(String name, String description, IEvaluator evaluator, INormalizer normalizer,
               IUtilityFunction utilityFunction, Map<String, BigDecimal> weights, BigDecimal[] thresholds) {
        super(name, description, evaluator, normalizer, utilityFunction, weights, thresholds);
    }

    // Used for cloning
    public Tqi(BigDecimal value, String name, String description, IEvaluator evaluator, INormalizer normalizer,
               IUtilityFunction utilityFunction, Map<String, BigDecimal> weights, BigDecimal[] thresholds, Map<String,
            ModelNode> children) {
        super(value, name, description, evaluator, normalizer, utilityFunction, weights, thresholds, children);
    }


    // Methods

    /**
     * Tqi clone needs to work from a bottom-up parse to allow the fully connected
     * QualityAspect -> ProductFactor layer to pass the cloned property nodes by reference.
     */
    // TODO (1.0): Possible breaking change?
    @Override
    public ModelNode clone() {

        Map<String, ModelNode> clonedChildren = new HashMap<>();
        getChildren().forEach((k, v) -> {
            clonedChildren.put(k, v.clone());
        });

        return new Tqi(getValue(), getName(), getDescription(), this.getEval_strategyObj(), this.getNormalizerObj(), this.getUtility_function(),
                 getWeights(), getThresholds(), clonedChildren);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Tqi)) {
            return false;
        }
        Tqi otherTqi = (Tqi) other;

        return getName().equals(otherTqi.getName())
                && getChildren().size() == otherTqi.getChildren().size();
    }

}
