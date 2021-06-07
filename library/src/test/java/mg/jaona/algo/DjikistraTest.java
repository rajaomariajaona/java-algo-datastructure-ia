package mg.jaona.algo;

import mg.jaona.datastructure.graph.AdjacencyMatrixGraph;
import mg.jaona.datastructure.graph.Graph;
import mg.jaona.datastructure.graph.Vertex;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DjikistraTest {
    @Test
    void shortPath() {
        Graph<Vertex, Double> graph = new AdjacencyMatrixGraph<>(0.0);
        List<Vertex> vertices = List.of(new Vertex("A"), new Vertex("B"), new Vertex("C"), new Vertex("D"), new Vertex("E"), new Vertex("F"), new Vertex("G"));
        graph.addVertices(vertices);
        graph.addEdge(new Vertex("A"), new Vertex("B"), 2.0, true);
        graph.addEdge(new Vertex("A"), new Vertex("D"), 4.0, true);
        graph.addEdge(new Vertex("A"), new Vertex("C"), 1.0, true);

        graph.addEdge(new Vertex("B"), new Vertex("C"), 2.0, true);
        graph.addEdge(new Vertex("B"), new Vertex("E"), 1.0, true);

        graph.addEdge(new Vertex("C"), new Vertex("D"), 3.0, true);
        graph.addEdge(new Vertex("C"), new Vertex("E"), 4.0, true);
        graph.addEdge(new Vertex("C"), new Vertex("F"), 5.0, true);

        graph.addEdge(new Vertex("D"), new Vertex("E"), 3.0, true);
        graph.addEdge(new Vertex("D"), new Vertex("F"), 1.0, true);

        graph.addEdge(new Vertex("E"), new Vertex("F"), 6.0, true);
        graph.addEdge(new Vertex("E"), new Vertex("G"), 5.0, true);

        graph.addEdge(new Vertex("F"), new Vertex("G"), 2.0, true);
        List<List<Vertex>> listRes = new ArrayList<>();
        listRes.add(List.of(new Vertex("A"), new Vertex("C"), new Vertex("D"), new Vertex("F"), new Vertex("G")));
        listRes.add(List.of(new Vertex("A"), new Vertex("D"), new Vertex("F"), new Vertex("G")));
        assertIterableEquals(listRes.get(1), (new Djikistra().shortPath(graph, new Vertex("A"), new Vertex("G"))).get(0));

//        assertThrows(IllegalArgumentException.class, () -> {
//           new Djikistra().shortPath(graph, new Vertex("Z"), new Vertex("X"));
//        }, "Start and End Vertex not exsit");
//        assertThrows(IllegalArgumentException.class, () -> {
//            new Djikistra().shortPath(graph, new Vertex("A"), new Vertex("X"));
//        }, "End Vertex not exsit");
//        assertThrows(IllegalArgumentException.class, () -> {
//            new Djikistra().shortPath(graph, new Vertex("Z"), new Vertex("G"));
//        }, "Start Vertex not exsit");
    }
}