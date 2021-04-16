package mg.jaona.datastructure.graph;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyListGraphTest {

    private int anInt;

    @Test
    void addVertex() {
        Graph<Integer> g = new AdjacencyListGraph();
        Vertex[] vertexes = new Vertex[]{new Vertex("a"),
                new Vertex("b"),
                new Vertex("c")};
        for (Vertex e : vertexes) {
            g.addVertex(e);
        }
        g.addVertex(new Vertex("c"));
        assertArrayEquals(g.getVertexes().toArray(), vertexes);
    }

    @Test
    void deleteVertex() {
        deleteVertexUnlinked();
        deleteVertexLinked();
    }

    private void deleteVertexUnlinked() {
        Graph<Integer> g = new AdjacencyListGraph();
        Vertex[] vertexes = new Vertex[]{new Vertex("a"),
                new Vertex("b")};
        g.addVertex(new Vertex("c"));
        for (Vertex e : vertexes) {
            g.addVertex(e);
        }
        g.deleteVertex(new Vertex("c"));
        assertArrayEquals(g.getVertexes().toArray(), vertexes, "Delete not linked vertex");
    }

    private void deleteVertexLinked() {
        Vertex[] vertexes = new Vertex[]{new Vertex("a"),
                new Vertex("b"),
                new Vertex("c")};
        Map<Vertex, List<Map.Entry<Vertex, Integer>>> list = new HashMap<>();
        for (Vertex e : vertexes) {
            List<Map.Entry<Vertex, Integer>> edge = new ArrayList<>();
            for (Vertex f : vertexes) {
                edge.add(new AbstractMap.SimpleEntry(f, randInt(0, 40)));
            }
            list.put(e, edge);
        }
        AdjacencyListGraph<Integer> g = new AdjacencyListGraph();
        for (Vertex e : vertexes) {
            g.addVertex(e);
        }
        for (Vertex key : list.keySet()) {
            for (Map.Entry<Vertex, Integer> target : list.get(key)) {
                g.addEdge(key, target.getKey(), target.getValue());
            }
        }
        g.deleteVertex(new Vertex("c"));
        var listToCompare = g.getList();
        for (Vertex key : listToCompare.keySet()) {
            assertNotEquals(new Vertex("c"), key, "some links not deleted at main");
            for (Map.Entry<Vertex, Integer> target : listToCompare.get(key)) {
                assertNotEquals(new Vertex("c"), target.getKey(), "some links not deleted at link");
            }
        }
    }

    @Test
    void addEdge() {
        Vertex[] vertexes = new Vertex[]{new Vertex("a"),
                new Vertex("b"),
                new Vertex("c")};
        Map<Vertex, List<Map.Entry<Vertex, Integer>>> list = new HashMap<>();
        for (Vertex e : vertexes) {
            List<Map.Entry<Vertex, Integer>> edge = new ArrayList<>();
            for (Vertex f : vertexes) {
                edge.add(new AbstractMap.SimpleEntry(f, randInt(0, 40)));
            }
            list.put(e, edge);
        }
        AdjacencyListGraph<Integer> g = new AdjacencyListGraph();
        for (Vertex e : vertexes) {
            g.addVertex(e);
        }
        for (Vertex key : list.keySet()) {
            for (Map.Entry<Vertex, Integer> target : list.get(key)) {
                g.addEdge(key, target.getKey(), target.getValue());
            }
        }
        Map<Vertex, List<Map.Entry<Vertex, Integer>>> listToCompare = g.getList();
        assertEquals(listToCompare.size(), list.size(), "Different result");
        assertDoesNotThrow(() -> {
            for (Vertex key : list.keySet()) {
                assertEquals(listToCompare.get(key).size(), list.get(key).size());
                for (int i = 0; i < list.get(key).size(); i++) {
                    Map.Entry<Vertex, Integer> t1 = list.get(key).get(i);
                    Map.Entry<Vertex, Integer> t2 = listToCompare.get(key).get(i);
                    assertEquals(t1.getKey(), t2.getKey());
                    assertEquals(t1.getValue(), t2.getValue());
                }
            }
        }, "Different Result");
        assertThrows(IllegalArgumentException.class, () -> {
            g.addEdge(new Vertex("z"), new Vertex("a"), 100);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            g.addEdge(new Vertex("a"), new Vertex("aasg"), 100);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            g.addEdge(new Vertex("z"), new Vertex("f"), 100);
        });
    }

    private int randInt(int min, int max) {
        return Double.valueOf(Math.floor(Math.random() * (max - min) + min)).intValue();
    }

    @Test
    void setValue() {
        Vertex[] vertexes = new Vertex[]{new Vertex("a"),
                new Vertex("b"),
                new Vertex("c")};
        Map<Vertex, List<Map.Entry<Vertex, Integer>>> list = new HashMap<>();
        for (Vertex e : vertexes) {
            List<Map.Entry<Vertex, Integer>> edge = new ArrayList<>();
            for (Vertex f : vertexes) {
                edge.add(new AbstractMap.SimpleEntry(f, randInt(0, 40)));
            }
            list.put(e, edge);
        }
        AdjacencyListGraph<Integer> g = new AdjacencyListGraph();
        for (Vertex e : vertexes) {
            g.addVertex(e);
        }
        for (Vertex key : list.keySet()) {
            for (Map.Entry<Vertex, Integer> target : list.get(key)) {
                g.addEdge(key, target.getKey(), target.getValue());
            }
        }
        g.setValue(new Vertex("a"), new Vertex("b"), 100);
        assertEquals(Integer.valueOf(100), g.getValue(new Vertex("a"), new Vertex("b")));
    }

    @Test
    void deleteEdge() {
        assertDoesNotThrow(() -> {
            Graph<Integer> g = new AdjacencyListGraph<>();
            g.addVertex(new Vertex("a"));
            g.addVertex(new Vertex("b"));
            g.addEdge(new Vertex("a"), new Vertex("b"), 10);
            g.deleteEdge(new Vertex("g"), new Vertex("x"));
            g.deleteEdge(new Vertex("a"), new Vertex("b"));
            Map<Vertex, List> list = ((AdjacencyListGraph) g).getList();
            assertTrue(list.get(new Vertex("a")).isEmpty());
        }, "Throw error should not throw");
    }

    @Test
    void getValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            Graph<Integer> g = new AdjacencyListGraph<>();
            g.getValue(new Vertex("v"), new Vertex("e"));
        }, "No Vertex");
        assertThrows(IllegalArgumentException.class, () -> {
            Graph<Integer> g = new AdjacencyListGraph<>();
            g.addVertex(new Vertex("a"));
            g.getValue(new Vertex("a"), new Vertex("b"));
        }, "Only Start Vertex");
        assertThrows(IllegalArgumentException.class, () -> {
            Graph<Integer> g = new AdjacencyListGraph<>();
            g.addVertex(new Vertex("b"));
            g.getValue(new Vertex("a"), new Vertex("b"));
        }, "Only End Vertex");
        assertThrows(IllegalArgumentException.class, () -> {
            Graph<Integer> g = new AdjacencyListGraph<>();
            g.addVertex(new Vertex("a"));
            g.addVertex(new Vertex("b"));
            g.getValue(new Vertex("a"), new Vertex("b"));
        }, "No Edge");
        assertDoesNotThrow(() -> {
            Graph<Integer> g = new AdjacencyListGraph<>();
            g.addVertex(new Vertex("a"));
            g.addVertex(new Vertex("b"));
            g.addEdge(new Vertex("a"), new Vertex("b"), 10);
            assertEquals(Integer.valueOf(10), g.getValue(new Vertex("a"), new Vertex("b")), "Not return correct value");
        }, "Normal get Value");
    }
}