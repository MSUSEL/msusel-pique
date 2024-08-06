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
package calibrationTests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import pique.calibration.*;

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


	@Test
	public void PDFBenchmarkerTest(){
		AbstractBenchmarker bm = new ProbabilityDensityFunctionBenchmarker();
		Map<String, ArrayList<BigDecimal>> measureBenchmarkData = new HashMap<String,ArrayList<BigDecimal>>();

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
		t1.add(new BigDecimal("10"));

		measureBenchmarkData.put("t1",t1);

		Map<String,BigDecimal[]> results = bm.calculateThresholds(measureBenchmarkData);

		BigDecimal[] temp = results.get("t1");
		Arrays.stream(temp).forEach(System.out::print);

	}
}