/*
    MIT License

    Copyright (c) 2020 David Scholz

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
 */
package org.dasc.tmp.core.data;

import org.dasc.tmp.api.data.Element;
import org.dasc.tmp.api.graph.IInterval;
import org.dasc.tmp.api.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author David Scholz
 */
public class NodeImpl implements Node {

	private List<Element> elements;
	
	private IInterval interval;

	private List<Node> neighbors = new ArrayList<>();
	
	private Map<String, Object> attributeMap = new HashMap<>();

	private Element first;

	private Element last;
	
	public NodeImpl(final List<Element> elements, final IInterval interval) {
		this.elements = elements;
		this.interval = interval;
		first = elements.get(0);
		last = elements.get(elements.size() - 1);
	}
	
	public NodeImpl() {
		
	}

	public NodeImpl(List<Element> elements) {
		this(elements, null);
	}

	@Override
	public Element first() {
		return first;
	}

	@Override
	public Element last() {
		return last;
	}

	@Override
	public void setInterval(IInterval interval) {
		this.interval = interval;
	}

	@Override
	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	@Override
	public void addNeighbor(Node neighbor) {
		neighbors.add(neighbor);
	}

	@Override
	public IInterval interval() {

		return interval;
	}

	@Override
	public List<Element> elements() {

		return elements;
	}

	@Override
	public List<Node> neighbors() {

		return neighbors;
	}

	@Override
	public void addAttribute(String id, Object obj) {
		attributeMap.put(id, obj);
	}

	@Override
	public Object getAttribute(String id) {

		return attributeMap.get(id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((interval == null) ? 0 : interval.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeImpl other = (NodeImpl) obj;
		if (interval == null) {
			if (other.interval != null)
				return false;
		} else if (!interval.equals(other.interval))
			return false;
		return true;
	}

}
