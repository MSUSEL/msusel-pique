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
package pique.utility;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * This is a BigDecimal extension that automatically sets the precision of the BigDecimal using a MathContext object.
 * Uses HALF_UP rounding mode.
 * @author Andrew
 *
 *
 */
public class BigDecimalWithContext extends BigDecimal{

	private static final Integer precision=25;
	public static final MathContext mc = new MathContext(precision,RoundingMode.HALF_UP);
	
	public BigDecimalWithContext(int x) {
		super(""+x,mc);
	}
	
	public BigDecimalWithContext(double x) {
		super(""+x,mc);
	}
	
	public BigDecimalWithContext(String x) {
		super(x,mc);
	}
	
	public static MathContext getMC() {
		return mc;
	}


}
