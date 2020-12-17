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

import org.dasc.tmp.api.data.PSBaseException;
import org.dasc.tmp.api.data.Splittable;
import org.dasc.tmp.api.data.SplittableFactory;
import org.dasc.tmp.api.graph.Node;

/**
 * @author David Scholz
 */
public final class SplittableFactoryImpl implements SplittableFactory {

    @Override
    public Splittable newSplittable(Node a, Node b, Node c) throws PSBaseException {
        return new SplittableImpl(a, b, c);
    }
}
