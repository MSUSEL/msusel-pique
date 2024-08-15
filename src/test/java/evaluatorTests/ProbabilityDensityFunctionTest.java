package evaluatorTests;

import org.junit.Test;
import pique.calibration.AbstractBenchmarker;
import pique.calibration.ProbabilityDensityFunctionBenchmarker;
import pique.evaluation.ProbabilityDensityFunctionUtilityFunction;
import pique.utility.BigDecimalWithContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProbabilityDensityFunctionTest {

    private ProbabilityDensityFunctionBenchmarker probabilityDensityFunctionBenchmarker;
    private ProbabilityDensityFunctionUtilityFunction probabilityDensityFunctionUtilityFunction;

    public ProbabilityDensityFunctionTest(){
        probabilityDensityFunctionBenchmarker = new ProbabilityDensityFunctionBenchmarker();
        probabilityDensityFunctionUtilityFunction = new ProbabilityDensityFunctionUtilityFunction();
    }

    @Test
    public void endToEndUtilitizingTest(){
        //perform benchmarking
        Map<String, ArrayList<BigDecimal>> measureBenchmarkData = new HashMap<>();

        ArrayList<BigDecimal> t1 = new ArrayList<>();
        t1.add(new BigDecimal("0"));
        t1.add(new BigDecimal("1"));
        t1.add(new BigDecimal("2"));
        t1.add(new BigDecimal("3"));
        t1.add(new BigDecimal("4"));
        t1.add(new BigDecimal("5"));
        t1.add(new BigDecimal("6"));
        t1.add(new BigDecimal("7"));
        t1.add(new BigDecimal("8"));
        t1.add(new BigDecimal("9"));
        t1.add(new BigDecimal("40"));
        measureBenchmarkData.put("t1",t1);

        Map<String,BigDecimal[]> thresholds = probabilityDensityFunctionBenchmarker.calculateThresholds(measureBenchmarkData);
        BigDecimal[] t1Thresholds = thresholds.get("t1");
        Arrays.stream(t1Thresholds).forEach(System.out::println);

        //perform utilitizing
        BigDecimal inValue = new BigDecimalWithContext(100.0);

        BigDecimal score = probabilityDensityFunctionUtilityFunction.utilityFunction(inValue, t1Thresholds, false);
        System.out.println(score);


    }
}
