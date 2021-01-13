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
package pique.analysis;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pique.model.Diagnostic;
import pique.utility.FileUtility;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class ToolTests {

    private Path tempTestOutputDirectory = Paths.get("src/test/out");
    private Path testToolLocation = Paths.get("src/test/resources/fake_tools/Test Tool");
    private Tool testTool;

    class ToolInstance extends Tool {
        public ToolInstance(String name, Path toolRoot) { super(name, toolRoot); }
        @Override
        public Path analyze(Path projectLocation) {
            return null;
        }
        @Override
        public Map<String, Diagnostic> parseAnalysis(Path toolResults) {
            return null;
        }
        @Override
        public Path initialize(Path toolRoot) {
            Path toolRootTempFolder = FileUtility.extractResourcesAsIde(tempTestOutputDirectory, toolRoot);
            setToolRoot(Paths.get(toolRootTempFolder.toString(), "Test Tool"));
            return Paths.get(getToolRoot().toString(), "test_tool_file_01.txt");
        }
    }

    @Before
    public void setUp() {
        testTool = new ToolInstance("Test Tool", testToolLocation);
    }

    /**
     * Test construction and private extract to temp resource folder method
     */
    @Test
    public void testToolConstructorWithTempFolderExtraction() {
        Path tempTestTool = testTool.getToolRoot();
        File testToolFile01 = new File(tempTestTool.toString(), "test_tool_file_01.txt");
        File testToolFile02 = new File(tempTestTool.toString(), "test_tool_file_02.txt");

        Assert.assertTrue(tempTestTool.toFile().exists());
        Assert.assertTrue(testToolFile01.exists());
        Assert.assertTrue(testToolFile01.isFile());
        Assert.assertTrue(testToolFile02.exists());
        Assert.assertTrue(testToolFile02.isFile());

    }

    @Test
    public void testGetToolRoot() {
        Path tempToolLocation = testTool.getToolRoot();

        Assert.assertEquals(tempTestOutputDirectory, tempToolLocation.getParent().getParent());
        Assert.assertTrue(tempToolLocation.getFileName().toString().startsWith(testTool.getName()));
    }
}
