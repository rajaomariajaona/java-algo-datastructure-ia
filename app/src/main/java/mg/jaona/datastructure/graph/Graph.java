package mg.jaona.datastructure.graph;

import java.util.List;
import java.util.Set;

/**
 * @param <T> The class representing the value manipulated inside Graph
 */
public interface Graph<T> {

    /**
     * @param v add Vertex inside Graph
     */
    void addVertex(Vertex v);

    /**
     * @param v delete Vertex inside Graph and all links
     */
    void deleteVertex(Vertex v);

    /**
     * Add Edge into graph and set his value
     *
     * @param start Start vertex
     * @param end   End vertex
     * @param value The value
     */
    void addEdge(Vertex start, Vertex end, T value);

    /**
     * Set value on Edge
     *
     * @param start Start vertex
     * @param end   End vertex
     * @param value The value
     */
    void setValue(Vertex start, Vertex end, T value);

    /**
     * Set value on Edge
     *
     * @param start Start vertex
     * @param end   End vertex
     * @return T The value
     */
    T getValue(Vertex start, Vertex end);

    /**
     * Do nothing if Start or End Vertex not exist
     * Delete Edge from Start to End
     *
     * @param start Start vertex
     * @param end   End vertex
     */
    void deleteEdge(Vertex start, Vertex end);

    /**
     * @return all Vertexes
     */
    Set<Vertex> getVertexes();
}
