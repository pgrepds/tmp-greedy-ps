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

import org.springframework.beans.factory.annotation.Autowired;
import org.dasc.tmp.api.data.Element;
import org.dasc.tmp.api.data.Sequence;
import org.dasc.tmp.api.graph.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author David Scholz
 */
public class GraphFactoryImpl implements GraphFactory {

	@Autowired
	private NodeFactory nodeFactory;

	@Override
	public Graph getUndirectedGraph() {

		return new UndirectedGraph();
	}

	@Override
	public Graph createIntervalGraph(Sequence sequence, List<IInterval> intervals) {

		Graph undirectedGraph = new UndirectedGraph();

		Map<IInterval, Node> intervalToNodeMap = new HashMap<>();

		// adding nodes
		for (Integer destination : sequence.destinations()) {

			List<Element> elements = sequence.elementsOfDestination(destination);
			IInterval intervalForDestination = getIntervalForDestination(destination, intervals);
			Node node = nodeFactory.newNode(elements, intervalForDestination);
			undirectedGraph.addNode(node);
			intervalToNodeMap.put(intervalForDestination, node);
		}

		// creating edges
		for (IInterval interval1 : intervals) {

			for (IInterval interval2 : intervals) {

				if (interval1.equals(interval2)) {
					continue;
				}
				
				if (interval1.overlaps(interval2)) {
					Node node1 = intervalToNodeMap.get(interval1);
					Node node2 = intervalToNodeMap.get(interval2);

					Edge edge = EdgeImpl.edge(node1, node2);
					node1.addNeighbor(node2);
					node2.addNeighbor(node1);
					undirectedGraph.addEdge(node1, node2);
				}

			}
		}

		return undirectedGraph;
	}

	private IInterval getIntervalForDestination(Integer destination, List<IInterval> intervals) {

		IInterval result = null;
		for (IInterval interval : intervals) {
			if (interval.name().equals("" + destination)) {
				result = interval;
				break;
			}
		}

		return result;
	}

}
