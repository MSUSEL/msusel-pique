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
package outputTests;

import org.junit.Test;
import pique.model.QualityModel;
import pique.model.QualityModelCompactExport;
import pique.model.QualityModelImport;

import java.nio.file.Paths;

public class AcceptanceTests {

    public AcceptanceTests(){

    }

    @Test
    public void basicAcceptanceTest(){
        QualityModelImport importer = new QualityModelImport(Paths.get("src/test/resources/quality_models/busybox-1.21.1_evalResults.json"));
        QualityModel model = importer.importQualityModel();

        QualityModelCompactExport compactExport = new QualityModelCompactExport(model);
        compactExport.exportToJson("acceptance-test-output.json", Paths.get("src/test/out"));
    }

    @Test
    public void acceptanceTestWithDefaultUtilityToPDFUtilityFunc(){
        QualityModelImport importer = new QualityModelImport(Paths.get("src/test/resources/quality_models/busybox-1.21.1_evalResults-pdf-utilityfunc.json"));
        QualityModel model = importer.importQualityModel();

        QualityModelCompactExport compactExport = new QualityModelCompactExport(model);
        compactExport.exportToJson("acceptance-test-output-pdf-util.json", Paths.get("src/test/out"));
    }
}
