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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pique.analysis.ITool;
import pique.evaluation.BenchmarkMeasureEvaluator;
import pique.evaluation.Project;
import pique.model.Diagnostic;
import pique.model.Measure;
import pique.model.QualityModel;
import pique.utility.FileUtility;

public abstract class AbstractBenchmarker implements IBenchmarker {

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
     * thresholds is a size = 2 array of BigDecimalWithContext[] containing the lowest and highest value
     * seen for the given measure (after normalization).
     */
    @Override
    public Map<String, BigDecimal[]> deriveThresholds(Path benchmarkRepository, QualityModel qmDescription, Set<ITool> tools,
                                                  String projectRootFlag) {

        // Collect root paths of each benchmark project
        Set<Path> projectRoots = this.CollectProjectPaths(benchmarkRepository, projectRootFlag);
        

        System.out.println("* Beginning repository benchmark analysis");
        System.out.println(projectRoots.size() + " projects to analyze.\n");

        ArrayList<Project> projects = analyzeProjects(projectRoots, qmDescription, tools);

        // Map all values audited for each measure
        Map<String, ArrayList<BigDecimal>> measureBenchmarkData = mapMeasureValues(projects);
        
        Map<String, BigDecimal[]> measureThresholds = calculateThresholds(measureBenchmarkData);
        return measureThresholds;
    }
    
    /**
     * Given a set of paths, a qm description, and a set of tools, return a list of projects with the tools run on them.
     * @param projectRoots The paths of the projects to run the tools on
     * @param qmDescription a description of the quality model
     * @param tools A set of tools to run on the projects.
     * @return A list of projects with tools evaluated and findings attached.
     */
    protected ArrayList<Project> analyzeProjects(Set<Path> projectRoots, QualityModel qmDescription, Set<ITool> tools) {
    	ArrayList<Project> projects = new ArrayList<Project>();
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
                measure.setEval_strategyObj(new BenchmarkMeasureEvaluator());
            });

            // Run the static analysis tools process
            Map<String, Diagnostic> allDiagnostics = runToolsOnTarget(tools, projectPath);
            
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
    	return projects;
    }
    
    /**
     * given a set of tools and a Path, run the tools on the Path and return a Map of Strings to Diagnostics with attached
     * findings
     * @param tools The tools to run
     * @param projectPath The Path to run the tools on
     * @return A Map of Strings to Diagnostics that have findings attached.
     */
    protected Map<String, Diagnostic> runToolsOnTarget(Set<ITool> tools, Path projectPath) {
    	Map<String, Diagnostic> allDiagnostics = new HashMap<String,Diagnostic>();
        tools.forEach(tool -> {
            Path analysisOutput = tool.analyze(projectPath);
            Map<String, Diagnostic> parsedOutput = tool.parseAnalysis(analysisOutput);
            if (parsedOutput!=null)  allDiagnostics.putAll(parsedOutput);
            else System.err.println(tool.getName() + " failed to run. Ignoring this and continuing the benchmarking process.");
        });
        return allDiagnostics;
    }

    /**
     * Given a list of projects, create a map of strings to a list of all values that a specific 
     * measure takes on across the projects.
     * @param projects The projects to take measure values from.
     * @return A map that takes measure names and maps them to a list of values that the measure takes on in the projects.
     */
	protected Map<String, ArrayList<BigDecimal>> mapMeasureValues(ArrayList<Project> projects) {
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
        return measureBenchmarkData;
    }
    
    /**
     * Collects the Paths of all projects in benchmark repository
     * @param benchmarkRepository the Path to the root of the benchmark repository
     * @param projectRootFlag Option flag to target the static analysis tools. If blank or null, will use a different method.
     * @return A set of paths of all the projects in the benchmark repository.
     */
    public Set<Path> CollectProjectPaths(Path benchmarkRepository, String projectRootFlag) {
    	Set<Path> projectRoots = null;
    	//Check if projectRootFlag has been set. If null or blank, we are looking for binaries or will accept all non-
    	//directory files.
    	if (projectRootFlag==null || projectRootFlag.equals("")) {
            projectRoots = new HashSet<>();
            File[] binaryFiles = benchmarkRepository.toFile().listFiles();
            for (File file : binaryFiles) {
                if (file.isFile()) {
                    projectRoots.add(file.toPath());
                }
            }
    	}
    	//otherwise, we should use what David originally used for PIQUE-C#
    	else {
    		projectRoots = FileUtility.multiProjectCollector(benchmarkRepository, projectRootFlag);
    	}
    	 return projectRoots;
    }

    /**
     * Given the results of the analysis of the benchmark repository analysis, calculate the thresholds for each measure.
     * @param measureBenchmarkData the data of the application of tools to the benchmark repository.
     * @return the thresholds for each measure.
     */
    public abstract Map<String, BigDecimal[]> calculateThresholds(Map<String, ArrayList<BigDecimal>> measureBenchmarkData);

	@Override
    public String getName() {
        return this.getClass().getCanonicalName();
    }
}
