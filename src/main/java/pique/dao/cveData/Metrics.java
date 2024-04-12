package pique.dao.cveData;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Metrics {
    private ArrayList<CvssMetricV2> cvssMetricV2;
}