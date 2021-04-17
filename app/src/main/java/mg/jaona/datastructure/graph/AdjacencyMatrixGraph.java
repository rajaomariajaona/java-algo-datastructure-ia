package mg.jaona.datastructure.graph;

import java.util.*;
import java.util.stream.Collectors;

public class AdjacencyMatrixGraph<T> implements Graph<T> {
    private Map<Vertex, Map<Vertex, T>> matrix;
    private T nullValue;

    public AdjacencyMatrixGraph(T nullValue) {
        matrix = new HashMap<>();
        this.nullValue = nullValue;
    }

    public AdjacencyMatrixGraph() {
        matrix = new HashMap<>();
        this.nullValue = null;
    }

    /**
     * @param v add Vertex inside Graph
     */
    @Override
    public void addVertex(Vertex v) {
        Set<Vertex> vertexes = matrix.keySet();
        matrix.put(v, new HashMap<>());
        for (Vertex key : vertexes) {
            matrix.get(v).put(key, nullValue);
            matrix.get(key).put(v, nullValue);
        }
        matrix.get(v).put(v, nullValue);
    }

    /**
     * @param v delete Vertex inside Graph and all links
     */
    @Override
    public void deleteVertex(Vertex v) {
        matrix.remove(v);
        for (Vertex i : matrix.keySet()) {
            matrix.get(i).remove(v);
        }
    }

    /**
     * Add Edge into graph and set his value
     *
     * @param start Start vertex
     * @param end   End vertex
     * @param value The value
     * @throws IllegalArgumentException if vertex not exist on graph
     */
    @Override
    public void addEdge(Vertex start, Vertex end, T value) {
        if(value.equals(nullValue)){
            throw new IllegalArgumentException("Edge not added because null value");
        }else{
            this.setValue(start,end,value);
        }
    }

    /**
     * Set value on Edge
     *
     * @param start Start vertex
     * @param end   End vertex
     * @param value The value
     * @throws IllegalArgumentException if Edge don't exist
     */
    @Override
    public void setValue(Vertex start, Vertex end, T value) {
        if (matrix.containsKey(start) && matrix.containsKey(end)) {
            matrix.get(start).replace(end, value);
        } else {
            throw new IllegalArgumentException("Edge don't exist");
        }
    }

    /**
     * Set value on Edge
     *
     * @param start Start vertex
     * @param end   End vertex
     * @return T The value
     * @throws IllegalArgumentException if Edge don't exist
     */
    @Override
    public T getValue(Vertex start, Vertex end) {
        if (matrix.containsKey(start) && matrix.containsKey(end)) {
            return matrix.get(start).get(end);
        } else {
            throw new IllegalArgumentException("Vertex don't exist");
        }
    }

    /**
     * Do nothing if Start or End Vertex not exist
     * Delete Edge from Start to End
     *
     * @param start Start vertex
     * @param end   End vertex
     */
    @Override
    public void deleteEdge(Vertex start, Vertex end) {
        if (matrix.containsKey(start) && matrix.containsKey(end)) {
            matrix.get(start).replace(end, nullValue);
        }
    }

    /**
     * @return all Vertexes
     */
    @Override
    public Set<Vertex> getVertexes() {
        return matrix.keySet();
    }

    public Map<Vertex, Map<Vertex, T>> getMatrix() {
        return matrix;
    }

    /**
     * Null Value is the value if there is no edge between two vertex
     *
     * @return null Value
     */
    public T getNullValue() {
        return nullValue;
    }

    /**
     * Null Value is the value if there is no edge between two vertex
     * It will change all value with old value to new one
     *
     * @param nullValue
     */
    public void setNullValue(T nullValue) {
        T oldValue = this.nullValue;
        for (Vertex i : this.matrix.keySet()) {
            for (Vertex j: this.matrix.get(i).keySet()) {
                if(this.matrix.get(i).get(j) == null ? oldValue == null : this.matrix.get(i).get(j).equals(oldValue)){
                    this.matrix.get(i).replace(j, nullValue);
                }
            }
        }
        this.nullValue = nullValue;
    }
}
