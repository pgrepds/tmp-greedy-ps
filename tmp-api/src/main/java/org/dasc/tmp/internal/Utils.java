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
package org.dasc.tmp.internal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
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
	 * Reads the given file line by line and creates a list of integers.
	 * 
	 * @param file the file to read.
	 * @return a <code>List</code> of <code>Integer</code>s.
	 * @throws IOException
	 */
	public static List<Integer> createIntegerSequence(File file) throws IOException {

		List<Integer> result = new ArrayList<>();

		readFile(file, s -> {
			String[] splittedLine = s.split(",");
			Arrays.asList(splittedLine).forEach(i -> result.add(Integer.parseInt(i)));
		});

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

	private Utils() {

	}
	
}
