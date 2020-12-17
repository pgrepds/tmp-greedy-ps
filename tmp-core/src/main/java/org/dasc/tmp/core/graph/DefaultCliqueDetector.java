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

import java.util.*;

/**
 * Adapted to our use case.
 * 
 * Credits to: https://github.com/mirsamantajbakhsh/CliqueDetector
 */
public class DefaultCliqueDetector implements CliqueDetector {

	private int k = 0;
	private GenQueue<TreeSet<Node>> Bk = new GenQueue<TreeSet<Node>>();
	
	private Set<Node> clique = new HashSet<Node>();
	private Set<Set<Node>> tempClique = new HashSet<>();
	
	private class SortByID implements Comparator<Node> {

		public int compare(Node n1, Node n2) {

			if (n1.interval().start() > n2.interval().start()) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	class GenQueue<E> {

		private LinkedList<E> list = new LinkedList<E>();

		public void enqueue(E item) {
			list.addLast(item);
		}

		public E dequeue() {
			return list.pollFirst();
		}

		public boolean hasItems() {
			return !list.isEmpty();
		}

		public int size() {
			return list.size();
		}

		public void addItems(GenQueue<? extends E> q) {
			while (q.hasItems()) {
				list.addLast(q.dequeue());
			}
		}
	}
	// </editor-fold>

	private Vector<Node> getLargerIndexNodes(Graph g, Node vi) {
		Vector<Node> output = new Vector<Node>();
		for (Node n : g.nodes()) {
			if ((n.interval().start() >  vi.interval().start() && g.isAdjacent(n, vi))) {
				// TODO check degree of n and vi
				output.addElement(n);
			}
		}

		return output;
	}

	private boolean checkBk1IsClique(Graph g, TreeSet<Node> Bk1) {
		for (Node firstNode : Bk1) {
			for (Node secondNode : Bk1) {
				if (firstNode == secondNode) {
					continue;
				}

				if (!g.isAdjacent(firstNode, secondNode)) { // One edge is missing in the Bk+1 clique
					return false;
				}
			}
		}

		return true;
	}

	private void executeAlgorithm(Graph graph) {

		Graph g = graph;

		// Firstly add each node as an item in Bk
		TreeSet<Node> tmp;
		for (Node n : g.nodes()) {
			// Trick: if the node's degree is less than k-1, it can not involve in k-clique
			if (g.degree(n) >= k - 1) {
				tmp = new TreeSet<Node>(new SortByID());
				tmp.add(n);
				Bk.enqueue(tmp); // Add the B1 (node itself) to the queue
			}
		}

		// Now start the iterative process for finding cliques
		tmp = Bk.dequeue();

		while (tmp != null) {

			// Search for Bk+1
			Node vi = tmp.last(); // (Node) getLastElement(tmp);
			Vector<Node> largerIndexes = getLargerIndexNodes(g, vi);

			for (Node vj : largerIndexes) {
				TreeSet<Node> Bk1 = new TreeSet<Node>(new SortByID());
				Bk1.addAll(tmp); // Clone current Bk into Bk+1
				Bk1.add(vj);
				if (Bk1.size() <= getK() && checkBk1IsClique(g, Bk1)) {

					if (Bk1.size() == getK()) { // A clique of size k found. Finish expanding this Bk+1 here.
						tempClique.add(Bk1);
					} else if (Bk1.size() < getK()) {
						Bk.enqueue(Bk1); // k should be checked for finding cliques of size k.
					} 
				}
			}

			tmp = Bk.dequeue(); // Check next item
		}

		for (Set<Node> output : tempClique ) {
			for (Node n : output) {
				getClique().add(n);
			}
		}
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	@Override
	public void execute(Graph graph) {
		executeAlgorithm(graph);
	}

	@Override
	public Set<Node> getClique() {
		
		return clique;
	}
	
}