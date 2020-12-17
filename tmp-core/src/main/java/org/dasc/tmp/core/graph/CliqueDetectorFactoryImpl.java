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
import org.dasc.tmp.api.graph.CliqueDetectoryFactory;

/**
 * @author David Scholz
 */
public final class CliqueDetectorFactoryImpl implements CliqueDetectoryFactory {

	public CliqueDetector createSpecificCliqueDetector(int k) {

		DefaultCliqueDetector d = new DefaultCliqueDetector();
		d.setK(k);
		
		return d;
	}

	public CliqueDetector createMaximumCliqueDetector() {
		
		return new MaxCliqueDetector();
	}
	
	public CliqueDetectorFactoryImpl() {
		
	}
}
