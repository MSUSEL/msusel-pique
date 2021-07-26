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
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QualityModelCompactExport extends QualityModelExport {

    public QualityModelCompactExport(QualityModel qualityModel, Pair<String, String>... optional) {
        super(qualityModel, optional);
    }

    public void fillTQI(QualityModel qualityModel){
        // factors::tqi
        ModelNode tqi = qualityModel.getTqi();
        CompactModelNode compactModelNode = new CompactModelNode(tqi);
        factors.get("tqi").put(compactModelNode.getName(), compactModelNode);
    }

    public void fillQualityAspects(QualityModel qualityModel){
        // factors::quality_aspects
        Map<String, ModelNode> qualityAspects = qualityModel.getQualityAspects();
        for (String key : qualityAspects.keySet()){
            CompactModelNode compactModelNode = new CompactModelNode(qualityAspects.get(key));
            factors.get("quality_aspects").put(compactModelNode.getName(), compactModelNode);
        }
    }

    public void fillProductFactors(QualityModel qualityModel){
        // factors::product_factors
        Map<String, ModelNode> productFactors = qualityModel.getProductFactors();
        for (String key : productFactors.keySet()){
            CompactModelNode compactModelNode = new CompactModelNode(productFactors.get(key));
            factors.get("product_factors").put(compactModelNode.getName(), compactModelNode);
        }
    }

    public void fillMeasures(QualityModel qualityModel){
        // measures
        Map<String, ModelNode> measures = qualityModel.getMeasures();
        for (String key : measures.keySet()){
            CompactModelNode compactModelNode = new CompactModelNode(measures.get(key));
            this.measures.put(compactModelNode.getName(), compactModelNode);
        }
    }

    public void fillDiagnostics(QualityModel qualityModel){
        // diagnostics
        Map<String, ModelNode> diagnostics = qualityModel.getDiagnostics();
        for (String key : diagnostics.keySet()){
            CompactModelNode compactModelNode = new CompactModelNode(diagnostics.get(key));
            this.diagnostics.put(compactModelNode.getName(), compactModelNode);
        }
    }

    private class CompactModelNode extends ModelNode{

        //I am trying this without exposing compact Children, which is just the name of hte child. I can get all this info from the weights field.
        //If I want to explicitly use children names, use @Expose here.
        @Getter
        private List<String> compactChildren = new ArrayList<>();

        public CompactModelNode(ModelNode modelNode){
            super(modelNode.getName(), modelNode.getDescription(), modelNode.getEvaluatorObject(), modelNode.getNormalizerObject(),
                modelNode.getUtilityFunctionObject(), modelNode.getWeights(), modelNode.getThresholds());

            compactChildren.addAll(modelNode.getChildren().keySet());
        }

        /**
         * A loose way of implementing the Prototype design pattern.
         * Each quality model node must describe how to clone itself. ---Not really needed in this subclass.
         *
         * @return A deep clone of the current node object.
         */
        @Override
        public ModelNode clone() {
            return null;
        }
    }
}
