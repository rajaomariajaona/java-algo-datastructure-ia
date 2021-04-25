package mg.jaona.datastructure.graph;

import java.util.List;
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
     * @param vs add Vertices inside Graph
     */
    default void addVertices(List<T> vs){
        for (T v: vs) {
            addVertex(v);
        }
    }

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
    default void addEdge(T start, T end, V value){
        this.addEdge(start, end, value, false);
    }

    /**
     * Add Edge into graph and set his value
     *
     * @param start Start vertex
     * @param end   End vertex
     * @param value The value
     * @param bidirectional if true add edge bidirectionally
     */
    void addEdge(T start, T end, V value, boolean bidirectional);

    /**
     * Set value on Edge
     *
     * @param start Start vertex
     * @param end   End vertex
     * @param value The value
     * @param bidirectional if true set value bidirectionally
     */
    void setValue(T start, T end, V value, boolean bidirectional);

    /**
     * Set value on Edge
     *
     * @param start Start vertex
     * @param end   End vertex
     * @param value The value
     */
    default void setValue(T start, T end, V value){
        setValue(start, end, value, false);
    }

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

    boolean isConnected(T start, T end, boolean bidirectional);
    default boolean isConnected(T start, T end){
        return isConnected(start, end, false);
    }

}
