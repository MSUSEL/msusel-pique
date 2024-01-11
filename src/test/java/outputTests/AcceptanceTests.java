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
