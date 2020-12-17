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

import org.dasc.tmp.api.graph.Edge;
import org.dasc.tmp.api.graph.Graph;
import org.dasc.tmp.api.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author David Scholz
 */
public class UndirectedGraph implements Graph {

    private Map<Node, List<Node>> nodeToNeighborsMap = new HashMap<>();

    private List<Node> nodes = new ArrayList<>();

    private List<Edge> edges = new ArrayList<>();

    @Override
    public boolean isAdjacent(Node n0, Node n1) {
        List<Node> candidates = nodeToNeighborsMap.get(n0);
        if (candidates == null) {
            return false;
        }

        return candidates.contains(n1);
    }

    @Override
    public void addNode(Node n0) {
        nodes.add(n0);
    }

    @Override
    public void addEdge(Node n0, Node n1) {

        Edge e = EdgeImpl.edge(n0, n1);

        if (!edges.contains(e))
            edges.add(EdgeImpl.edge(n0, n1));

        List<Node> neighborN0 = nodeToNeighborsMap.get(n0);
        List<Node> neighborN1 = nodeToNeighborsMap.get(n1);

        if (neighborN0 == null) {
            neighborN0 = new ArrayList<>();
            neighborN0.add(n1);
            nodeToNeighborsMap.put(n0, neighborN0);
        } else if (!neighborN0.contains(n1)) {
            neighborN0.add(n1);
        }

        if (neighborN1 == null) {
            neighborN1 = new ArrayList<>();
            neighborN1.add(n0);
            nodeToNeighborsMap.put(n1, neighborN1);
        } else if (!neighborN1.contains(n0)) {
            neighborN1.add(n0);
        }
    }

    @Override
    public void addEdge(Edge e0) {
        edges.add(e0);
        addEdge(e0.firstNode(), e0.secNode());
    }

    @Override
    public boolean isStronglyConnected() {
        // TODO dasc implement me
        return false;
    }

    @Override
    public List<Node> nodes() {
        return nodes;
    }

    @Override
    public List<Edge> edges() {
        return edges;
    }

    @Override
    public int degree(Node n0) {

        int deg = 0;

        if (nodeToNeighborsMap.get(n0) != null) {
            deg = nodeToNeighborsMap.get(n0).size();
        }

        return deg;
    }

    @Override
    public List<Node> getNeighbors(Node n0) {

        return nodeToNeighborsMap.get(n0);
    }

}
