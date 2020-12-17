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
package org.dasc.tmp;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.dasc.tmp.api.Algorithm;
import org.dasc.tmp.api.converter.NodeSetToSplittablesConverter;
import org.dasc.tmp.api.data.Element;
import org.dasc.tmp.api.data.PSBaseException;
import org.dasc.tmp.api.data.Sequence;
import org.dasc.tmp.api.data.Splittable;
import org.dasc.tmp.api.graph.*;
import org.dasc.tmp.api.converter.ElementListToElementSequenceConverter;
import org.dasc.tmp.api.converter.IntListToElementListConverter;
import org.dasc.tmp.api.converter.SequenceToIIntervalListConverter;
import org.dasc.tmp.commons.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * The Main-Class for testing the backend.
 *
 * @param -f <pathToFile> where pathToFile is the location of a CSV-File, which
 *           contains the train in the form of a comma separated numerical
 *           sequence, e.g. 1, 2, 1, 3, 4, 1, 2, 3, 5, 5, where 1 is a car in
 *           the train with destination 1, 2 with destination 2 and so on. Note:
 *           the far right is the last car.
 * @author David Scholz
 */
public final class App {

    // the parameter flag for indicating the file location for the train input as
    // CSV-data.
    private static final String INPUT_PARAMETER_FLAG = "-f";

    public static void main(String[] args) throws IOException, PSBaseException {

        if (args.length == 0 || args.length > 2) {
            throw new IllegalArgumentException("Wrong number of parameters!");
        }

        String paramFlag = args[0];
        String param = args[1];

        if (paramFlag == null || param == null) {
            throw new IllegalArgumentException("Input parameter cannot be null!");
        }

        if (!paramFlag.equals(INPUT_PARAMETER_FLAG)) {
            throw new IllegalArgumentException("Unknown parameter!");
        }

        File file = new File(param);

        if (!file.exists()) {
            throw new FileNotFoundException("File " + file.getAbsolutePath() + " does not exist!");
        }

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        List<Integer> integerSequence = Utils.createTrain(file);

        ElementListToElementSequenceConverter elementListToELementSequenceConverter =
                context.getBean(ElementListToElementSequenceConverter.class);

        IntListToElementListConverter intListToElementListConverter = context.getBean(IntListToElementListConverter.class);

        SequenceToIIntervalListConverter sequenceToIIntervalListConverter = context.getBean(SequenceToIIntervalListConverter.class);

        List<Element> elementSequence = intListToElementListConverter.convert(integerSequence);
        Sequence sequence = elementListToELementSequenceConverter.convert(elementSequence);
        List<IInterval> intervals = sequenceToIIntervalListConverter.convert(sequence);

        Graph graph = context.getBean(GraphFactory.class).createIntervalGraph(sequence, intervals);

        CliqueDetector cliqueDetector = context.getBean(CliqueDetectoryFactory.class).createMaximumCliqueDetector();
        cliqueDetector.execute(graph);
        Set<Node> clique = cliqueDetector.getClique();

        NodeSetToSplittablesConverter nodeSetToSplittablesConverter = context.getBean(NodeSetToSplittablesConverter.class);

        List<Splittable> splittables = nodeSetToSplittablesConverter.convert(clique);

        Algorithm algorithm = context.getBean(Algorithm.class);

        algorithm.run(graph, splittables);

    }

    private App() {

    }
}
