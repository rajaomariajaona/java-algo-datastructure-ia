package mg.jaona.datastructure.graph;

import java.util.*;

public class AdjacencyMatrixGraph<T extends Vertex, V> implements Graph<T, V> {
    private Map<T, Map<T, V>> matrix;
    private V nullValue;

    public AdjacencyMatrixGraph(V nullValue) {
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
    public void addVertex(T v) {
        Set<T> vertexes = matrix.keySet();
        matrix.put(v, new HashMap<>());
        for (T key : vertexes) {
            matrix.get(v).put(key, nullValue);
            matrix.get(key).put(v, nullValue);
        }
        matrix.get(v).put(v, nullValue);
    }

    /**
     * @param v delete Vertex inside Graph and all links
     */
    @Override
    public void deleteVertex(T v) {
        matrix.remove(v);
        for (T i : matrix.keySet()) {
            matrix.get(i).remove(v);
        }
    }

    /**
     * Add Edge into graph and set his value
     *
     * @param start         Start vertex
     * @param end           End vertex
     * @param value         The value
     * @param bidirectional if true add edge bidirectionally
     * @throws IllegalArgumentException if vertex not exist on graph
     */
    @Override
    public void addEdge(T start, T end, V value, boolean bidirectional) {
        if (!bidirectional && value == null || value.equals(nullValue)) {
            throw new IllegalArgumentException("Edge not added because null value");
        } else if (this.getValue(end, start) != this.nullValue && !this.getValue(end, start).equals(value)) {
            throw new IllegalArgumentException("Bidirectional edge should have same value");
        } else {
            this.setValue(start, end, value);
            if (bidirectional) {
                this.setValue(end, start, value);
            }
        }
    }

    /**
     * Set value on Edge
     *
     * @param start         Start vertex
     * @param end           End vertex
     * @param value         The value
     * @param bidirectional if true set value bidirectionally
     * @throws IllegalArgumentException if Edge don't exist
     */
    @Override
    public void setValue(T start, T end, V value, boolean bidirectional) {
        if (matrix.containsKey(start) && matrix.containsKey(end)) {
            matrix.get(start).replace(end, value);
            if (bidirectional) {
                matrix.get(start).replace(end, value);
            }
        } else {
            throw new IllegalArgumentException("Edge don't exist");
        }
    }

    /**
     * Set value on Edge
     *
     * @param start Start vertex
     * @param end   End vertex
     * @return V2 The value
     * @throws IllegalArgumentException if Edge don't exist
     */
    @Override
    public V getValue(T start, T end) {
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
    public void deleteEdge(T start, T end) {
        if (matrix.containsKey(start) && matrix.containsKey(end)) {
            matrix.get(start).replace(end, nullValue);
        }
    }

    /**
     * @return all Vertexes
     */
    @Override
    public Set<T> getVertexes() {
        return matrix.keySet();
    }

    @Override
    public boolean isConnected(T start, T end, boolean bidirectional) {
        if(!getVertexes().contains(start) || !getVertexes().contains(end)){
            return false;
        }
        boolean res = !(this.matrix.get(start).get(end) == null ? getNullValue() == null : this.matrix.get(start).get(end).equals(this.getNullValue()));
        if (bidirectional) {
            res = res && !(this.matrix.get(end).get(start) == null ? getNullValue() == null : this.matrix.get(end).get(start).equals(this.getNullValue()));
        }
        return res;
    }

    public Map<T, Map<T, V>> getMatrix() {
        return matrix;
    }

    /**
     * Null Value is the value if there is no edge between two vertex
     *
     * @return null Value
     */
    public V getNullValue() {
        return nullValue;
    }

    /**
     * It will change all value with old value to new one
     *
     * @param nullValue is the value if there is no edge between two vertex
     */
    public void setNullValue(V nullValue) {
        V oldValue = this.nullValue;
        for (T i : this.matrix.keySet()) {
            for (T j : this.matrix.get(i).keySet()) {
                if (this.matrix.get(i).get(j) == null ? oldValue == null : this.matrix.get(i).get(j).equals(oldValue)) {
                    this.matrix.get(i).replace(j, nullValue);
                }
            }
        }
        this.nullValue = nullValue;
    }

}
