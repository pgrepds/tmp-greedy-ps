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
package org.dasc.tmp.commons;

import org.dasc.tmp.api.data.Splittable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 
 * A basic utility class. For now, it contains pretty much every utility except
 * graph utilities. For the future it is planned to move the common package to
 * its own module, such it can build an own maven artifact.
 * 
 * @author David Scholz
 */
public final class Utils {

	/**
	 * Reads the given file line by line and creates a list of integers representing
	 * the incomming train.
	 * 
	 * @param file the file to read.
	 * @return a <code>List</code> of <code>Integer</code>s, where each integer
	 *         represents one car with its destination in the train.
	 * @throws IOException
	 */
	public static List<Integer> createTrain(File file) throws IOException {

		List<Integer> result = new ArrayList<>();

		readFile(file, s -> {
			train(result, s);
		});

		return result;
	}

	private static void train(List<Integer> result, String s) {
		String[] splittedLine = s.split(",");
		Arrays.asList(splittedLine).forEach(i -> result.add(Integer.parseInt(i.trim())));
	}

	public static List<Integer> createTrain(String train) {
		List<Integer> result = new ArrayList<>();
		train(result, train);
		return result;
	}

	/**
	 * Reads the given file line by line.
	 * 
	 * @param file the file to read.
	 * @return a <code>List</code> of <code>String</code>s, where each string
	 *         represents one line in the file.
	 * @throws IOException
	 */
	public static List<String> readFile(File file) throws IOException {

		List<String> result = new ArrayList<>();
		readFile(file, result::add);

		return result;
	}

	private static void readFile(File file, Consumer<String> consumer) throws IOException {

		try (Stream<String> stream = Files.lines(file.toPath())) {
			stream.forEach(consumer::accept);
		} catch (IOException e) {
			throw e;
		}
	}

	public static Comparator<Splittable> comparator() {

		return (o1, o2) -> {

			int i11 = o1.a().first().position();
			int i21 = o2.a().first().position();

			int result = i11 - i21;
			if (result == 0) {
				int i12 = o1.b().first().position();
				int i22 = o2.b().first().position();
				result = i12 - i22;

				if (result == 0) {

					int i13 = o1.c().first().position();
					int i23 = o2.c().first().position();
					result = i13 - i23;
				}
			}

			return result;
		};
	}

	private Utils() {

	}
	
}
