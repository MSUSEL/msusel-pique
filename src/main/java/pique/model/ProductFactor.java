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

public class ProductFactor extends ModelNode {

	// Constructors
	public ProductFactor(String name, String description) {
		super(name, description, new DefaultFactorEvaluator(), new DefaultNormalizer());
	}

	public ProductFactor(String name, String description, IEvaluator evaluator, INormalizer normalizer,
						 IUtilityFunction utilityFunction, Map<String, BigDecimal> weights, BigDecimal[] thresholds) {
		super(name, description, evaluator, normalizer, utilityFunction, weights, thresholds);
	}

	public ProductFactor(String name, String description, IEvaluator evaluator) {
		super(name, description, new DefaultFactorEvaluator(), new DefaultNormalizer());
		this.eval_strategyObj = evaluator;
	}

	public ProductFactor(String name, String description, ModelNode measure){
		super(name, description, new DefaultFactorEvaluator(), new DefaultNormalizer());
		this.children.put(measure.getName(), measure);
	}

	// Used for cloning
	public ProductFactor(BigDecimal value, String name, String description, IEvaluator evaluator, INormalizer normalizer,
						 IUtilityFunction utilityFunction, Map<String, BigDecimal> weights, BigDecimal[] thresholds, Map<String,
		ModelNode> children) {
		super(value, name, description, evaluator, normalizer, utilityFunction, weights, thresholds, children);
	}

	//region Methods

	@Override
	public ModelNode clone() {

		Map<String, ModelNode> clonedChildren = new HashMap<>();
		getChildren().forEach((k, v) -> {
			clonedChildren.put(k, v.clone());
		});

		return new ProductFactor(getValue(), getName(), getDescription(), this.getEval_strategyObj(), this.getNormalizerObj(),
				this.getUtility_function(), getWeights(), getThresholds(), clonedChildren);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ProductFactor)) { return false; }
		ProductFactor otherProductFactor = (ProductFactor) other;

		return getName().equals(otherProductFactor.getName())
				&& getAnyChild().equals(otherProductFactor.getAnyChild());
	}

	//endregion

}
