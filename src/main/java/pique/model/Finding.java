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

import lombok.Getter;
import lombok.Setter;
import pique.evaluation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * A Finding is actual instances of diagnostic IDs found in a project after a static analysis tool run.
 * <p>
 * A Finding can be a metric value (e.g. the tool found that the "total methods available in classes" is 401)
 * or a finding can be a rule discovery (e.g. rule "SCS001: Bad Hashing Function" was found in file X.java
 * at line 5, character 2).
 */
public class Finding extends ModelNode {

    //region Instance variables

    @Getter @Setter
    private int characterNumber;
    @Getter @Setter
    private int lineNumber;
    @Getter @Setter
    private int severity;  // TODO: consider refactoring into enum
    @Getter @Setter
    private String filePath;

    //endregion

    // Constructors

    // (TODO: change to builder pattern to better accommodate metrics and rule findings)

    public Finding(String filePath, int lineNumber, int characterNumber, int severity) {
        super("", "", new DefaultFindingEvaluator(), new DefaultNormalizer());
        this.name = hashName(filePath, String.valueOf(lineNumber), String.valueOf(characterNumber),
            String.valueOf(severity));
        this.filePath = filePath;
        this.lineNumber = lineNumber;
        this.characterNumber = characterNumber;
        this.severity = severity;
    }


    // Used for cloning
    public Finding(BigDecimal value, String name, String description, IEvaluator evaluator, INormalizer normalizer,
                   IUtilityFunction utilityFunction, Map<String, BigDecimal> weights, BigDecimal[] thresholds, Map<String,
            ModelNode> children, String filePath, int lineNumber, int characterNumber, int severity) {
        super(value, name, description, evaluator, normalizer, utilityFunction, weights, thresholds, children);
        this.filePath = filePath;
        this.lineNumber = lineNumber;
        this.characterNumber = characterNumber;
        this.severity = severity;
    }


    public String getLocation() {
        return filePath + "," + lineNumber + "," + characterNumber;
    }


    //region Methods

    @Override
    public Finding clone() {
        return new Finding(getValue(), getName(), getDescription(), this.getEval_strategyObj(), this.getNormalizerObj(),
                this.getUtility_function(), getWeights(), getThresholds(), getChildren(), getFilePath(), getLineNumber(),
                getCharacterNumber(), getSeverity());
    }

    // Generate a hashed name using the properties of the Finding under construction.
    private String hashName(String... params) {

        StringBuilder hashBuilder = new StringBuilder();

        for (String param : params) {
            hashBuilder.append(param);
        }

        String valueToHash = hashBuilder.toString();
        return String.valueOf(valueToHash.hashCode());
    }

    //endregion
}
