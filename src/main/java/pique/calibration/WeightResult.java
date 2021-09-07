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

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Struct type class for representing the results weight elicitation.
 */
public class WeightResult {

    // Fields
    /*
     * Note: fields are public due to Sun Java coding guidelines stance on struct classes.
     * See https://www.oracle.com/technetwork/java/javase/documentation/codeconventions-137265.html#177
     * for more information.
     */
    @Getter @Setter
    private String name;  // the name of the ModelNode object these weights apply to

    @Getter @Setter
    private Map<String, BigDecimal> weights;  // weight mapping of {Key: incoming weight ModelNode name, Value: weight value}


    // Constructor
    public WeightResult(String name) {
        this.name = name;
        this.weights = new HashMap<>();
    }

    public void setWeight(String weightName, BigDecimal value) {
        getWeights().put(weightName, value);
    }
}
