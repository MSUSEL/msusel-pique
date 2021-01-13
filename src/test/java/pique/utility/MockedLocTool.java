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

public class MockedLocTool implements ITool {

    @Override
    public Path analyze(Path projectLocation) {
        return Paths.get("src/test/resources/tool_results/faketool_loc_output.txt");
    }

    @Override
    public Map<String, Diagnostic> parseAnalysis(Path toolResults) {
        Map<String, Diagnostic> diagnostics = new HashMap<>();
        Finding f1 = new Finding("no/path", 0, 0, 100);
        Diagnostic locDiagnostic = new Diagnostic("loc", "loc diagnostic description", this.getName(),
                new DefaultDiagnosticEvaluator());
        locDiagnostic.setChild(f1);
        diagnostics.put(locDiagnostic.getName(), locDiagnostic);
        return diagnostics;
    }

    @Override
    public Path initialize(Path toolRoot) {
        return null;
    }

    @Override
    public String getName() {
        return "Fake LoC Tool";
    }
}
