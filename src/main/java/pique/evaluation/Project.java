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
package pique.evaluation;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import pique.model.*;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Map;

/**
 * This class represents a project under evaluation.
 */

public class Project{

	// Fields

	@Getter @Setter
	private String name;
	@Getter @Setter
	private Path path;  // the original path where the sources of the project are stored (with or without the name)
	@Getter @Setter
	private QualityModel qualityModel;  // the QM prototype this project uses for evaluation

	// Constructors

	// TODO (1.0): Currently need to use .clone() for benchmark repository quality model sharing. This will be
	//  confusing and problematic to people not using the default benchmarker.
	public Project(String name){
		this.name = name;
	}

	public Project(String name, QualityModel qm) {
		this.name = name;
		this.qualityModel = qm;
	}

	public Project(String name, Path path, QualityModel qm) {
		this.name = name;
		this.path = path;
		this.qualityModel = qm;
	}

	// Methods

	/**
	 * Search through diagnostics associated with this project and update the findings of the diagnostic
	 * with the diagnostic resulting from a tool analysis run.
	 *
	 * Search time complexity is O(n) where n is number of diagnostics in the project.
	 *
	 * @param toolResult
	 * 		A diagnostic object parsed from the tool result file
	 */
	// TODO (1.0): Currently makes assumption that all product factors have exactly 1 connected measure
	public void addFindings(Diagnostic toolResult) {

		for (ModelNode measure : getQualityModel().getMeasures().values()) {
			for (ModelNode diagnostic : measure.getChildren().values()) {
				if (diagnostic.getName().equals(toolResult.getName())) {
					diagnostic.setChildren(toolResult.getChildren().values());
				}
			}
		}
	}

	public void evaluateMeasures() {
		getQualityModel().getMeasures().values().forEach(ModelNode::getValue);
	}

	/**
	 * Evaluate and set this project's characteristics using the weights
	 * provided by the quality model and the values contained in the project's ProductFactor nodes.
	 */
	public void evaluateCharacteristics() {
		getQualityModel().getQualityAspects().values().forEach(ModelNode::getValue);
	}


	/**
	 * Evaluate and set this project's properties using the thresholds
	 * provided by the quality model and the findings contained in the Measure nodes.
	 */
	public void evaluateProperties() {
		getQualityModel().getProductFactors().values().forEach(ModelNode::getValue);
	}


	public BigDecimal evaluateTqi() {
		return getQualityModel().getTqi().getValue();
	}


	/**
	 * Create a hard-drive file representation of the project.
	 *
	 * @param resultsDir
	 * 		The directory to place the project representation file into.  Does not need to exist beforehand.
	 * @return
	 * 		The path of the project json file.
	 */
	public Path exportToJson(Path resultsDir) {
		return exportToJson(resultsDir, false);
	}

	public Path exportToJson(Path resultsDir, boolean compactExport){
		String fileName = this.getName() + "_evalResults";
		//Pair<String, String> loc = Pair.of("projectLinesOfCode", String.valueOf(getLinesOfCode()));
		Pair<String, String> name = Pair.of("projectName", getName());

		QualityModelExport qmExport;
		if (compactExport){
			fileName = this.getName() + "_compact_evalResults";
			qmExport = new QualityModelCompactExport(getQualityModel(), name);
		}else{
			qmExport = new QualityModelExport(getQualityModel(), name);
		}
		return qmExport.exportToJson(fileName, resultsDir);
	}


	/**
	 * Find the diagnostic objects in this project and update their findings with findings containing in
	 * the input parameter.  This method matches the diagnostic objects by name.
	 *
	 * @param diagnosticsWithFindings
	 * 		A map {Key: diagnostic name, Value: diagnostic object} of diagnostics contaning > 0 values of
	 * 		findings.
	 */
	public void updateDiagnosticsWithFindings(Map<String, Diagnostic> diagnosticsWithFindings) {
		diagnosticsWithFindings.values().forEach(diagnostic -> {
			getQualityModel().getMeasures().values().forEach(measure -> {
				measure.getChildren().values().forEach(oldDiagnostic -> {
					if (oldDiagnostic.getName().equals(diagnostic.getName())) {
						oldDiagnostic.setChildren(diagnostic.getChildren());
					}
				});
			});
		});
	}
}
