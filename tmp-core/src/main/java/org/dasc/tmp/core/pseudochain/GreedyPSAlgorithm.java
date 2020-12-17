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

import org.springframework.beans.factory.annotation.Autowired;
import org.dasc.tmp.api.Algorithm;
import org.dasc.tmp.api.Pseudochain;
import org.dasc.tmp.api.PseudochainFactory;
import org.dasc.tmp.api.data.Splittable;
import org.dasc.tmp.api.graph.Graph;
import org.dasc.tmp.api.graph.Node;
import org.dasc.tmp.commons.Utils;

import java.util.*;

/**
 * @author David Scholz
 */
public class GreedyPSAlgorithm implements Algorithm {

    @Autowired
    private PseudochainFactory pseudochainFactory;

    private List<Pseudochain> pseudochains = new ArrayList<>();

    @Override
    public void run(Graph graph, List<Splittable> splittables) {

        Set<Node> visited = new HashSet<>();
        splittables.sort(Utils.comparator());

        while (splittables.size() > 0) {

            Pseudochain chain = pseudochainFactory.newPseudochain();
            pseudochains.add(chain);

            for (Splittable splittable : splittables) {
                if (chain.addTau(splittable)) {
                    visited.add(splittable.a());
                    visited.add(splittable.b());
                    visited.add(splittable.c());
                }
            }

            removeVisitedNodes(splittables, visited);
        }

        for (Node node : graph.nodes()) {
            if (!visited.contains(node)) {
                boolean exit = false;
                for (Pseudochain ps : pseudochains) {
                    if (ps.add(node)) {
                        visited.add(node);
                        exit = true;
                        break;
                    }
                }
                if (exit) {
                    continue;
                }

                Pseudochain chain = pseudochainFactory.newPseudochain();
                chain.add(node);
                pseudochains.add(chain);
            }
        }
    }

    private void removeVisitedNodes(List<Splittable> splittables, Set<Node> visited) {
        for (Node visitedNode : visited) {
            for (Splittable s : new ArrayList<>(splittables)) {
                if (s.a().equals(visitedNode)
                        || s.b().equals(visitedNode)
                        || s.c().equals(visitedNode)) {
                    splittables.remove(s);
                }
            }
        }
    }

    @Override
    public List<Pseudochain> getResult() {

        return pseudochains;
    }

}
