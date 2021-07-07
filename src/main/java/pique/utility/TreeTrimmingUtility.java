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

import java.util.HashMap;
import java.util.Map;

import pique.model.ModelNode;
import pique.model.QualityModel;
import pique.model.Tqi;


/**
 * 
 * @author Andrew Johnson
 * 
 * The purpose of this utility class is to provide a function to trim measures with no associated diagnostics,
 * and product factors with no associated measures. This allows us to maintain the entire structure of a CWE view
 * without needing diagnostics for every measure, and allows the model to ignore those nodes which will never have 
 * findings.
 */
public class TreeTrimmingUtility {
	
	/**
	 * removes measures with no children and product factors with no children.
	 * @param qm The QualityModel to trim
	 * @return the trimmed qm
	 */
    public static QualityModel trimQualityModelTree(QualityModel qm) {
    	ModelNode tqi = qm.getTqi();
    	Map<String, ModelNode> qas = tqi.getChildren();
    	Map<String, ModelNode> pfs = new HashMap<String,ModelNode>();
    	for (ModelNode x : qas.values()) {
    		pfs.putAll(x.getChildren());
    	}
    	
    	for (ModelNode x : pfs.values()) {
    		x = trimNode(x);
    	}
    	for (ModelNode x : qas.values()) {
    		x = trimNode(x);
    	}
    	qm.setTqi((Tqi) tqi);
    	return qm;
	}
    
    //for a given node, remove any children that have no children of their own
    private static ModelNode trimNode(ModelNode node) {
    	Map<String, ModelNode> children = node.getChildren();
    	Map<String,ModelNode> newChildren = new HashMap<String,ModelNode>();
    	for (ModelNode x : children.values()) {
    		if (x.getNumChildren() > 0) {
    			newChildren.put(x.getName(), x);
    		}
    	}
    	node.setChildren(newChildren);
    	return node;
    	
    }
}
