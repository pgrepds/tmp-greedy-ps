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
import org.dasc.tmp.api.data.PSBaseException;
import org.dasc.tmp.api.data.Splittable;
import org.dasc.tmp.api.graph.Node;

/**
 * @author David Scholz
 */
public class SplittableImpl implements Splittable {

    private final Node a;
    private final Node b;
    private final Node c;

    public SplittableImpl(final Node a, final Node b, final Node c) throws PSBaseException {
        this.a = a;
        this.b = b;
        this.c = c;

        if (!isValid(a, b, c)) {
            throw new PSBaseException("Splittable is not valid");
        }
    }

    private boolean isValid(Node a, Node b, Node c) {

        if (!(a.first().position() < c.first().position())) {
            return false;
        }

        // I. a and b overlaps. b and c overlaps. a and c does not overlap.
        if (a.interval().overlaps(b.interval())
                && b.interval().overlaps(c.interval())
                && !a.interval().overlaps(c.interval())) {
            return true;
        }

        Element firstC = c.first();
        Element lastA = a.last();
        int firstCPos = firstC.position();
        int lastAPos = lastA.position();

        // II. not element between firstC and lastA
        for (Element bElement : b.elements()) {

            int bPosition = bElement.position();

            if (bPosition > firstCPos && bPosition < lastAPos) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Node a() {
        return a;
    }

    @Override
    public Node b() {
        return b;
    }

    @Override
    public Node c() {
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SplittableImpl that = (SplittableImpl) o;

        if (a != null ? !a.equals(that.a) : that.a != null) return false;
        if (b != null ? !b.equals(that.b) : that.b != null) return false;
        return c != null ? c.equals(that.c) : that.c == null;

    }

    @Override
    public int hashCode() {
        int result = a != null ? a.hashCode() : 0;
        result = 31 * result + (b != null ? b.hashCode() : 0);
        result = 31 * result + (c != null ? c.hashCode() : 0);
        return result;
    }

}
