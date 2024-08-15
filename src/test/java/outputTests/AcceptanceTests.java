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

import org.junit.Before;
import org.junit.Test;
import pique.model.QualityModel;
import pique.model.QualityModelCompactExport;
import pique.model.QualityModelImport;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class AcceptanceTests {

    public AcceptanceTests(){

    }

    @Before
    public void loadTestFileFromExistingSources(){
        QualityModelImport importer = new QualityModelImport(Paths.get("src/test/resources/quality_models/busybox-1.21.1_evalResults.json"));
        QualityModel model = importer.importQualityModel();

        QualityModelCompactExport compactExport = new QualityModelCompactExport(model);
        compactExport.exportToJson("busybox-1.21.1_evalResults-out", Paths.get("src/test/resources/quality_models"));
    }

    @Before
    public void loadPDFTestFileFromExistingSources(){
        QualityModelImport importer = new QualityModelImport(Paths.get("src/test/resources/quality_models/busybox-1.21.1_evalResults-pdf-utilityfunc.json"));
        QualityModel model = importer.importQualityModel();

        QualityModelCompactExport compactExport = new QualityModelCompactExport(model);
        compactExport.exportToJson("busybox-1.21.1_evalResults-pdf-utilityfunc-out", Paths.get("src/test/resources/quality_models/"));
    }

    @Test
    public void basicAcceptanceTest(){
        QualityModelImport importer = new QualityModelImport(Paths.get("src/test/resources/quality_models/busybox-1.21.1_evalResults-out.json"));
        QualityModel model = importer.importQualityModel();

        //can add more tests as necessary, basically if we can get this far that means something correct happened.
        assertEquals("Binary Security Quality", model.getTqi().getName());
    }

    @Test
    public void acceptanceTestWithDefaultUtilityToPDFUtilityFunc(){
        QualityModelImport importer = new QualityModelImport(Paths.get("src/test/resources/quality_models/busybox-1.21.1_evalResults-pdf-utilityfunc-out.json"));
        QualityModel model = importer.importQualityModel();

        //can add more tests as necessary, basically if we can get this far that means something correct happened.
        assertEquals("Binary Security Quality", model.getTqi().getName());
    }
}
