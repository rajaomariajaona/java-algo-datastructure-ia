package mg.jaona.ia;

import mg.jaona.datastructure.graph.Vertex;

public class VertexPMC<T> extends Vertex {
    private T value = null;
    /**
     * Constructor with parameter
     *
     * @param label The vertex label
     */
    public VertexPMC(String label) {
        super(label);
    }

    public VertexPMC(String label, T value) {
        super(label);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
