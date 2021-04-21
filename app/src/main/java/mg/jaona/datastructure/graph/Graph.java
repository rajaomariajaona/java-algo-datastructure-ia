package mg.jaona.datastructure.graph;

import java.util.Set;

/**
 * @param <V> The class representing the value manipulated inside Graph
 */
public interface Graph<T extends Vertex, V> {

    /**
     * @param v add Vertex inside Graph
     */
    void addVertex(T v);

    /**
     * @param v delete Vertex inside Graph and all links
     */
    void deleteVertex(T v);

    /**
     * Add Edge into graph and set his value
     *
     * @param start Start vertex
     * @param end   End vertex
     * @param value The value
     */
    void addEdge(T start, T end, V value);

    /**
     * Set value on Edge
     *
     * @param start Start vertex
     * @param end   End vertex
     * @param value The value
     */
    void setValue(T start, T end, V value);

    /**
     * Set value on Edge
     *
     * @param start Start vertex
     * @param end   End vertex
     * @return T The value
     */
    V getValue(T start, T end);

    /**
     * Do nothing if Start or End Vertex not exist
     * Delete Edge from Start to End
     *
     * @param start Start vertex
     * @param end   End vertex
     */
    void deleteEdge(T start, T end);

    /**
     * @return all Vertexes
     */
    Set<T> getVertexes();
}
