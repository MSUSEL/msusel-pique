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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * A Measure is a concrete, numerical way to measure a given ProductFactor (property of source code).
 * The components necessary to complete a Measure's evaluation are a Measure's Diagnostics.
 *
 * A Measure is therefore a collection of its necessary diagnostics and an evaluation function describing
 * how to evaluate itself using its diagnostics.
 *
 * Metrics example: for a metrics Measure "MIF" (Method Inheritance Factor) that is evaluated by the formula
 * 							MIF = inherited methods / total methods available
 *  	and you have a static analysis tool that can find IM (inherited methods) and TMA (total methods available),
 *  	IM and TMA are Diagnostic objects, and the Measure object MIF's eval function is IM::value / TMA::value.
 *
 *  Rule example: for a rule Measure "Encryption Faults" that is evaluated by the sum of all tool findings that relate
 *  	to encryption, imagine a tool named Security Code Scan that has 3 rules relating to encryption, "SCS001", "SCS002",
 *  	and "SCS003".  SCS001, SCS002, and SCS003 are Diagnostic objects whose eval function should be to sum the
 *  	findings with their Diagnostic name.  The Measure "Encryption Faults" may then eval by suming its diagnostic
 *  	object's evals.
 */
public class Measure extends ModelNode {

	//region Instance variables

	@Getter @Setter
	@Expose
	private boolean positive;

	//endregion


	//region Constructors
	// TODO (1.0) A lot of these constructors can be (and should be) removed with the 1.0 redesign
	public Measure(String name, String description, INormalizer normalizer, boolean positive) {
		super(name, description, new DefaultMeasureEvaluator(), normalizer);
		this.positive = positive;
		this.utility_function = new DefaultUtility();
	}

	public Measure(String name, String description, IEvaluator evaluator, INormalizer normalizer,
				   IUtilityFunction utilityFunction, Map<String, BigDecimal> weights, BigDecimal[] thresholds,
				   boolean positive) {
		super(name, description, evaluator, normalizer, utilityFunction, weights, thresholds);
		this.positive = positive;
	}

	public Measure(String name, String description, INormalizer normalizer, boolean positive,
				   Map<String, ModelNode> diagnostics) {
		super(name, description, new DefaultMeasureEvaluator(), normalizer);
		this.positive = positive;
		this.children = diagnostics;
	}

	public Measure(String name, String description, INormalizer normalizer, boolean positive,
				   Map<String, ModelNode> diagnostics, BigDecimal[] thresholds) {
		super(name, description, new DefaultMeasureEvaluator(), normalizer);
		this.positive = positive;
		this.children = diagnostics;
		this.thresholds = thresholds;
	}

	public Measure(String name, String description, boolean positive, INormalizer normalizer,
				   Map<String, ModelNode> diagnostics, BigDecimal[] thresholds) {
		super(name, description, new DefaultMeasureEvaluator(), normalizer);
		this.positive = positive;
		this.children = diagnostics;
		this.thresholds = thresholds;
	}


	public Measure(String name, String description, INormalizer normalizer, boolean positive,
				   Map<String, ModelNode> diagnostics, BigDecimal[] thresholds, IEvaluator evaluator) {
		super(name, description, evaluator, normalizer);
		this.positive = positive;
		this.children = diagnostics;
		this.thresholds = thresholds;
	}

	public Measure(String name, String description, INormalizer normalizer, boolean positive,
				   Map<String, ModelNode> diagnostics, BigDecimal[] thresholds, IEvaluator evaluator,
				   IUtilityFunction utilityFunction)
	{
		super(name, description, evaluator, normalizer);
		this.positive = positive;
		this.children = diagnostics;
		this.utility_function = utilityFunction;
		this.thresholds = thresholds;
	}

	// Used for cloning
	public Measure(BigDecimal value, String name, String description, IEvaluator evaluator, INormalizer normalizer,
				   IUtilityFunction utilityFunction, Map<String, BigDecimal> weights, BigDecimal[] thresholds, Map<String,
		ModelNode> children) {
		super(value, name, description, evaluator, normalizer, utilityFunction, weights, thresholds, children);
	}

	//endregion


	//region Methods

	@Override
	public ModelNode clone() {
		Map<String, ModelNode> clonedChildren = new HashMap<>();
		getChildren().forEach((k, v) -> {
			clonedChildren.put(k, v.clone());
		});

		return new Measure(getValue(), getName(), getDescription(), this.getEval_strategyObj(), this.getNormalizerObj(),
				this.getUtility_function(), getWeights(), getThresholds(), clonedChildren);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Measure)) { return false; }
		Measure otherMeasure = (Measure) other;

		return getName().equals(otherMeasure.getName())
				&& (getChildren().size() == otherMeasure.getChildren().size());
	}

	//endregion
}
