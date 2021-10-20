package calibrationTests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import pique.calibration.AdvancedBenchmarker;
import pique.calibration.IBenchmarker;
import pique.calibration.NaiveBenchmarker;

public class BenchmarkerTests {


	@Test
	public void NaiveBenchmarkTest() {
		NaiveBenchmarker bm = new NaiveBenchmarker();
		Map<String, ArrayList<BigDecimal>> measureBenchmarkData = new HashMap<String,ArrayList<BigDecimal>>();

		ArrayList<BigDecimal> t1 = new ArrayList<BigDecimal>();
		t1.add(new BigDecimal("0"));
		t1.add(new BigDecimal("1"));
		t1.add(new BigDecimal("2"));
		t1.add(new BigDecimal("3"));
		
		ArrayList<BigDecimal> t2 = new ArrayList<BigDecimal>();
		t2.add(new BigDecimal("3"));
		t2.add(new BigDecimal("2"));
		t2.add(new BigDecimal("2"));
		t2.add(new BigDecimal("4"));
		
		
		ArrayList<BigDecimal> t3 = new ArrayList<BigDecimal>();
		t3.add(new BigDecimal("-3"));
		t3.add(new BigDecimal("2"));
		t3.add(new BigDecimal("2"));
		t3.add(new BigDecimal("4"));

		measureBenchmarkData.put("t1", t1);
		measureBenchmarkData.put("t2", t2);
		measureBenchmarkData.put("t3", t3);
		
		Map<String,BigDecimal[]> results = bm.calculateThresholds(measureBenchmarkData);
		
		BigDecimal[] temp = results.get("t1");

		assert(temp[0].compareTo(new BigDecimal("0"))==0);
		assert(temp[1].compareTo(new BigDecimal("3"))==0);
		
		temp = results.get("t2");

		assert(temp[0].compareTo(new BigDecimal("2"))==0);
		assert(temp[1].compareTo(new BigDecimal("4"))==0);
		
		temp = results.get("t3");

		assert(temp[0].compareTo(new BigDecimal("-3"))==0);
		assert(temp[1].compareTo(new BigDecimal("4"))==0);
		
	}
	
	@Test
	public void AdvancedBenchmarkTest() {
		AdvancedBenchmarker bm = new AdvancedBenchmarker();
		Map<String, ArrayList<BigDecimal>> measureBenchmarkData = new HashMap<String,ArrayList<BigDecimal>>();

		ArrayList<BigDecimal> t1 = new ArrayList<BigDecimal>();
		t1.add(new BigDecimal("0"));
		t1.add(new BigDecimal("1"));
		t1.add(new BigDecimal("2"));
		t1.add(new BigDecimal("3"));
		
		measureBenchmarkData.put("t1",t1);
		
		Map<String,BigDecimal[]> results = bm.calculateThresholds(measureBenchmarkData);
		
		BigDecimal[] temp = results.get("t1");
		
		for (BigDecimal val : temp) {
			assert(t1.contains(val));
		}
	}
}
