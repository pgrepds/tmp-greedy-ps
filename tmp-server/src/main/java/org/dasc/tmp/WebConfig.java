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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.dasc.tmp.api.Algorithm;
import org.dasc.tmp.api.Pseudochain;
import org.dasc.tmp.api.PseudochainFactory;
import org.dasc.tmp.api.converter.ElementListToElementSequenceConverter;
import org.dasc.tmp.api.converter.IntListToElementListConverter;
import org.dasc.tmp.api.converter.NodeSetToSplittablesConverter;
import org.dasc.tmp.api.converter.SequenceToIIntervalListConverter;
import org.dasc.tmp.api.data.SplittableFactory;
import org.dasc.tmp.api.graph.*;
import org.dasc.tmp.core.converter.ElementListToElementSequenceConverterImpl;
import org.dasc.tmp.core.converter.IntListToElementListConverterImpl;
import org.dasc.tmp.core.converter.NodeSetToSplittablesConverterImpl;
import org.dasc.tmp.core.converter.SequenceToIIntervalListConverterImpl;
import org.dasc.tmp.core.data.*;
import org.dasc.tmp.core.graph.CliqueDetectorFactoryImpl;
import org.dasc.tmp.core.pseudochain.GreedyPSAlgorithm;
import org.dasc.tmp.core.pseudochain.PseudochainFactoryImpl;
import org.dasc.tmp.core.pseudochain.PseudochainImpl;
import org.dasc.tmp.services.AlgorithmFacade;
import org.dasc.tmp.services.AlgorithmFacadeImpl;
import org.dasc.tmp.services.GraphDOTConverter;
import org.dasc.tmp.services.GraphDOTConverterImpl;

/**
 * @author David Scholz
 */
@Configuration
public class WebConfig {

    @Bean
    public AlgorithmFacade algorithmFacade() {
        return new AlgorithmFacadeImpl();
    }

    @Bean
    public PseudochainFactory pseudochainFactory() {
        return new PseudochainFactoryImpl();
    }

    @Bean
    public NodeSetToSplittablesConverter nodeSetToSplittablesConverter() {
        return new NodeSetToSplittablesConverterImpl();
    }

    @Bean
    public SplittableFactory splittableFactory() {
        return new SplittableFactoryImpl();
    }

    @Bean
    public Algorithm algorithm() {
        return new GreedyPSAlgorithm();
    }

    @Bean
    public Graph undirectedGraph() {
        return new UndirectedGraph();
    }

    @Bean
    public Pseudochain pseudochain() {
        return new PseudochainImpl();
    }

    @Bean
    public ElementListToElementSequenceConverter getElementListToElementSequenceConverter() {
        return new ElementListToElementSequenceConverterImpl();
    }

    @Bean
    public IntListToElementListConverter getIntListToElementListConverter() {
        return new IntListToElementListConverterImpl();
    }

    @Bean
    public SequenceToIIntervalListConverter getSequenceToIntervalListConverter() {
        return new SequenceToIIntervalListConverterImpl();
    }

    @Bean
    public NodeFactory getNodeFactory() {
        return new NodeFactoryImpl();
    }

    @Bean
    public GraphFactory getGraphFactory() {
        return new GraphFactoryImpl();
    }

    @Bean
    public CliqueDetectoryFactory getCliqueDetectorFactory() {
        return new CliqueDetectorFactoryImpl();
    }

    @Bean
    public IntervalFactory getIntervalFactory() {
        return new IntervalFactoryImpl();
    }

    @Bean
    public GraphDOTConverter graphDOTConverter() {
        return new GraphDOTConverterImpl();
    }
}
