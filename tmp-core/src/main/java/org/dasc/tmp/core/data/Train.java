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
import org.dasc.tmp.api.data.Sequence;

import java.util.*;

/**
 * Data container representing the incomming train.
 *
 * @author David Scholz
 */
public final class Train implements Sequence {

    private final Map<Integer, List<Element>> destinationToCarPositions;

    private List<Integer> destinations = new ArrayList<>();

    private Train(Element... train) {
        destinationToCarPositions = destinationToCarPositions(Arrays.asList(train));
    }

    private Train(List<Element> train) {
        destinationToCarPositions = destinationToCarPositions(train);
    }

    public static Train train(List<Element> train) {
        return new Train(train);
    }

    public List<Element> carsOfDestination(Integer destination) {

        return destinationToCarPositions.get(destination);
    }

    public List<Integer> destinations() {

        return destinations;
    }

    private Map<Integer, List<Element>> destinationToCarPositions(List<Element> train) {

        Map<Integer, List<Element>> destinationToCarPositions = new HashMap<>();

        for (int i = 0; i < train.size(); i++) {

            Element car = train.get(i);
            int destination = car.destination();
            if (destinationToCarPositions.containsKey(destination)) {
                destinationToCarPositions.get(destination).add(car);
            } else {
                List<Element> cars = new ArrayList<>();
                cars.add(car);
                destinationToCarPositions.put(destination, cars);
                destinations.add(destination);
            }
        }

        return destinationToCarPositions;
    }

    @Override
    public List<Element> elementsOfDestination(Integer destination) {

        return carsOfDestination(destination);
    }

}
