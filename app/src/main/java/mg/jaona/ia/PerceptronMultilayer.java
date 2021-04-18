package mg.jaona.ia;

import mg.jaona.datastructure.graph.AdjacencyMatrixGraph;
import mg.jaona.datastructure.graph.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class PerceptronMultilayer {
    private Structure structure;
    private AdjacencyMatrixGraph<Float> g;
    private List<Vertex> inputVertexes;
    private List<Vertex> hiddenVertexes;
    private Vertex outputVertexes;
    private Float alpha = 0.1f;

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
        g.addVertex(this.outputVertexes);
    }

    public String generateNetworkVariableScript() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        sb.append("var network = {nodes: [");
        sb2.append("edges: [");
        for (int i = 0; i < this.inputVertexes.size(); i++) {
            sb.append("{id:'");
            sb.append(this.inputVertexes.get(i).getLabel());
            sb.append("',nr:");
            sb.append(i + 1);
            sb.append(",layer: 1},");
            for (Vertex hiddenVertex : this.hiddenVertexes) {

                sb2.append("{source: '");
                sb2.append(this.inputVertexes.get(i).getLabel());
                sb2.append("', target: '");
                sb2.append(hiddenVertex.getLabel());
                sb2.append("', weight:'");
                if (g.getNullValue() == null ? this.g.getValue(this.inputVertexes.get(i), hiddenVertex) == null : g.getNullValue().equals(this.g.getValue(this.inputVertexes.get(i), hiddenVertex))) {
                    sb2.append("0");
                } else {
                    sb2.append(String.format("%.8f", this.g.getValue(this.inputVertexes.get(i), hiddenVertex)));
                }
                sb2.append("'},");
            }
        }
        for (Vertex hiddenVertex : this.hiddenVertexes) {
            sb2.append("{source: '");
            sb2.append(hiddenVertex.getLabel());
            sb2.append("', target: '");
            sb2.append(this.outputVertexes.getLabel());
            sb2.append("', weight:'");
            if (g.getNullValue() == null ? this.g.getValue(hiddenVertex, this.outputVertexes) == null : g.getNullValue().equals(this.g.getValue(hiddenVertex, this.outputVertexes))) {
                sb2.append("0");
            } else {
                sb2.append(String.format("%.8f", this.g.getValue(hiddenVertex, this.outputVertexes)));
            }
            sb2.append("'},");
        }
        for (int j = 0; j < this.hiddenVertexes.size(); j++) {
            sb.append("{id:'");
            sb.append(this.hiddenVertexes.get(j).getLabel());
            sb.append("',nr:");
            sb.append(j + 1);
            sb.append(",layer: 2},");
        }
        sb.append("{id:'");
        sb.append(this.outputVertexes.getLabel());
        sb.append("',nr:");
        sb.append(1);
        sb.append(",layer: 3},],");
        sb2.append("]");
        sb.append(sb2);
        sb.append("};");
        return sb.toString();
    }

    public void fit(List<Float> serie, int epoch, float testRatio, NMSE.NMSECallback callback) {

        if (testRatio >= 1 || testRatio < 0) {
            throw new IllegalArgumentException("Test ration is not enough");
        }
        List<Float> testSerie = serie.stream().skip(Math.round(serie.size() * (1 - testRatio))).collect(Collectors.toList());
        List<Float> fitSerie = serie.stream().limit(Math.round(serie.size() * (1 - testRatio))).collect(Collectors.toList());
        while (epoch > 0) {
            Deque<Float> deque = new ArrayDeque<>(fitSerie);
            List<Float> expectedFit = new ArrayList<>();
            List<Float> actualFit = new ArrayList<>();
            List<Float> expectedTest = new ArrayList<>();
            List<Float> actualTest = new ArrayList<>();
            if (fitSerie.size() < structure.nbInputLayer) {
                throw new IllegalArgumentException("Part Serie is not enough");
            }
            while (deque.size() > structure.nbInputLayer) {
                List<Float> toPropagate = deque.stream().limit(structure.nbInputLayer + 1).collect(Collectors.toList());
                Float expected = toPropagate.remove(structure.nbInputLayer);
                Float actual = propagateOneValue(toPropagate, expected);
                expectedFit.add(expected);
                actualFit.add(actual);
                deque.removeFirst();
            }
            deque.addAll(testSerie);
            while (deque.size() > structure.nbInputLayer) {
                List<Float> toPropagate = deque.stream().limit(structure.nbInputLayer + 1).collect(Collectors.toList());
                Float expected = toPropagate.remove(structure.nbInputLayer);
                Float actual = forwardPropagation(toPropagate);
                expectedTest.add(expected);
                actualTest.add(actual);
                deque.removeFirst();
            }
            callback.pass(NMSE.compute(expectedFit, actualFit, expectedTest, actualTest));
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

    public void initializeWeight() {
        for (Vertex in : inputVertexes) {
            for (Vertex hi : hiddenVertexes) {
                this.g.addEdge(in, hi, Double.valueOf(Math.random()).floatValue());
            }
        }
        for (Vertex hi : hiddenVertexes) {
            this.g.addEdge(hi, outputVertexes, Double.valueOf(Math.random()).floatValue());
        }
    }

    public boolean isWeightInitialized() {
        if (g.getNullValue() == null) {
            return this.g.getValue(inputVertexes.get(random(inputVertexes.size())), hiddenVertexes.get(random(hiddenVertexes.size()))) != null;
        } else {
            return !this.g.getNullValue().equals(this.g.getValue(inputVertexes.get(random(inputVertexes.size())), hiddenVertexes.get(random(hiddenVertexes.size()))));
        }
    }

    private int random(int max) {
        return Double.valueOf(Math.floor(Math.random() * max)).intValue();
    }

    public Float forwardPropagation(List<Float> input) {
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
                        middleValues.replace(hi, middleValues.get(hi) + valIn * this.g.getValue(in, hi));
                    } else {
                        middleValues.put(hi, valIn * this.g.getValue(in, hi));
                    }
                }

            }
            middleValues = middleValues.entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(),
                    SigmoidFunction.compute(entry.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            for (Vertex hi : hiddenVertexes) {
                out += middleValues.get(hi) * this.g.getValue(hi,this.outputVertexes);
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
                        middleValues.replace(hi, middleValues.get(hi) + valIn * this.g.getValue(in, hi));
                    } else {
                        middleValues.put(hi, valIn * this.g.getValue(in, hi));
                    }
                }

            }
            middleValues = middleValues.entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(),
                    SigmoidFunction.compute(entry.getValue()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            for (Vertex hi : hiddenVertexes) {
                out += middleValues.get(hi) * this.g.getValue(hi,this.outputVertexes);
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
        return out;
    }

    public Float getAlpha() {
        return alpha;
    }

    public void setAlpha(Float alpha) {
        this.alpha = alpha;
    }

    public static class Structure {
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