package mg.jaona.algo;

import mg.jaona.datastructure.graph.Graph;
import mg.jaona.datastructure.graph.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class Djikistra {
    public List<List<Vertex>> shortPath(Graph g, Vertex start, Vertex end) {
        List<List<Vertex>> res = new ArrayList<>();
        Set<Vertex> vertices = g.getVertexes();
        if (!vertices.contains(start) || !vertices.contains(end)) {
            throw new IllegalArgumentException("Vertex not exist on graph");
        }
        Map<Vertex, AbstractMap.SimpleEntry<Vertex, Double>> dist = new HashMap<>();
        for (Vertex v : vertices) {
            dist.put(v, new AbstractMap.SimpleEntry<>(v, Double.POSITIVE_INFINITY));
        }
        Set<Vertex> markedVertices = new HashSet<>();
        Set<Vertex> unmarkedVertices = new HashSet<>(vertices);
        dist.replace(start, new AbstractMap.SimpleEntry<>(start, 0.0));
        markedVertices.add(start);
        unmarkedVertices.remove(start);

        while (!unmarkedVertices.isEmpty()) {
            Vertex curr = null;
            Vertex parent = null;
            double minValue = Double.POSITIVE_INFINITY;
            for (Vertex ma : markedVertices) {
                for (Vertex un : unmarkedVertices) {
                    if (g.isConnected(ma, un)) {
                        double value = dist.get(ma).getValue() + (Double) g.getValue(ma, un);
                        if (minValue > value) {
                            curr = un;
                            minValue = value;
                            parent = ma;
                        }
                    }
                }
            }
            dist.replace(curr, new AbstractMap.SimpleEntry<>(parent, minValue));
            markedVertices.add(curr);
            unmarkedVertices.remove(curr);
        }
        Vertex curr = end;
        Deque<Vertex> v = new ArrayDeque<>();
        v.addFirst(end);
        while(!curr.equals(start)){
            Vertex parent = dist.get(curr).getKey();
            v.addFirst(parent);
            curr = parent;
        }
        res.add(new ArrayList<>(v));
        return res;
    }
}
