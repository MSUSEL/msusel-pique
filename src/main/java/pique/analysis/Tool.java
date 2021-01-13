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

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

/**
 * Abstract object representation of an external static analysis tool.
 */
public abstract class Tool implements ITool {

    // Instance vars

    @Getter
    private String name;
    @Getter
    private Path executable;
    @Getter @Setter
    private Path toolRoot;


    // Constructor

    /**
     * On construction of any tool, Qatch will copy the tool files inside of the language-specific Qatch run into
     * a temporary folder named 'tools'.  This handles problems with accessing executables inside of JAR files.
     *
     * @param name
     *      The tool name
     * @param toolRoot
     *      The initial location of this tool's root folder
     */
    public Tool(String name, Path toolRoot) {
        this.name = name;
        this.toolRoot = toolRoot;
        this.executable = initialize(toolRoot);
    }


    // Methods

    @Override
    public abstract Path initialize(Path toolRoot);
}
