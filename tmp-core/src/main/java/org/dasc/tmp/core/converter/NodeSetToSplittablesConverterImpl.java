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
package org.dasc.tmp.core.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.dasc.tmp.api.converter.NodeSetToSplittablesConverter;
import org.dasc.tmp.api.data.PSBaseException;
import org.dasc.tmp.api.data.Splittable;
import org.dasc.tmp.api.data.SplittableFactory;
import org.dasc.tmp.api.graph.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author David Scholz
 */
public class NodeSetToSplittablesConverterImpl implements NodeSetToSplittablesConverter {

    @Autowired
    private SplittableFactory factory;

    @Override
    public List<Splittable> convert(Set<Node> source) {

        List<Splittable> splittableList = new ArrayList<>();

        for (Node a : source) {

            for (Node b : source) {

                if (a.equals(b)) {
                    continue;
                }
                for (Node c : source) {

                    if (a.equals(c) || b.equals(c)) {
                        continue;
                    }

                    try {
                        splittableList.add(factory.newSplittable(a, b, c));
                    } catch (PSBaseException e) {
                        continue;
                    }
                }
            }
        }

        return splittableList;
    }
}
