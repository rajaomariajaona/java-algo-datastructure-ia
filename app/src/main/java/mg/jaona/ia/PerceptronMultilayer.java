package mg.jaona.ia;

import mg.jaona.datastructure.graph.AdjacencyMatrixGraph;
import mg.jaona.datastructure.graph.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class PerceptronMultilayer {
    private Structure structure;
    private AdjacencyMatrixGraph<Float> g;
    private Integer nbPrototype;
    private List<Vertex> inputVertexes;
    private List<Vertex> hiddenVertexes;
    private Vertex outputVertexes;
    private Float alpha = 0.001f;

    public PerceptronMultilayer(Structure structure) {
        this.structure = structure;
        this.inputVertexes = new ArrayList<>();
        this.hiddenVertexes = new ArrayList<>();
        this.outputVertexes = null;
        this.g = new AdjacencyMatrixGraph<>();
        for (int i = 0; i < structure.getNbInputLayer(); i++) {
            Vertex v = new Vertex("i" + i);
            this.inputVertexes.add(v);
            g.addVertex(v);
        }
        for (int i = 0; i < structure.getNbHiddenLayer(); i++) {
            Vertex v = new Vertex("h" + i);
            this.hiddenVertexes.add(v);
            g.addVertex(v);
        }
        this.outputVertexes = new Vertex("o");
    }

    public void fit(List<Float> partSerie, int epoch) {
        while(epoch > 0){
            Deque<Float> deque = new ArrayDeque<>(partSerie);
            if (partSerie.size() < structure.nbInputLayer) {
                throw new IllegalArgumentException("Part Serie is not enough");
            }
            while (deque.size() > structure.nbInputLayer) {
                List<Float> toPropagate = deque.stream().limit(structure.nbInputLayer + 1).collect(Collectors.toList());
                Float expected = toPropagate.remove(structure.nbInputLayer);
                propagateOneValue(toPropagate, expected);
                deque.removeFirst();
            }
            epoch--;
        }
    }

    public List<Float> predict(List<Float> input, int step) {
        List<Float> res = new ArrayList<>();
        Deque<Float> queueInput = new ArrayDeque<>(input);
        if (input.size() != structure.getNbInputLayer()) {
            throw new IllegalArgumentException("Input not correspond to input layer");
        } else if (step < 1) {
            throw new IllegalArgumentException("Step invalid");
        } else {
            for (int i = 0; i < step; i++) {
                Float pred = forwardPropagation(new ArrayList<>(queueInput));
                res.add(pred);
                queueInput.removeLast();
                queueInput.addFirst(pred);
            }
        }
        return res;
    }

    private void initializeWeight() {
        for (Vertex in : inputVertexes) {
            for (Vertex hi : hiddenVertexes) {
                this.g.addEdge(in, hi, Double.valueOf(Math.random()).floatValue());
            }
        }
        for (Vertex hi : hiddenVertexes) {
            this.g.addEdge(hi, outputVertexes, Double.valueOf(Math.random()).floatValue());
        }
    }

    private Float forwardPropagation(List<Float> input) {
        Float out = 0f;
        if (input.size() != structure.getNbInputLayer()) {
            throw new IllegalArgumentException("Input and input Layer size is different");
        } else {
            Map<Vertex, Float> inputValues = new HashMap<>();
            int i = 0;
            for (Vertex in : this.inputVertexes) {
                inputValues.put(in, input.get(i++));
            }
            Map<Vertex, Float> middleValues = new HashMap<>();

            for (Vertex in : inputVertexes) {
                Float valIn = inputValues.get(in);
                for (Vertex hi : hiddenVertexes) {
                    if (middleValues.containsKey(hi)) {
                        middleValues.replace(hi, middleValues.get(hi) + valIn);
                    } else {
                        middleValues.put(hi, valIn);
                    }
                }

            }
            middleValues = middleValues.entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), SigmoidFunction.compute(entry.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            for (Vertex hi : hiddenVertexes) {
                out += middleValues.get(hi);
            }
            out = SigmoidFunction.compute(out);
        }
        return out;
    }

    private Float propagateOneValue(List<Float> input, Float expected) {
        Float out = 0f;
        if (input.size() != structure.getNbInputLayer()) {
            throw new IllegalArgumentException("Input and input Layer size is different");
        } else {
            Map<Vertex, Float> inputValues = new HashMap<>();
            int i = 0;
            for (Vertex in : this.inputVertexes) {
                inputValues.put(in, input.get(i++));
            }
            Map<Vertex, Float> middleValues = new HashMap<>();

            for (Vertex in : inputVertexes) {
                Float valIn = inputValues.get(in);
                for (Vertex hi : hiddenVertexes) {
                    if (middleValues.containsKey(hi)) {
                        middleValues.replace(hi, middleValues.get(hi) + valIn);
                    } else {
                        middleValues.put(hi, valIn);
                    }
                }

            }
            middleValues = middleValues.entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), SigmoidFunction.compute(entry.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            for (Vertex hi : hiddenVertexes) {
                out += middleValues.get(hi);
            }
            out = SigmoidFunction.compute(out);
            AdjacencyMatrixGraph<Float> graph = new AdjacencyMatrixGraph(this.g);
            Float diff = out - expected;
            for (Vertex hi : this.hiddenVertexes) {
                Float w = graph.getValue(hi, this.outputVertexes);
                w -= this.alpha * w * middleValues.get(hi) * diff;
                this.g.setValue(hi, this.outputVertexes, w);
            }
            for (Vertex hi : this.hiddenVertexes) {
                for (Vertex in : this.inputVertexes) {
                    Float w = graph.getValue(in, hi);
                    w -= this.alpha * inputValues.get(in) * graph.getValue(hi, this.outputVertexes) * diff * middleValues.get(hi) * (1 - middleValues.get(hi));
                    this.g.setValue(in, hi, w);
                }
            }
        }
        return computeError(expected, out);
    }

    private Float computeError(Float expected, Float actual) {
        return Double.valueOf(0.5 * Math.pow(actual - expected, 2)).floatValue();
    }

    public Float getAlpha() {
        return alpha;
    }

    public void setAlpha(Float alpha) {
        this.alpha = alpha;
    }

    static class Structure {
        public Structure(int nbInputLayer, int nbHiddenLayer) {
            this.nbHiddenLayer = nbHiddenLayer;
            this.nbInputLayer = nbInputLayer;
        }

        private int nbHiddenLayer;
        private int nbInputLayer;

        public int getNbHiddenLayer() {
            return nbHiddenLayer;
        }

        public void setNbHiddenLayer(int nbHiddenLayer) {
            this.nbHiddenLayer = nbHiddenLayer;
        }

        public int getNbInputLayer() {
            return nbInputLayer;
        }

        public void setNbInputLayer(int nbInputLayer) {
            this.nbInputLayer = nbInputLayer;
        }
    }
}