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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pique.calibration.IBenchmarker;
import pique.calibration.IWeighter;
import pique.calibration.ProbabilityDensityFunctionBenchmarker;
import pique.calibration.NaiveWeighter;
import pique.evaluation.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;

/**
 * Class responsible for parsing quality model file data and transforming it into a {@link ModelNode} object tree.
 * <p>
 * Note, this class is entirely dependent on the assumption that the quality model file is written in a specific,
 * intenional syntax.
 * Pre and post checks, and more robost design is left as future work.
 * </p>
 * <p>
 * This class is capable of importing a quality model description (no edge weights or threshold arrays) as well as a
 * derived  quality model (includes edge weights and threshold arrays). The user does not need to distingust between
 * the two.
 * </p>
 * <p>
 * This class heavily utilizes class-global state variables. Not the greatest design, but does the job given this
 * class is simply for parsing an input text file.
 *</p>
 */
public class QualityModelImport {

    // Private Fields
    private QualityModel qualityModel = new QualityModel();

    private ModelNode tqi;
    private Map<String, ModelNode> qualityAspects = new HashMap<>();
    private Map<String, ModelNode> productFactors = new HashMap<>();
    private Map<String, ModelNode> measures = new HashMap<>();
    private Map<String, ModelNode> diagnostics = new HashMap<>();

    private JsonObject jsonQm;
    private JsonObject jsonTqi;
    private JsonObject jsonFactors;
    private JsonObject jsonQualityAspects;
    private JsonObject jsonProductFactors;
    private JsonObject jsonMeasures;
    private JsonObject jsonDiagnostics;


    // Constructor

    public QualityModelImport(Path qmFileLocation) {
        try {
            FileReader fr = new FileReader(qmFileLocation.toString());
            jsonQm = new JsonParser().parse(fr).getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    // Methods

    /**
     * One-shot run of model import.
     * <p>
     * Note: The procedures assume that each model node name is unique.
     * TODO (1.0): Pre-check that all model node names in the QM file are unique
     *
     * @return A PIQUE JVM object of the quality model file.
     */
    public QualityModel importQualityModel() {

        // Set top-level data
        qualityModel.setName(instanceNameFromJson());
        qualityModel.setBenchmarker(instanceBenchmarkerFromJson());
        qualityModel.setWeighter(instanceWeighterFromJson());

        // Instance each node, layer by layer (no edges yet)
        jsonFactors = jsonQm.getAsJsonObject("factors");
        jsonTqi = jsonFactors.getAsJsonObject("tqi");
        jsonQualityAspects = jsonFactors.getAsJsonObject("quality_aspects");
        jsonProductFactors = jsonFactors.getAsJsonObject("product_factors");
        jsonMeasures = jsonQm.getAsJsonObject("measures");
        jsonDiagnostics = jsonQm.getAsJsonObject("diagnostics");

        tqi = instanceTqiFromJson(jsonTqi);
        qualityAspects = instanceQualityAspectsFromJson(jsonQualityAspects);
        productFactors = instanceProductFactorsFromJson(jsonProductFactors);
        measures = instanceMeasuresFromJson(jsonMeasures);
        diagnostics = instanceDiagnosticsFromJson(jsonDiagnostics);

        // Use ModelNode instances to connect edges using name matching, bottom to top
        jsonMeasures.entrySet().forEach(jsonEntry -> connectNodeEdges(jsonEntry, NodeType.MEASURE));
        jsonProductFactors.entrySet().forEach(jsonEntry -> connectNodeEdges(jsonEntry, NodeType.PRODUCT_FACTOR));
        jsonQualityAspects.entrySet().forEach(jsonEntry -> connectNodeEdges(jsonEntry, NodeType.QUALITY_ASPECT));
        jsonTqi.entrySet().forEach(jsonEntry -> connectNodeEdges(jsonEntry, NodeType.TQI));

        // Connect the TQI node to the quality model
        qualityModel.setTqi((Tqi)tqi);

        // With nodes instances, children connected, and configurations assigned to properties, finally return the
        // object.
        return qualityModel;
    }

    /**
     * Parse the quality model file for "children" entries and connect the child node instances using name matching.
     * This method assumes {@link ModelNode} instances already exist for each node described in the quality model file.
     *
     * @param targetNodeJson
     *      The JsonElement representation of the connect node to connect children to.
     * @param nodeType
     *      The family of nodes rootNodeJson belongs to.
     */
    protected void connectNodeEdges(Map.Entry<String, JsonElement> targetNodeJson, NodeType nodeType) {
        String targetNodeName = targetNodeJson.getKey();
        JsonObject targetNodeValues = targetNodeJson.getValue().getAsJsonObject();

        // Make a single map collection of all model nodes (useful later)
        Map<String, ModelNode> allModelNodes = new HashMap<>();
        allModelNodes.put(tqi.getName(), tqi);
        allModelNodes.putAll(qualityAspects);
        allModelNodes.putAll(productFactors);
        allModelNodes.putAll(measures);
        allModelNodes.putAll(diagnostics);

        // If child nodes are explicitly listed in the qm file, directly make the connections
        if (targetNodeValues.get("children") != null) {

            // Put string values of all listed children name in a list
            List<String> childrenNames = new ArrayList<>();
            JsonObject children = targetNodeValues.get("children").getAsJsonObject();
            children.entrySet().forEach(childJsonElement -> childrenNames.add(childJsonElement.getKey()));

            // Add the specified children to the ModelNode
            ModelNode rootNode = allModelNodes.get(targetNodeName);
            childrenNames.forEach(name -> rootNode.setChild(allModelNodes.get(name)));

            //else we are in the compact JSON file, and the children aren't available but the weights contain child information in it.
        } else if (targetNodeValues.get("weights") != null) {
            List<String> childrenNames = new ArrayList<>();

            JsonObject children = targetNodeValues.get("weights").getAsJsonObject();
            children.entrySet().forEach(childJsonElement -> childrenNames.add(childJsonElement.getKey()));

            // Add the specified children to the ModelNode
            ModelNode rootNode = allModelNodes.get(targetNodeName);
            childrenNames.forEach(name -> rootNode.setChild(allModelNodes.get(name)));
        }

        // Otherwise, assume fully connected using the node type below it (e.g. if targetNodeJson is of type
        //      QUALITY_ASPECT, connect all product factor nodes as children).
        else {
            ModelNode targetNode = allModelNodes.get(targetNodeName);
            switch (nodeType) {
                case TQI:               // TQI fully connects to qualtiy aspects
                    targetNode.setChildren(qualityAspects.values());
                    break;
                case QUALITY_ASPECT:   // Quality aspect node fully connects to product factors
                    targetNode.setChildren(productFactors.values());
                    break;
                case PRODUCT_FACTOR:   // Product factor node connects to all measures (this likely should
                                       // never configure to happen)
                    targetNode.setChildren(measures.values());
                    break;
                case MEASURE:         // Measure node connects to all diagnostics (also suggested to not do)
                    targetNode.setChildren(diagnostics.values());
                    break;
                default:
                    throw new RuntimeException("nodeType did not match a support enum.");
            }
        }
    }

    protected IEvaluator getEvaluatorFromConfiguration(JsonObject jsonQmNode, String nodeTypeQm) {
        if (jsonQmNode.get("eval_strategy") != null) {
            String fullClassName = jsonQmNode.get("eval_strategy").getAsString();
            try {
                return (IEvaluator) Class.forName(fullClassName).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        else {
            switch (nodeTypeQm.toLowerCase()) {
                case "diagnostic":
                    return new DefaultDiagnosticEvaluator();
                case "factor":
                case "qualityaspect":
                case "quality_aspect":
                case "quality aspect":
                    return new DefaultFactorEvaluator();
                case "finding":
                    return new DefaultFindingEvaluator();
                case "measure":
                    return new DefaultMeasureEvaluator();
                case "productfactor":
                case "product factor":
                case "product_factor":
                    return new DefaultProductFactorEvaluator();
                default:
                    throw new RuntimeException("Quality Model Importer failed to match node type spelling.");
            }
        }
    }

    protected INormalizer getNormalizerFromConfiguration(JsonObject jsonQmNode) {
        if (jsonQmNode.get("normalizer") != null) {
            String fullClassName = jsonQmNode.get("normalizer").getAsString();
            try {
                return (INormalizer) Class.forName(fullClassName).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        else {
            return new DefaultNormalizer();
        }
    }

    protected BigDecimal[] getThresholdsFromConfiguration(JsonObject jsonQmNode) {
        if (jsonQmNode.get("thresholds") != null && jsonQmNode.get("thresholds").getAsJsonArray().size() > 0) {

            JsonArray jsonThresholds = jsonQmNode.getAsJsonArray("thresholds");
            BigDecimal[] thresholds = new BigDecimal[jsonThresholds.size()];
            for (int i = 0; i < thresholds.length; i++) {
                thresholds[i] = jsonThresholds.get(i).getAsBigDecimal();
            }

            return thresholds;
        }
        else {
            return null;
        }
    }

    protected IUtilityFunction getUtilityFunctionFromConfiguration(JsonObject jsonQmNode) {
        if (jsonQmNode.get("utility_function") != null) {
            String fullClassName = "";
            Object utilFunctionImport = jsonQmNode.get("utility_function");
            // YAY tech debt to ensure backwards compatibility. I hate using instanceof.....
            if (utilFunctionImport instanceof JsonObject){
                fullClassName = ((JsonObject) utilFunctionImport).getAsJsonPrimitive("name").getAsString();
            } else if (utilFunctionImport instanceof JsonElement){
                //redundant element cast potentially, but it is whatever
                fullClassName = ((JsonElement) utilFunctionImport).getAsString();
            }else{
                System.out.println("PROBLEM");
            }
            try {
                return (IUtilityFunction) Class.forName(fullClassName).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        else {
            return new ProbabilityDensityFunctionUtilityFunction();
        }
    }

    protected Map<String, BigDecimal> getWeightsFromConfiguration(JsonObject jsonQmNode) {
        if (jsonQmNode.get("weights") != null) {

            Map<String, BigDecimal> weightNames = new HashMap<>();
            JsonObject jsonWeights = jsonQmNode.getAsJsonObject("weights");
            jsonWeights.entrySet().forEach(jsonWeight -> {
                weightNames.put(jsonWeight.getKey(), jsonWeight.getValue().getAsBigDecimal());
            });

            return weightNames;

        }
        else {
            return null;
        }
    }

    /**
     * Check for 'glocal_config' -> 'benchmark_strategy' and return the benchmark strategy class listed.
     * If configuration does not exist, return a {@link pique.calibration.ProbabilityDensityFunctionBenchmarker}.
     */
    protected IBenchmarker instanceBenchmarkerFromJson() {
        if (jsonQm.getAsJsonObject("global_config") != null && jsonQm.getAsJsonObject("global_config").get("benchmark_strategy") != null) {
            String fullClassName = jsonQm.getAsJsonObject("global_config").get("benchmark_strategy").getAsString();
            try {
                return (IBenchmarker) Class.forName(fullClassName).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        } else {
            return new ProbabilityDensityFunctionBenchmarker();
        }
    }

    /**
     * Example from measuresJson node:
         "diagnostics": {
            "TST0011": {
                "description": "Description for TST0011 diagnostic",
                "toolName": "Test tool"
            },
            "TST0012": {
                "description": "Description for TST0012 diagnostic",
                "toolName": "Test tool"
            }
         }
     */
    protected Map<String, ModelNode> instanceDiagnosticsFromJson(JsonObject diagnosticsJson) {

        Map<String, ModelNode> tempDiagnostics = new HashMap<>();

        diagnosticsJson.entrySet().forEach(entry -> {

            JsonObject jsonDiagnostic = entry.getValue().getAsJsonObject();
            String diagnosticName = entry.getKey();
            String diagnosticDescription = jsonDiagnostic.get("description").getAsString();
            String diagnosticToolName = jsonDiagnostic.get("toolName").getAsString();
            IEvaluator evaluator = getEvaluatorFromConfiguration(jsonDiagnostic, "diagnostic");
            INormalizer normalizer = getNormalizerFromConfiguration(jsonDiagnostic);
            IUtilityFunction utilityFunction = getUtilityFunctionFromConfiguration(jsonDiagnostic);
            Map<String, BigDecimal> weights = getWeightsFromConfiguration(jsonDiagnostic);
            BigDecimal[] thresholds = getThresholdsFromConfiguration(jsonDiagnostic);

            // Instance the diagnostic
            Diagnostic d = new Diagnostic(diagnosticName, diagnosticDescription, diagnosticToolName,
                    evaluator, normalizer, utilityFunction, weights, thresholds);

            // Add to the collection
            if (tempDiagnostics.containsKey(d.getName())) {
                throw new RuntimeException("Two diagnostics with the same name were found while parsing the " +
                        "quality model file.");
            }
            tempDiagnostics.put(d.getName(), d);
        });

        return tempDiagnostics;
    }

    /**
     * Example from measuresJson node:
         "Measure 01": {
            "description": "Measure 01 description",
            "positive": false,
            "children": {
                "TST0011": {},
                "TST0012": {}
            }
         },
         "Measure 02": {
            "description": "Measure 02 description",
            "positive": false,
            "children": {
                "TST0021": {},
                "TST0022": {}
            }
         }
     */
    // TODO (1.0): Update to support any combination of non-default mechanisms
    protected Map<String, ModelNode> instanceMeasuresFromJson(JsonObject measuresJson) {

        Map<String, ModelNode> tempMeasures = new HashMap<>();

        measuresJson.entrySet().forEach(entry -> {
            JsonObject jsonMeasure = entry.getValue().getAsJsonObject();
            String measureName = entry.getKey();
            String measureDescription = jsonMeasure.get("description").getAsString();
            boolean positive = jsonMeasure.getAsJsonPrimitive("positive").getAsBoolean();
            IEvaluator evaluator = getEvaluatorFromConfiguration(jsonMeasure, "measure");
            INormalizer normalizer = getNormalizerFromConfiguration(jsonMeasure);
            IUtilityFunction utilityFunction = getUtilityFunctionFromConfiguration(jsonMeasure);
            Map<String, BigDecimal> weights = getWeightsFromConfiguration(jsonMeasure);
            BigDecimal[] thresholds = getThresholdsFromConfiguration(jsonMeasure);

            // TODO (1.0): Support optional normalizer
            // Instance the measure
            Measure m = new Measure(measureName, measureDescription, evaluator, normalizer, utilityFunction, weights,
                    thresholds, positive);


            // Add to the collection
            if (tempMeasures.containsKey(m.getName())) {
                throw new RuntimeException("Two measures with the same name were found while parsing the " +
                        "quality model file.");
            }
            tempMeasures.put(m.getName(), m);
        });

        return tempMeasures;
    }

    protected String instanceNameFromJson() {
        return jsonQm.getAsJsonPrimitive("name").getAsString();
    }

    /**
     * Example from from productFactorsJson node:
         "product_factors": {
            "ProductFactor 01": {
            "description": "ProductFactor 01 description",
            "children": {
                "Measure 01":{}
            }
         },
         "ProductFactor 02": {
            "description": "ProductFactor 02 description",
            "children": {
                "Measure 02":{}
            }
         }
     */
    // TODO (1.0): Update to support any combination of non-default mechanisms
    protected Map<String, ModelNode> instanceProductFactorsFromJson(JsonObject productFactorsJson) {

        Map<String, ModelNode> tempProductFactors = new HashMap<>();

        productFactorsJson.entrySet().forEach(jsonProductFactor -> {

            JsonObject valueObj = jsonProductFactor.getValue().getAsJsonObject();
            String pfName = jsonProductFactor.getKey();
            String pfDescription = valueObj.get("description").getAsString();
            IEvaluator evaluator = getEvaluatorFromConfiguration(valueObj, "productfactor");
            INormalizer normalizer = getNormalizerFromConfiguration(valueObj);
            IUtilityFunction utilityFunction = getUtilityFunctionFromConfiguration(valueObj);
            Map<String, BigDecimal> weights = getWeightsFromConfiguration(valueObj);
            BigDecimal[] thresholds = getThresholdsFromConfiguration(valueObj);

            // Instance the product factor
            ProductFactor pf = new ProductFactor(pfName, pfDescription, evaluator, normalizer, utilityFunction,
                    weights,thresholds);

            // Add to the collection
            if (tempProductFactors.containsKey(pf.getName())) {
                throw new RuntimeException("Two product factors with the same name were found while parsing the " +
                        "quality model file.");
            }
            tempProductFactors.put(pf.getName(), pf);
        });

        return tempProductFactors;
    }

    /**
     * Example from from qualityAspectsJson node:
     * <p>
     * "quality_aspects": {
     * "QualityAspect 01": {
     * "description": "QualityAspect 01 description"
     * },
     * "QualityAspect 02": {
     * "description": "QualityAspect 02 description"
     * }
     * ...
     * },
     */
    // TODO (1.0): Update to support any combination of non-default mechanisms
    protected Map<String, ModelNode> instanceQualityAspectsFromJson(JsonObject qualityAspectsJson) {

        Map<String, ModelNode> tempQualityAspects = new HashMap<>();

        qualityAspectsJson.entrySet().forEach(entry -> {
            JsonObject valueObj = entry.getValue().getAsJsonObject();
            String qaName = entry.getKey();
            String qaDescription = valueObj.get("description").getAsString();
            IEvaluator evaluator = getEvaluatorFromConfiguration(valueObj, "qualityaspect");
            INormalizer normalizer = getNormalizerFromConfiguration(valueObj);
            IUtilityFunction utilityFunction = getUtilityFunctionFromConfiguration(valueObj);
            Map<String, BigDecimal> weights = getWeightsFromConfiguration(valueObj);
            BigDecimal[] thresholds = getThresholdsFromConfiguration(valueObj);

            // Instance the quality aspect
            QualityAspect qa = new QualityAspect(qaName, qaDescription, evaluator, normalizer, utilityFunction,
             weights, thresholds);

            // Add to the collection
            if (tempQualityAspects.containsKey(qa.getName())) {
                throw new RuntimeException("Two quality aspects with the same name were found while parsing the " +
                        "quality model file.");
            }
            tempQualityAspects.put(qa.getName(), qa);
        });

        return tempQualityAspects;
    }

    /**
     * Example format from tqiJson node:
     * <p>
     * "tqi": {
     * "Total Quality": {
     * "description": "Total Quality description"
     * }
     * },
     * ...
     */
    protected ModelNode instanceTqiFromJson(JsonObject tqiJson) {

        Map.Entry<String, JsonElement> tqiEntry = tqiJson.entrySet().iterator().next();
        JsonObject tqiValues = tqiEntry.getValue().getAsJsonObject();

        String tqiName = tqiEntry.getKey();
        String tqiDescription = tqiValues.get("description").getAsString();
        IEvaluator evaluator = getEvaluatorFromConfiguration(tqiValues, "factor");
        INormalizer normalizer = getNormalizerFromConfiguration(tqiValues);
        IUtilityFunction utilityFunction = getUtilityFunctionFromConfiguration(tqiValues);
        Map<String, BigDecimal> weights = getWeightsFromConfiguration(tqiValues);
        BigDecimal[] thresholds = getThresholdsFromConfiguration(tqiValues);

        return new Tqi(tqiName, tqiDescription, evaluator, normalizer, utilityFunction, weights, thresholds);
    }

    /**
     * Check for 'glocal_config' -> 'weights_strategy' and return the weighter strategy class listed.
     * If configuration does not exist, return a {@link pique.calibration.NaiveWeighter}.
     */
    protected IWeighter instanceWeighterFromJson() {
        if (jsonQm.getAsJsonObject("global_config") != null && jsonQm.getAsJsonObject("global_config").get("weights_strategy") != null) {
            String fullClassName = jsonQm.getAsJsonObject("global_config").get("weights_strategy").getAsString();
            try {
                return (IWeighter) Class.forName(fullClassName).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        } else {
            return new NaiveWeighter();
        }
    }

}
