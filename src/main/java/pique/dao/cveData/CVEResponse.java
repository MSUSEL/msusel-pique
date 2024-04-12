package pique.dao.cveData;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CVEResponse {
    private int resultsPerPage;
    private int startIndex;
    private int totalResults;
    private String format;
    private String version;
    private String timestamp;
    private ArrayList<Vulnerability> vulnerabilities;
}
