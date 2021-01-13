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
package pique.utility;

import pique.analysis.ITool;
import pique.evaluation.DefaultDiagnosticEvaluator;
import pique.model.Diagnostic;
import pique.model.Finding;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Concrete class implementing ITool that mocks static analysis.
 */
public class MockedIToolQmSimple implements ITool {

    @Override
    public Path analyze(Path projectLocation) {
        switch (projectLocation.getFileName().toString()) {
            case "BenchmarkProjectOne":
                return Paths.get("src/test/resources/tool_results/benchmark_one_results.xml");
            case "BenchmarkProjectTwo":
                return Paths.get("src/test/resources/tool_results/benchmark_two_results.xml");
            case "BenchmarkProjectThree":
                return Paths.get("src/test/resources/tool_results/benchmark_three_results.xml");
            case "fake_project":
                return Paths.get("src/test/resources/tool_results/mocked_itool_project_output.txt");
            default:
                throw new RuntimeException("switch statement default case");
        }
    }

    @Override
    public Map<String, Diagnostic> parseAnalysis(Path toolResults) {
        Finding f1 = Builder.buildFinding("file/path/one", 11, 1);
        Finding f2 = Builder.buildFinding("file/path/two", 22, 2);
        Finding f3 = Builder.buildFinding("file/path/three", 33, 3);

        Map<String, Diagnostic> diagnostics = new HashMap<>();
        switch (toolResults.getFileName().toString()) {
            case "benchmark_one_results.xml":

                Diagnostic bench1tst1 = new Diagnostic("TST0011", "Sample Description", "Sample Tool Name",
                        new DefaultDiagnosticEvaluator());
                Diagnostic bench1tst2 = new Diagnostic("TST0012", "Sample Description", "Sample Tool Name",
                        new DefaultDiagnosticEvaluator());

                bench1tst1.setChild(f1);
                bench1tst2.setChild(f1);

                diagnostics.put(bench1tst1.getName(), bench1tst1);
                diagnostics.put(bench1tst2.getName(), bench1tst2);

                return diagnostics;

            case "benchmark_two_results.xml":
                Diagnostic bench2tst1 = new Diagnostic("TST0011", "Sample Description", "Sample Tool Name",
                        new DefaultDiagnosticEvaluator());
                Diagnostic bench2tst2 = new Diagnostic("TST0012", "Sample Description", "Sample Tool Name",
                        new DefaultDiagnosticEvaluator());
                Diagnostic bench2tst3 = new Diagnostic("TST0021", "Sample Description", "Sample Tool Name",
                        new DefaultDiagnosticEvaluator());
                Diagnostic bench2tst4 = new Diagnostic("TST0022", "Sample Description", "Sample Tool Name",
                        new DefaultDiagnosticEvaluator());

                bench2tst1.setChild(f1);
                bench2tst2.setChild(f1);
                bench2tst3.setChild(f1);
                bench2tst4.setChild(f1);

                diagnostics.put(bench2tst1.getName(), bench2tst1);
                diagnostics.put(bench2tst2.getName(), bench2tst2);
                diagnostics.put(bench2tst3.getName(), bench2tst3);
                diagnostics.put(bench2tst4.getName(), bench2tst4);

                return diagnostics;

            case "benchmark_three_results.xml":
                Diagnostic bench3tst1 = new Diagnostic("TST0011", "Sample Description", "Sample Tool Name",
                        new DefaultDiagnosticEvaluator());
                Diagnostic bench3tst2 = new Diagnostic("TST0012", "Sample Description", "Sample Tool Name",
                        new DefaultDiagnosticEvaluator());
                Diagnostic bench3tst3 = new Diagnostic("TST0021", "Sample Description", "Sample Tool Name",
                        new DefaultDiagnosticEvaluator());
                Diagnostic bench3tst4 = new Diagnostic("TST0022", "Sample Description", "Sample Tool Name",
                        new DefaultDiagnosticEvaluator());

                bench3tst1.setChildren(Stream.of(f1, f2).collect(Collectors.toSet()));
                bench3tst2.setChildren(Stream.of(f1, f2).collect(Collectors.toSet()));
                bench3tst3.setChildren(Stream.of(f1, f2).collect(Collectors.toSet()));
                bench3tst4.setChildren(Stream.of(f1, f2).collect(Collectors.toSet()));

                diagnostics.put(bench3tst1.getName(), bench3tst1);
                diagnostics.put(bench3tst2.getName(), bench3tst2);
                diagnostics.put(bench3tst3.getName(), bench3tst3);
                diagnostics.put(bench3tst4.getName(), bench3tst4);

                return diagnostics;

            case "mocked_itool_project_output.txt":
                Diagnostic diagnostic11 = new Diagnostic("TST0011", "Sample Description", "Sample Tool Name",
                        new DefaultDiagnosticEvaluator());
                Diagnostic diagnostic12 = new Diagnostic("TST0012", "Sample Description", "Sample Tool Name",
                        new DefaultDiagnosticEvaluator());
                Diagnostic diagnostic21 = new Diagnostic("TST0021", "Sample Description", "Sample Tool Name",
                        new DefaultDiagnosticEvaluator());

                diagnostic11.setChild(f1);
                diagnostic12.setChild(f1);
                diagnostic12.setChild(f3);
                diagnostic21.setChild(f1);
                diagnostic21.setChild(f1);

                diagnostics.put(diagnostic11.getName(), diagnostic11);
                diagnostics.put(diagnostic12.getName(), diagnostic12);
                diagnostics.put(diagnostic21.getName(), diagnostic21);

                return diagnostics;

            default:
                throw new RuntimeException("switch statement default case");
        }
    }

    @Override
    public Path initialize(Path toolRoot) {
        return null;
    }

    @Override
    public String getName() {
        return "Deriver Test Tool";
    }
}
