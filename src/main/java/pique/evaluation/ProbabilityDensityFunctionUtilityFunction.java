package pique.evaluation;

import java.math.BigDecimal;

public class ProbabilityDensityFunctionUtilityFunction extends UtilityFunction{


    public ProbabilityDensityFunctionUtilityFunction(String name, String description) {
        super(name, description);
    }

    @Override
    public BigDecimal utilityFunction(BigDecimal inValue, BigDecimal[] thresholds, boolean positive) {
        //Redempta TODO
        return null;
    }
}
