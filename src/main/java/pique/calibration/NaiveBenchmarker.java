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

import pique.analysis.ITool;
import pique.evaluation.BenchmarkMeasureEvaluator;
import pique.evaluation.Project;
import pique.model.Diagnostic;
import pique.model.Measure;
import pique.model.QualityModel;
import pique.utility.FileUtility;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class NaiveBenchmarker implements IBenchmarker {
    /**
     * Derive thesholds for all {@link Measure} nodes using a naive approach:
     * (1) threshold[0] = the lowest value seen for the {@link Measure}
     * (2) threshold[1] = the highest value seen for the {@link Measure}
     *
     * @param benchmarkRepository The root directory containing the items to be used for benchmarking
     * @param qmDescription       The quality model description file
     * @param tools               The collection of static analysis tools needed to audio the benchmark repository
     * @param projectRootFlag     Option flag to target the static analysis tools
     * @return A dictionary of [ Key: {@link pique.model.ModelNode} name, Value: thresholds ] where
     * thresholds is a size = 2 array of Double[] containing the lowest and highest value
     * seen for the given measure (after normalization).
     */
    @Override
    public Map<String, Double[]> deriveThresholds(Path benchmarkRepository, QualityModel qmDescription, Set<ITool> tools,
                                                  String projectRootFlag) {

        // Collect root paths of each benchmark project
        Set<Path> projectRoots = FileUtility.multiProjectCollector(benchmarkRepository, projectRootFlag);
        ArrayList<Project> projects = new ArrayList<>();

        System.out.println("* Beginning repository benchmark analysis");
        System.out.println(projectRoots.size() + " projects to analyze.\n");

        int totalProjects = projectRoots.size();
        int counter = 0;

        for (Path projectPath : projectRoots) {

            counter++;

            // Clone the QM
            // TODO (1.0): Currently need to use .clone() for benchmark repository quality model sharing. This will be
            //  confusing and problematic to people not using the default benchmarker.
            QualityModel clonedQM = qmDescription.clone();

            // Instantiate new project object
            Project project = new Project(projectPath.getFileName().toString(), projectPath, clonedQM);

            // TODO: temp fix
            // Set measures to not use a utility function during their node evaluation
            project.getQualityModel().getMeasures().values().forEach(measure -> {
                measure.setEvaluatorObject(new BenchmarkMeasureEvaluator());
            });

            // Run the static analysis tools process
            Map<String, Diagnostic> allDiagnostics = new HashMap<>();
            tools.forEach(tool -> {
                Path analysisOutput = tool.analyze(projectPath);
                allDiagnostics.putAll(tool.parseAnalysis(analysisOutput));
            });

            // Apply collected diagnostics (containing findings) to the project
            allDiagnostics.forEach((diagnosticName, diagnostic) -> {
                project.addFindings(diagnostic);
            });

            // Evaluate project up to Measure level (normalize does happen first)
            project.evaluateMeasures();

            // Add new project (with tool findings information included) to the list
            projects.add(project);

            // Print information
            System.out.println("\n\tFinished analyzing project " + project.getName());
            System.out.println("\t" + counter + " of " + totalProjects + " analyzed.\n");
        }

        // Map all values audited for each measure
        Map<String, ArrayList<Double>> measureBenchmarkData = new HashMap<>();
        projects.forEach(p -> {
            p.getQualityModel().getMeasures().values().forEach(m -> {
                        if (!measureBenchmarkData.containsKey(m.getName())) {
                            measureBenchmarkData.put(m.getName(), new ArrayList<Double>() {{
                                add(m.getValue());
                            }});
                        } else {
                            measureBenchmarkData.get(m.getName()).add(m.getValue());
                        }
                    }
            );
        });

        // Identify the lowest and highest of each measure value
        Map<String, Double[]> measureThresholds = new HashMap<>();
        measureBenchmarkData.forEach((measureName, measureValues) -> {
            measureThresholds.putIfAbsent(measureName, new Double[2]);
            measureThresholds.get(measureName)[0] = Collections.min(measureValues);
            measureThresholds.get(measureName)[1] = Collections.max(measureValues);

        });

        return measureThresholds;
    }

    @Override
    public String getName() {
        return this.getClass().getCanonicalName();
    }
}
