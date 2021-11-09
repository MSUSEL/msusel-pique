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
package pique.calibration;

import pique.model.ModelNode;
import pique.model.QualityModel;
import pique.utility.BigDecimalWithContext;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

// TODO (1.0): Documentation
public class NaiveWeighter implements IWeighter {

    /**
     * Set each node's incoming edge weights equal to the average of all incoming edges.
     */
    @Override
    public Set<WeightResult> elicitateWeights(QualityModel qualityModel, Path... externalInput) {

        Set<WeightResult> weights = new HashSet<>();

        qualityModel.getAllQualityModelNodes().values().forEach(node -> {
            WeightResult weightResult = new WeightResult(node.getName());
            node.getChildren().values().forEach(child -> weightResult.setWeight(child.getName(), averageWeight(node)));
            weights.add(weightResult);
        });

        return weights;
    }

    @Override
    public String getName() {
        return this.getClass().getCanonicalName();
    }

    private BigDecimal averageWeight(ModelNode currentNode) {
        return new BigDecimalWithContext("1.0").divide(new BigDecimalWithContext(""+currentNode.getChildren().size()),BigDecimalWithContext.getMC());
    }
}
