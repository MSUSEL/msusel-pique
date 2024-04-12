package pique.dao.cveData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CvssMetricV2 {
    private String source;
    private String type;
    private CvssData cvssData;
    private String baseSeverity;
    private Double exploitabilityScore;
    private Double impactScore;
    private boolean acInsufInfo;
    private boolean obtainAllPrivilege;
    private boolean obtainUserPrivilege;
    private boolean obtainOtherPrivilege;
    private boolean userInteractionRequired;
}