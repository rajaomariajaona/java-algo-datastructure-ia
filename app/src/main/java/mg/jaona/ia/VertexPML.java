package mg.jaona.ia;

import mg.jaona.datastructure.graph.Vertex;

public class VertexPML<T> extends Vertex {
    private T data = null;
    /**
     * Constructor with parameter
     *
     * @param label The vertex label
     */
    public VertexPML(String label) {
        super(label);
    }

    public VertexPML(String label, T data) {
        super(label);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
