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
package org.dasc.tmp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.dasc.tmp.api.Algorithm;
import org.dasc.tmp.api.Pseudochain;
import org.dasc.tmp.api.converter.ElementListToElementSequenceConverter;
import org.dasc.tmp.api.converter.IntListToElementListConverter;
import org.dasc.tmp.api.converter.NodeSetToSplittablesConverter;
import org.dasc.tmp.api.converter.SequenceToIIntervalListConverter;
import org.dasc.tmp.api.data.Element;
import org.dasc.tmp.api.data.Sequence;
import org.dasc.tmp.api.data.Splittable;
import org.dasc.tmp.api.graph.*;
import org.dasc.tmp.commons.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author David Scholz
 */
public class AlgorithmFacadeImpl implements AlgorithmFacade {

    @Autowired
    private ElementListToElementSequenceConverter elementListToElementSequenceConverter;

    @Autowired
    private IntListToElementListConverter intListToElementListConverter;

    @Autowired
    private SequenceToIIntervalListConverter sequenceToIIntervalListConverter;

    @Autowired
    private GraphFactory graphFactory;

    @Autowired
    private CliqueDetectoryFactory cliqueDetectoryFactory;

    @Autowired
    private NodeSetToSplittablesConverter nodeSetToSplittablesConverter;

    @Autowired
    private Algorithm algorithm;

    private List<Pseudochain> result;

    private String sequence;

    private Graph graph;

    @Override
    public List<Pseudochain> result() {
        return result;
    }

    @Override
    public void run(File file) throws IOException {

        List<Integer> integerSequence = Utils.createTrain(file);
        doRun(integerSequence);
    }

    private void doRun(List<Integer> integerSequence) {
        sequence = integerSequence.toString();

        List<Element> elementSequence = intListToElementListConverter.convert(integerSequence);
        Sequence sequence = elementListToElementSequenceConverter.convert(elementSequence);
        List<IInterval> intervals = sequenceToIIntervalListConverter.convert(sequence);

        graph = graphFactory.createIntervalGraph(sequence, intervals);
        CliqueDetector cliqueDetector = cliqueDetectoryFactory.createMaximumCliqueDetector();
        cliqueDetector.execute(graph);
        Set<Node> clique = cliqueDetector.getClique();

        List<Splittable> splittables = nodeSetToSplittablesConverter.convert(clique);

        algorithm.run(graph, splittables);

        result = algorithm.getResult();
    }

    @Override
    public String sequence() {
        return sequence;
    }

    @Override
    public Graph intervalGraph() {
        return graph;
    }

    @Override
    public void run(String train) {

        List<Integer> integerSequence = Utils.createTrain(train);
        doRun(integerSequence);
    }
}
