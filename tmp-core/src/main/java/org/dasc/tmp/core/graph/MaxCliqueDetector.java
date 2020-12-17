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
package org.dasc.tmp.core.graph;

import org.dasc.tmp.api.graph.CliqueDetector;
import org.dasc.tmp.api.graph.Graph;
import org.dasc.tmp.api.graph.Node;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;


/**
 * Bron Kerbosch algorithm
 * 
 * @author https://stackoverflow.com/questions/37524112/bron-kerbosch-implementation-in-java
 */
public class MaxCliqueDetector implements CliqueDetector {

	private Vector<Node> listNoeud;
	private Set<Node> MaxClique;
	private Graph g;

	public MaxCliqueDetector() {
		this.listNoeud = new Vector<Node>();
		this.MaxClique = new HashSet<Node>();
	}

	private Vector<Node> getNbrs(Node v) {

		Vector<Node> t = new Vector<Node>();
		if (g.getNeighbors(v) != null) {
			t.addAll(g.getNeighbors(v));
		}
		return t;
	}

	// Intersection of two sets
	private Vector<Node> intersect(Vector<Node> arlFirst, Vector<Node> arlSecond) {
		Vector<Node> arlHold = new Vector<Node>(arlFirst);
		arlHold.retainAll(arlSecond);
		return arlHold;
	}

	// Union of two sets
	private Vector<Node> union(Vector<Node> arlFirst, Vector<Node> arlSecond) {
		Vector<Node> arlHold = new Vector<Node>(arlFirst);
		arlHold.addAll(arlSecond);
		return arlHold;
	}

	// Removes the neigbours
	private Vector<Node> removeNbrs(Vector<Node> arlFirst, Node v) {
		Vector<Node> arlHold = new Vector<Node>(arlFirst);
		arlHold.removeAll(getNbrs(v));
		return arlHold;
	}

	// Version with a Pivot
	private void Bron_KerboschWithPivot(Vector<Node> R, Vector<Node> P, Vector<Node> X, String pre) {

		if ((P.size() == 0) && (X.size() == 0)) {
			printClique(R);
			return;
		}
		// System.out.println();
		Vector<Node> P1 = new Vector<Node>(P);
		// Find Pivot
		Node u = getMaxDegreeVertex(union(P, X));

		// System.out.println("" + pre + " Pivot is " + (u.getLabel()));
		// P = P / Nbrs(u)
		P = removeNbrs(P, u);

		for (Node v : P) {
			R.add(v);
			Bron_KerboschWithPivot(R, intersect(P1, getNbrs(v)), intersect(X, getNbrs(v)), pre + "\t");
			R.remove(v);
			P1.remove(v);
			X.add(v);
		}
	}

	private Node getMaxDegreeVertex(Vector<Node> t) {
		int i = 0, temp = 0;
		Node n = null;
		while (i < t.size()) {
			if (g.degree(t.elementAt(i)) > temp) {
				temp = g.degree(t.elementAt(i));
				n = t.get(i);
			}
			i += 1;
		}
		return n;
	}

	private void Bron_KerboschPivotExecute() {

		Vector<Node> X = new Vector<Node>();
		Vector<Node> R = new Vector<Node>();
		Vector<Node> P = listNoeud;
		Bron_KerboschWithPivot(R, P, X, "");
	}

	private void printClique(Vector<Node> r) {

		if (this.MaxClique.isEmpty()) {

			for (Node v : r) {
				this.MaxClique.add(v);
			}

		} else {
			if (r.size() > this.MaxClique.size()) {
				this.MaxClique.clear();
				for (Node v : r) {
					this.MaxClique.add(v);
				}
			}
		}

	}

	@Override
	public void execute(Graph graph) {
		this.g = graph;
		for (Node n : graph.nodes()) {
			listNoeud.add(n);
		}
		
		Bron_KerboschPivotExecute();
	}

	@Override
	public Set<Node> getClique() {
		
		return MaxClique;
	}
	
}
