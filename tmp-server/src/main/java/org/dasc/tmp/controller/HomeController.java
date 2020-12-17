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
package org.dasc.tmp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.dasc.tmp.api.Pseudochain;
import org.dasc.tmp.api.graph.Graph;
import org.dasc.tmp.api.graph.Node;
import org.dasc.tmp.services.AlgorithmFacade;
import org.dasc.tmp.services.GraphDOTConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author David Scholz
 */
@Controller
public class HomeController {

    private static final String FOLDER = "resources/temp/";

    @Autowired
    private AlgorithmFacade algorithmFacade;

    @Autowired
    private GraphDOTConverter graphDOTConverter;

    private List<Pseudochain> result = new ArrayList<>();

    private String sequence;

    private Graph intervalGraph;

    @RequestMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/data/dot/graph")
    @ResponseBody
    public String graphAsDot() {

        return "{ " + graphDOTConverter.toDot(intervalGraph) + " }";
    }

    @GetMapping("data/dot/graph/ps")
    @ResponseBody
    public String resultAsPSDot() {

        List<String> psResult = new ArrayList<>();
        for (Pseudochain ps : result) {
            psResult.add(graphDOTConverter.toDot(ps));
        }

        return "{ " + psResult.toString().replace("[", "").
                replace("]", "").replace(",", "") + " }";
    }

    @GetMapping("data/dot/graph/ps/independent")
    @ResponseBody
    public List<String> independentSetsAsDot() {

        List<String> res = new ArrayList<>();

        for (Pseudochain chain : result) {

            for (List<Node> independent : chain.getIndependentSets()) {

                StringBuilder sb = new StringBuilder();
                sb.append("{");
                for (Node n : independent) {
                    sb.append(n.first().destination());
                    sb.append(";");
                }
                sb.append("}");
                res.add(sb.toString());
            }

        }

        return res;
    }

    @GetMapping("/data/dot/graph/connectedNodes")
    @ResponseBody
    public List<String> getConnectedNodes(@RequestParam("id") String id) {
        List<String> connected = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Pseudochain chain : result) {

            for (Node n : chain.getGraph().nodes()) {

                if (n.first().destination() == Integer.parseInt(id)) {

                    List<Node> neighbors = chain.getGraph().getNeighbors(n);

                    if (neighbors != null) {

                        for (Node neighbor : neighbors) {

                            sb.append(neighbor.first().destination() + ";");
                        }

                    }
                    break;
                }
            }
        }

        sb.append("}");
        connected.add(sb.toString());

        return connected;
    }

    @GetMapping("/data/train")
    @ResponseBody
    public String getTrain() {

        sequence = sequence.replace("[", "");
        sequence = sequence.replace("]", "");

        return sequence;
    }

    @GetMapping("/data/train/random")
    @ResponseBody
    public String getRandomTrain() {

        Random random = new Random();

        int max = 16;
        int min = 1;

        StringBuilder sb = new StringBuilder();

        int bound = random.nextInt(60 - 10 + 1) + 10;

        for (int i = 0; i < bound; i++) {
            int res = random.nextInt(max - min + 1) + min;
            sb.append(res);
            if (i != bound - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    @PostMapping("/editTrain")
    @ResponseBody
    public void edit(@RequestParam("content") String train) {

        result.clear();
        algorithmFacade.run(train);
        sequence = algorithmFacade.sequence();
        intervalGraph = algorithmFacade.intervalGraph();
        result = algorithmFacade.result();
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {

        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(file.getOriginalFilename());
            Files.write(path, bytes);

            attributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

            File f = new File(path.toAbsolutePath().toString());

            result.clear();
            algorithmFacade.run(f);
            sequence = algorithmFacade.sequence();
            intervalGraph = algorithmFacade.intervalGraph();
            result = algorithmFacade.result();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/home";
    }

}
