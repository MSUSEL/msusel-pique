package pique.calibration;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pique.analysis.ITool;
import pique.evaluation.BenchmarkMeasureEvaluator;
import pique.evaluation.Project;
import pique.model.Diagnostic;
import pique.model.Measure;
import pique.model.QualityModel;
import pique.utility.BigDecimalWithContext;
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
                Map<String, Diagnostic> parsedOutput = tool.parseAnalysis(analysisOutput);
                if (parsedOutput!=null)  allDiagnostics.putAll(parsedOutput);
                else System.err.println(tool.getName() + " failed to run. Ignoring this and continuing the benchmarking process.");
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
        
        Map<String, BigDecimal[]> measureThresholds = calculateThresholds(measureBenchmarkData);
        return measureThresholds;
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
    	if (projectRootFlag==null ||projectRootFlag.equals("")) {
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
