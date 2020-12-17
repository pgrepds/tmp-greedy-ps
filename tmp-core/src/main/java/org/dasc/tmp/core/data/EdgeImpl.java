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
import org.dasc.tmp.api.graph.Node;

import java.util.Objects;

/**
 * @author David Scholz
 */
public class EdgeImpl implements Edge {

    private final Node n1;
    private final Node n2;

    private EdgeImpl(final Node n1, Node n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public static Edge edge(final Node n1, final Node n2) {
        return new EdgeImpl(n1, n2);
    }

    @Override
    public Node firstNode() {
        return n1;
    }

    @Override
    public Node secNode() {
        return n2;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeImpl edge = (EdgeImpl) o;
        return Objects.equals(n1, edge.n1) &&
                Objects.equals(n2, edge.n2) || (Objects.equals(n1, edge.n2) && Objects.equals(n2, edge.n1))
                || (Objects.equals(n2, edge.n1) && Objects.equals(n1, edge.n2));
    }

    @Override
    public int hashCode() {
        return Objects.hash(n1, n2);
    }
}
