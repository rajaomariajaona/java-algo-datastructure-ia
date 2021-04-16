package mg.jaona.datastructure.graph;

import java.util.*;
import java.util.stream.Collectors;

public class AdjacencyListGraph<T> implements Graph<T> {
    private Set<Vertex> vertexes;
    private Map<Vertex, List<Map.Entry<Vertex, T>>> list;

    public AdjacencyListGraph() {
        vertexes = new HashSet<>();
        list = new HashMap<>();
    }

    /**
     * @param v add Vertex inside Graph
     */
    @Override
    public void addVertex(Vertex v) {
        vertexes.add(v);
        list.put(v, new ArrayList<>());
    }

    /**
     * @param v delete Vertex inside Graph and all links
     */
    @Override
    public void deleteVertex(Vertex v) {
        vertexes.forEach(vertex -> {
            this.deleteEdge(vertex, v);
        });
        vertexes.remove(v);
        list.remove(v);
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
        if (vertexes.contains(start) && vertexes.contains(end)) {
            list.get(start).add(new AbstractMap.SimpleEntry<Vertex, T>(end, value));
        } else {
            throw new IllegalArgumentException("Vertex not exist on graph");
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
        List<Map.Entry<Vertex, T>> edges = list.get(start);
        List<Map.Entry> res = edges.stream().filter(entry -> entry.getKey().equals(end)).limit(1).collect(Collectors.toList());
        if (res.size() == 0) {
            throw new IllegalArgumentException("Edge don't exist");
        } else if (res.size() == 1) {
            res.get(0).setValue(value);
        } else {
            throw new UnknownError("lol");
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
        if (vertexes.contains(start) && vertexes.contains(end)) {
            var edge = list.get(start).stream().filter(entry -> entry.getKey().equals(end)).limit(1).collect(Collectors.toList());
            if (edge.size() != 1) {
                throw new IllegalArgumentException("Edge don't exist");
            } else {
                return edge.get(0).getValue();
            }
        } else {
            throw new IllegalArgumentException("Vertex not exist on graph");
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
        if (vertexes.contains(start) && vertexes.contains(end)) {
            list.get(start).removeIf(entry -> entry.getKey().equals(end));
        }
    }

    /**
     * @return all Vertexes
     */
    @Override
    public Set<Vertex> getVertexes() {
        return vertexes;
    }

    /**
     * @return Adjacency List
     */
    public Map<Vertex, List<Map.Entry<Vertex, T>>> getList() {
        return list;
    }
}
