package mg.jaona.ia;

import mg.jaona.datastructure.graph.Vertex;

public class VertexPML<T> extends Vertex {
    private T value = null;
    /**
     * Constructor with parameter
     *
     * @param label The vertex label
     */
    public VertexPML(String label) {
        super(label);
    }

    public VertexPML(String label, T value) {
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
