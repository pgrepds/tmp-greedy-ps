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

import java.util.Objects;

/**
 * Car in a {@link Train}.
 *
 * @author David Scholz
 */
public final class Car implements Element {

    private final int destination;
    private final int positionInTrain;

    private Car(final int destination, final int positionInTrain) {
        this.destination = destination;
        this.positionInTrain = positionInTrain;
    }

    public static Car car(final int destination, final int positionInTrain) {
        return new Car(destination, positionInTrain);
    }

    public int getDestination() {
        return destination;
    }

    public int getPositionInTrain() {
        return positionInTrain;
    }

    @Override
    public String toString() {

        return "Destination " + destination + " at train position " + positionInTrain;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionInTrain);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Car) {
            Car objCar = (Car) obj;
            if (objCar.getPositionInTrain() == this.positionInTrain) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int destination() {

        return getDestination();
    }

    @Override
    public int position() {

        return getPositionInTrain();
    }

}
