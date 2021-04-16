package mg.jaona.datastructure.graph;

/**
 * Immutable class representing Graph vertexes
 */
public class Vertex {
    private String label;

    /**
     * Constructor with parameter
     * @param label The vertex label
     */
    public Vertex(String label) {
        this.label = label;
    }

    /**
     * Getter for label
     * @return label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param o The object to compare with current object
     * @return True if two object label are equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        return getLabel().equals(vertex.getLabel());
    }

    @Override
    public int hashCode() {
        return getLabel().hashCode();
    }

    @Override
    public String toString() {
        return "{label:'" + label + "'}";
    }
}
