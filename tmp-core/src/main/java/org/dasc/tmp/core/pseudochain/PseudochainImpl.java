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
package org.dasc.tmp.core.pseudochain;

import org.dasc.tmp.api.Pseudochain;
import org.dasc.tmp.api.data.Splittable;
import org.dasc.tmp.api.graph.Graph;
import org.dasc.tmp.api.graph.Node;
import org.dasc.tmp.core.data.UndirectedGraph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The main data structure for our algorithm according to Doerpinghaus.
 *
 * @author David Scholz
 */
public final class PseudochainImpl implements Pseudochain {

    private Graph graph = new UndirectedGraph();

    private LinkedList<List<Node>> indendentSets = new LinkedList<>();

    @Override
    public boolean addTau(Splittable splittable) {

        if (graph.nodes().isEmpty()) {
            handleInitPS(splittable);
            return true;
        }

        Node a = splittable.a();
        Node b = splittable.b();
        Node c = splittable.c();

        List<Node> firstIndependentSet = indendentSets.getFirst();
        List<Node> lastIndependentSet = indendentSets.getLast();

        Node firstNodeOfFirstIndependentSet = firstIndependentSet.get(0);
        Node firstNodeOfLastIndependentSet = lastIndependentSet.get(0);

        if (c.equals(firstNodeOfFirstIndependentSet)) {

            return appendSplittableToLeft(a, b, firstNodeOfFirstIndependentSet);

        } else if (a.equals(firstNodeOfLastIndependentSet)) {

            return appendSplittableToRight(b, c, firstNodeOfLastIndependentSet);
        }

        return false;
    }

    private boolean appendSplittableToRight(Node b, Node c, Node firstNodeOfLastIndependentSet) {

        if (graph.nodes().contains(b) || graph.nodes().contains(c)) {
            return false;
        }

        List<Node> newLastIndependentSet = new ArrayList<>();
        newLastIndependentSet.add(c);
        indendentSets.addLast(newLastIndependentSet);

        graph.addNode(b);
        graph.addNode(c);

        graph.addEdge(firstNodeOfLastIndependentSet, b);
        graph.addEdge(b, c);

        return true;
    }

    private boolean appendSplittableToLeft(Node a, Node b, Node firstNodeOfFirstIndependentSet) {

        if (graph.nodes().contains(a) || graph.nodes().contains(b)) {
            return false;
        }

        List<Node> newFirstIndependentSet = new ArrayList<>();
        newFirstIndependentSet.add(a);
        indendentSets.addFirst(newFirstIndependentSet);

        graph.addNode(a);
        graph.addNode(b);

        graph.addEdge(a, b);
        graph.addEdge(b, firstNodeOfFirstIndependentSet);

        return true;
    }

    private void handleInitPS(Splittable splittable) {
        Node a = splittable.a();
        Node b = splittable.b();
        Node c = splittable.c();

        List<Node> independentSetOfA = new ArrayList<>();
        List<Node> independentSetOfC = new ArrayList<>();

        independentSetOfA.add(a);
        independentSetOfC.add(c);

        indendentSets.add(independentSetOfA);
        indendentSets.add(independentSetOfC);

        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);

        graph.addEdge(a, b);
        graph.addEdge(b, c);
    }

    @Override
    public boolean add(Node n) {

        if (indendentSets.isEmpty()) {
            List<Node> nodeList = new ArrayList<>();
            nodeList.add(n);
            indendentSets.add(nodeList);

            graph.addNode(n);
            return true;
        }

        if (indendentSets.size() == 1) {
            List<Node> nodeList = indendentSets.get(0);

            if (!isAdjacentToNodesInIndependentSet(nodeList, n)) {
                graph.addNode(n);
                return nodeList.add(n);
            } else {
                return false;
            }
        }

        List<Node> first = indendentSets.getFirst();
        List<Node> last = indendentSets.getLast();

        if (!isAdjacentToNodesInIndependentSet(first, n)) {

            if (!(n.last().position() < first.get(0).first().position())) {
                return false;
            }

            graph.addNode(n);
            return first.add(n);


        } else if (!isAdjacentToNodesInIndependentSet(last, n)) {

            if (!(n.first().position() > graph.getNeighbors(last.get(0)).get(0).last().position())) {
                return false;
            }

            graph.addNode(n);
            return last.add(n);

        }

        return false;
    }

    private boolean isAdjacentToNodesInIndependentSet(List<Node> independentSet, Node node) {
        for (Node n : independentSet) {
            if (n.interval().overlaps(node.interval())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Graph getGraph() {
        return graph;
    }

    @Override
    public List<List<Node>> getIndependentSets() {
        return indendentSets;
    }

}
