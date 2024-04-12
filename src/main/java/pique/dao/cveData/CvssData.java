package pique.dao.cveData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CvssData {
    private String version;
    private String vectorString;
    private String accessVector;
    private String accessComplexity;
    private String authentication;
    private String confidentialityImpact;
    private String integrityImpact;
    private String availabilityImpact;
    private Double baseScore;
}