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
import pique.model.QualityModel;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

/**
 * Interface class used by model derivation for benchmarking
 */
// TODO (1.0) update documentation
public interface IBenchmarker {

    /**
     * Primary benchmarking function: Derive thresholds for a collection of {@link pique.model.ModelNode} objects
     *      given a benchmark repository
     *
     * @param benchmarkRepository
     *      The root directory containing the items to be used for benchmarking
     * @param qmDescription
     *      The quality model description file
     * @param tools
     *      The collection of static analysis tools needed to audio the benchmark repository
     * @param projectRootFlag
     *      Option flag to target the static analysis tools
     * @return
     *      A dictionary of [ Key: {@link pique.model.ModelNode} name, Value: thresholds ]
     */
    Map<String, BigDecimal[]> deriveThresholds(Path benchmarkRepository, QualityModel qmDescription, Set<ITool> tools,
                                           String projectRootFlag);

    /**
     * @return An identifiable name of the benchmarker
     */
    String getName();
}
