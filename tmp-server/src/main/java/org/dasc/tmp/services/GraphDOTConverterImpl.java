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
package org.dasc.tmp.services;

import org.dasc.tmp.api.Pseudochain;
import org.dasc.tmp.api.graph.Edge;
import org.dasc.tmp.api.graph.Graph;
import org.dasc.tmp.api.graph.Node;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author David Scholz
 */
public class GraphDOTConverterImpl implements GraphDOTConverter {

    @Override
    public String toDot(Graph graph) {


        StringBuilder sb = new StringBuilder();

        for (Node n : graph.nodes()) {

            sb.append(n.first().destination());
            sb.append(";");
        }

        Set<Edge> exitent = new HashSet<>();
        for (Edge e : graph.edges()) {

            if (exitent.contains(e)) {

                continue;
            }

            exitent.add(e);

            sb.append(e.firstNode().first().destination() + " -- " + e.secNode().first().destination());
            sb.append(";");
        }

        return sb.toString();
    }

    @Override
    public String toDot(Pseudochain ps) {

        Graph graph = ps.getGraph();

        StringBuilder sb = new StringBuilder();

        List<List<Node>> independentSets = ps.getIndependentSets();

        for (List<Node> set : independentSets) {

            for (Node n : set) {

                sb.append(n.first().destination());
                sb.append(";");

                List<Node> neighbors = graph.getNeighbors(n);

                if (neighbors != null) {
                    for (Node neighbor : neighbors) {
                        sb.append(n.first().destination() + " -- " + neighbor.first().destination());
                        sb.append(";");
                    }
                }
            }
        }

        return sb.toString();
    }

}
