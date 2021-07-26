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
import org.apache.commons.lang3.tuple.Pair;
import pique.utility.DefaultExclusionStrategy;
import pique.utility.FileUtility;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Duplicate information of the QualityModel class, but with fields set for ease of Gson json exporting.
 * This class is a cheap hack for exporting a quality model in the desired format. This class is esentially
 * a container class.
 *
 * There is a much better way to handle this instead of having this class, but that can be a future problem to deal with :)
 */
// TODO (1.0): Better GSON supprt.  Expose while limiting depth. Currect state will cause problems for large quality
//  models.  Likely not a difficult fix.
public class QualityModelExport {

    /// Fields ///
    @Getter @Expose
    protected String name;
    @Getter @Expose
    protected Map<String, String> additionalData = new HashMap<>();
    @Expose
    protected Map<String, String> global_config = new HashMap<>();
    @Getter @Expose
    protected Map<String, Map<String, ModelNode>> factors = new HashMap<>();
    @Getter @Expose
    protected Map<String, ModelNode> measures = new HashMap<>();
    @Getter @Setter @Expose
    protected Map<String, ModelNode> diagnostics = new HashMap<>();


    /// Constructor
    @SafeVarargs
    public QualityModelExport(QualityModel qualityModel, Pair<String, String>... optional) {

        // Setup
        factors.put("tqi", new HashMap<>());
        factors.put("quality_aspects", new HashMap<>());
        factors.put("product_factors", new HashMap<>());

        // Basic info
        this.name = qualityModel.getName();
        this.global_config.put("benchmark_strategy", qualityModel.getBenchmarker().getName());
        this.global_config.put("weights_strategy", qualityModel.getWeighter().getName());
        if (optional.length > 0 ) { for (Pair<String, String> entry : optional) { additionalData.put(entry.getKey(), entry.getValue()); }}

        fillTQI(qualityModel);
        fillQualityAspects(qualityModel);
        fillProductFactors(qualityModel);
        fillMeasures(qualityModel);
        fillDiagnostics(qualityModel);
    }

    public void fillTQI(QualityModel qualityModel){
        // factors::tqi
        ModelNode tqi = qualityModel.getTqi();
        factors.get("tqi").put(tqi.getName(), tqi);
    }

    public void fillQualityAspects(QualityModel qualityModel){
        // factors::quality_aspects
        Map<String, ModelNode> qualityAspects = qualityModel.getQualityAspects();
        factors.get("quality_aspects").putAll(qualityAspects);
    }

    public void fillProductFactors(QualityModel qualityModel){
        // factors::product_factors
        Map<String, ModelNode> productFactors = qualityModel.getProductFactors();
        factors.get("product_factors").putAll(productFactors);
    }

    public void fillMeasures(QualityModel qualityModel){
        // measures
        Map<String, ModelNode> measures = qualityModel.getMeasures();
        this.measures.putAll(measures);
    }

    public void fillDiagnostics(QualityModel qualityModel){
        // diagnostics
        Map<String, ModelNode> diagnostics = qualityModel.getDiagnostics();
        this.diagnostics.putAll(diagnostics);
    }

    /// Methods ///
    /**
     * Create a hard-drive file representation of the model
     *
     * @param outputDirectory
     * 		The directory to place the QM file into.  Does not need to exist beforehand.
     * @return
     * 		The path of the exported model file.
     */
    public Path exportToJson(String fileName, Path outputDirectory) {
        return FileUtility.exportObjectToJson(this, outputDirectory, fileName, new DefaultExclusionStrategy());
    }
}
