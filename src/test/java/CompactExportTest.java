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

import org.junit.Before;
import org.junit.Test;
import pique.model.QualityModel;
import pique.model.QualityModelCompactExport;
import pique.model.QualityModelExport;
import pique.model.QualityModelImport;

import java.nio.file.Paths;


public class CompactExportTest {

    protected QualityModel qualityModel;

    /***
     * import an existing model so that I don't have to run everything to prepare the internal data model.
     */
    @Before
    public void importModel(){
        System.out.println(System.getProperty("user.dir"));
        QualityModelImport qualityModelImport = new QualityModelImport(Paths.get("src/test/resources/quality_models/busybox-1.21.1_evalResults.json"));
        this.qualityModel = qualityModelImport.importQualityModel();
    }

    @Test
    public void testCompactExport(){
        // regular output is working.
        QualityModelCompactExport qualityModelCompactExport = new QualityModelCompactExport(qualityModel);
        qualityModelCompactExport.exportToJson("compact_output", Paths.get("src/test/out/"));

        QualityModelExport qualityModelExport = new QualityModelExport(qualityModel);
        qualityModelExport.exportToJson("regular_output", Paths.get("src/test/out/"));
    }

}
