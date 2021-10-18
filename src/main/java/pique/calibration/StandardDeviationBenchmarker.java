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

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pique.analysis.ITool;
import pique.calibration.IBenchmarker;
import pique.evaluation.BenchmarkMeasureEvaluator;
import pique.evaluation.Project;
import pique.model.Diagnostic;
import pique.model.Measure;
import pique.model.QualityModel;

public class StandardDeviationBenchmarker implements IBenchmarker {
    /**
     * Derive thesholds for all {@link Measure} nodes using a mean +- standard deviation approach:
     * (1) threshold[0] = the mean value minus the standard deviation for the {@link Measure}
     * (2) threshold[1] = the mean value plus the standard deviation for the {@link Measure}
     *
     * @param benchmarkRepository The root directory containing the items to be used for benchmarking
     * @param qmDescription       The quality model description file
     * @param tools               The collection of static analysis tools needed to audio the benchmark repository
     * @param projectRootFlag     Option flag to target the static analysis tools, not used in binary case
     * @return A dictionary of [ Key: {@link pique.model.ModelNode} name, Value: thresholds ] where
     * thresholds is a size = 2 array of BigDecimal[] containing the calculated thresholds.
     */
    @Override
    public Map<String, BigDecimal[]> deriveThresholds(Path benchmarkRepository, QualityModel qmDescription, Set<ITool> tools,
                                                      String projectRootFlag) {

        // Collect benchmark binaries
        Set<Path> projectRoots = new HashSet<>();
        File[] binaryFiles = benchmarkRepository.toFile().listFiles();
        for (File file : binaryFiles) {
            if (file.isFile()) {
                projectRoots.add(file.toPath());
            }
        }
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

            // Would normalize here if we do so in the future

            // Apply collected diagnostics (containing findings) to the project
            allDiagnostics.forEach((diagnosticName, diagnostic) -> {
                project.addFindings(diagnostic);
            });

            // Evaluate project up to Measure level
            project.evaluateMeasures();

            // Add new project (with tool findings information included) to the list
            projects.add(project);

            // Print information
            System.out.println("\n\tFinished analyzing project " + project.getName());
            System.out.println("\t" + counter + " of " + totalProjects + " analyzed.\n");
        }

        // Map all values audited for each measure
        Map<String, ArrayList<BigDecimal>> measureBenchmarkData = new HashMap<>();
        projects.forEach(p -> {
            p.getQualityModel().getMeasures().values().forEach(m -> {
                    if (!measureBenchmarkData.containsKey(m.getName())) {
                        measureBenchmarkData.put(m.getName(), new ArrayList<BigDecimal>() {{
                            add(m.getValue());
                        }});
                    } else {
                        measureBenchmarkData.get(m.getName()).add(m.getValue());
                    }
                }
            );
        });


        // Identify the mean+-sd of each measure value
        Map<String, BigDecimal[]> measureThresholds = new HashMap<>();
        measureBenchmarkData.forEach((measureName, measureValues) -> {
            BigDecimal[] values = new BigDecimal[2];

            values[0] = mean(measureValues).subtract(calculateSD(measureValues));
            values[1] = mean(measureValues).add(calculateSD(measureValues));

            if (values[0].compareTo(new BigDecimal("0.0")) < 0) { //measureThresholds.get(measureName)[0] < 0.0
                values[0] = new BigDecimal("0.0");
            }
            measureThresholds.put(measureName, values);
        });

        return measureThresholds;
    }

    /**
     * Take mean of a BigDecimal ArrayList
     * @param measureValues The ArrayList<BigDecimal> to take the mean of
     * @return mean value of the passed parameter
     */
    private static BigDecimal mean(ArrayList<BigDecimal> measureValues) {
        BigDecimal sum = new BigDecimal("0.0");
        for (int i = 0; i < measureValues.size(); i++) {
            sum = sum.add(measureValues.get(i));
        }
        return sum.divide(new BigDecimal(""+measureValues.size()));
    }

    /**
     * Finds the percentiles of an ArrayList<BigDecimal>. 
     * @param values The values in which to find percentiles
     * @param percentiles The desired percentiles; i.e. [0.25,0.5,0.75] will return the 25th, 50th, and 75th percentiles.
     * @return the percentiles of the passed values, as specified by the percentiles passed in
     */
    private static BigDecimal[] getPercentiles(ArrayList<BigDecimal> values, BigDecimal[] percentiles) {
        BigDecimal[] tempVals= new BigDecimal[values.size()];
        tempVals = values.toArray(tempVals);
        Arrays.sort(tempVals, 0, tempVals.length);
        for (int i = 0; i < percentiles.length; i++) {
            int index =  percentiles[i].multiply(new BigDecimal(""+tempVals.length)).intValue(); //could cause an issue.
            percentiles[i] = tempVals[index];
        }

        return percentiles;
    }

    /**
     * Calculates the standard deviation of an ArrayList<BigDecimal>.
     * @param measureValues values of BigDecimals
     * @return Standard deviation of the passed BigDecimals
     */
    private static BigDecimal calculateSD(ArrayList<BigDecimal> measureValues)
    {
        BigDecimal sum = new BigDecimal("0.0"), standardDeviation = new BigDecimal("0.0");
        int length = measureValues.size();

        for(BigDecimal num : measureValues) {
            sum.add(num);
        }

        BigDecimal mean = sum.divide(new BigDecimal(""+length));

        for(BigDecimal num: measureValues) {
            standardDeviation.add(num.subtract(mean).pow(2));
        }

        return (standardDeviation.divide(new BigDecimal(length)));
    }

    @Override
    public String getName() {
        return this.getClass().getCanonicalName();
    }
}