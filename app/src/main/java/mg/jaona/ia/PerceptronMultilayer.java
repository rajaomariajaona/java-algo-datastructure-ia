package mg.jaona.ia;

import mg.jaona.datastructure.graph.AdjacencyMatrixGraph;

import java.util.*;
import java.util.stream.Collectors;

public class PerceptronMultilayer {
    private Structure structure;
    private AdjacencyMatrixGraph<VertexPML<VertexData>, Float> g;
    private List<VertexPML<VertexData>> inputVertexes;
    private List<VertexPML<VertexData>> hiddenVertexes;
    private VertexPML<VertexData> outputVertexes;
    private Float alpha = 0.1f;

    public PerceptronMultilayer(Structure structure) {
        this.structure = structure;
        this.inputVertexes = new ArrayList<>();
        this.hiddenVertexes = new ArrayList<>();
        this.outputVertexes = null;
        this.g = new AdjacencyMatrixGraph<>();
        for (int i = 0; i < structure.getNbInputLayer(); i++) {
            VertexPML<VertexData> v = new VertexPML<VertexData>("i" + i, new VertexData());
            this.inputVertexes.add(v);
            g.addVertex(v);
        }
        for (int i = 0; i < structure.getNbHiddenLayer(); i++) {
            VertexPML<VertexData> v = new VertexPML<VertexData>("h" + i, new VertexData());
            this.hiddenVertexes.add(v);
            g.addVertex(v);
        }
        this.outputVertexes = new VertexPML<VertexData>("o", new VertexData());
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
            for (VertexPML<VertexData> hiddenVertex : this.hiddenVertexes) {

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
        for (VertexPML<VertexData> hiddenVertex : this.hiddenVertexes) {
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
                Float actual = feedForward(toPropagate);
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
                Float pred = feedForward(new ArrayList<>(queueInput));
                res.add(pred);
                queueInput.removeLast();
                queueInput.addFirst(pred);
            }
        }
        return res;
    }

    public void initializeWeight() {
        for (VertexPML<VertexData> in : inputVertexes) {
            for (VertexPML<VertexData> hi : hiddenVertexes) {
                this.g.addEdge(in, hi, randomWeight());
            }
        }
        for (VertexPML<VertexData> hi : hiddenVertexes) {
            this.g.addEdge(hi, outputVertexes, randomWeight());
        }
    }

    private float randomWeight(){
        return Double.valueOf(Math.random()).floatValue();
//        return Double.valueOf(Math.random() * 0.001 + 0.00001).floatValue();
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

    private void resetSumVertexValues() {
        for (VertexPML<VertexData> v : this.hiddenVertexes) {
            v.getData().setSum(0f);
        }
        this.outputVertexes.getData().setSum(0f);
    }

    public Float feedForward(List<Float> input) {
        if (input.size() != this.structure.getNbInputLayer()) {
            throw new IllegalArgumentException("Input and input Layer size is different");
        } else {
            resetSumVertexValues();
            int i = 0;
            for (VertexPML<VertexData> in : this.inputVertexes) {
                in.getData().setOutput(input.get(i++));
            }
            for (VertexPML<VertexData> in : inputVertexes) {
                Float valIn = in.getData().getOutput();
                for (VertexPML<VertexData> hi : hiddenVertexes) {
                    hi.getData().incrementSum(valIn);
                }
            }
            for (VertexPML<VertexData> hi : hiddenVertexes) {
                hi.getData().setOutput(SigmoidFunction.compute(hi.getData().getSum()));
            }
            for (VertexPML<VertexData> hi : hiddenVertexes) {
                this.outputVertexes.getData().incrementSum(hi.getData().getOutput());
            }
            this.outputVertexes.getData().setOutput(SigmoidFunction.compute(this.outputVertexes.getData().getSum()));
            return this.outputVertexes.getData().getOutput();
        }
    }

    public Float propagateOneValue(List<Float> input, Float expected) {
        if (input.size() != structure.getNbInputLayer()) {
            throw new IllegalArgumentException("Input and input Layer size is different");
        } else {
            Float out = feedForward(input);
            float diff = expected - out;
            // hi = sum at output vertex
            float siDerivative = SigmoidFunction.derivative(this.outputVertexes.getData().getSum());
            this.outputVertexes.getData().setDelta(diff * siDerivative);
            for (VertexPML<VertexData> hi : this.hiddenVertexes) {
                // LOOP ON output vertexes if containing multiple output vertexes
                hi.getData().setDelta(this.outputVertexes.getData().getDelta() * this.g.getValue(hi, this.outputVertexes) * SigmoidFunction.derivative(hi.getData().getSum()));
            }
            updateWeight();
            return Double.valueOf(0.5 * Math.pow(diff, 2.0)).floatValue();
        }
    }

    private void updateWeight() {
        for (VertexPML<VertexData> hi : hiddenVertexes) {
            Float oldWeight = this.g.getValue(hi, this.outputVertexes);
            // delta in the output layer * Output at the last hidden layer * alpha
            this.g.setValue(hi, this.outputVertexes, oldWeight + this.alpha * this.outputVertexes.getData().getDelta() * hi.getData().getOutput());
        }

        // delta in the hidden layer * Output at the first layer (input if the input layer) * alpha
        for (VertexPML<VertexData> in : inputVertexes) {
            for (VertexPML<VertexData> hi : hiddenVertexes) {
                Float oldWeight = this.g.getValue(in, hi);
                this.g.setValue(in, hi, oldWeight + this.alpha * in.getData().getOutput() * hi.getData().getDelta());
            }
        }
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