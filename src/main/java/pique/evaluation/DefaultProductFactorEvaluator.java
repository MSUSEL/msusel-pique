/*
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

import java.math.BigDecimal;

import pique.model.ModelNode;
import pique.utility.BigDecimalWithContext;

// TODO (1.0): Documentation
public class DefaultProductFactorEvaluator extends Evaluator {

    @Override
    public BigDecimal evaluate(ModelNode inNode) {
    	BigDecimal weightedSum = new BigDecimalWithContext("0.0");
        for (ModelNode child : inNode.getChildren().values()) {
            weightedSum = weightedSum.add(child.getValue());
        }
        if (inNode.getChildren().size() != 0){
            //running into issues here when no children are declared; either because we will add them to the model later or a mistake with the model definition
            weightedSum = weightedSum.divide(new BigDecimalWithContext(""+ inNode.getChildren().size()),BigDecimalWithContext.getMC());
        }
        return weightedSum;
    }

}
