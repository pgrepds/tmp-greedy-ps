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

import java.util.ArrayList;
import java.util.List;

import org.dasc.tmp.api.data.Element;
import org.dasc.tmp.api.converter.SequenceToIIntervalListConverter;
import org.dasc.tmp.core.data.IntervalImpl;
import org.dasc.tmp.core.data.Train;
import org.dasc.tmp.api.data.Sequence;
import org.dasc.tmp.api.graph.IInterval;

/**
 * @author David Scholz
 */
public class SequenceToIIntervalListConverterImpl implements SequenceToIIntervalListConverter {

	/**
	 * Get the car intervals from the incomming train.
	 * 
	 * @param train the incomming {@link Train}.
	 * @return a <List>List</code> of {@link IInterval}s.
	 */
	@Override
	public List<IInterval> convert(Sequence train) {

		return carIntervals(train);
	}

	private List<IInterval> carIntervals(Sequence train) {

		List<IInterval> result = new ArrayList<>();

		for (Integer destination : train.destinations()) {

			List<Element> carPositionsOfDestination = train.elementsOfDestination(destination);
			int start = carPositionsOfDestination.get(0).position();
			int end = carPositionsOfDestination.get(carPositionsOfDestination.size() - 1).position();

			result.add(IntervalImpl.interval(start, end, "" + destination));
		}

		return result;
	}

}
