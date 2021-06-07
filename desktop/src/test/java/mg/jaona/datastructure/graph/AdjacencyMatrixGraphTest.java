package mg.jaona.datastructure.graph;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyMatrixGraphTest {

    private int anInt;

    @Test
    void addVertex() {
        AdjacencyMatrixGraph<Vertex, Integer> g = new AdjacencyMatrixGraph();
        Vertex[] vertexes = new Vertex[]{new Vertex("a"),
                new Vertex("b"),
                new Vertex("c")};
        for (Vertex e : vertexes) {
            g.addVertex(e);
        }
        g.addVertex(new Vertex("c"));
        assertArrayEquals(g.getVertexes().toArray(), vertexes);
        Map<Vertex, Map<Vertex, Integer>> matrix = g.getMatrix();
        assertEquals(3, matrix.size(), "Vertex not added in matrix row");
        for (Vertex key : matrix.keySet()) {
            assertEquals(3, matrix.get(key).size(), "Vertex not added in column");
        }
    }

    @Test
    void deleteVertex() {
        deleteVertexUnlinked();
        deleteVertexLinked();
    }

    private void deleteVertexUnlinked() {
        Graph<Vertex, Integer> g = new AdjacencyMatrixGraph();
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
        AdjacencyMatrixGraph<Vertex, Integer> g = new AdjacencyMatrixGraph();
        for (Vertex e : vertexes) {
            g.addVertex(e);
        }
        for (Vertex key : list.keySet()) {
            for (Map.Entry<Vertex, Integer> target : list.get(key)) {
                g.addEdge(key, target.getKey(), target.getValue());
            }
        }
        g.deleteVertex(new Vertex("c"));
        Map<Vertex, Map<Vertex, Integer>> matrix = g.getMatrix();
        for (Vertex key : matrix.keySet()) {
            assertNotEquals(new Vertex("c"), key, "some links not deleted at row");
            assertFalse(matrix.get(key).containsKey(new Vertex("c")), "some links not deleted at col");
        }
    }

    @Test
    void addEdge() {
        assertThrows(IllegalArgumentException.class, () -> {
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

            AdjacencyMatrixGraph<Vertex, Integer> g = new AdjacencyMatrixGraph();
            for (Vertex e : vertexes) {
                g.addVertex(e);
            }
            for (Vertex key : list.keySet()) {
                for (Map.Entry<Vertex, Integer> target : list.get(key)) {
                    g.addEdge(key, target.getKey(), target.getValue());
                }
            }
        }, "Bidirectional edge should have the same value");


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
        AdjacencyMatrixGraph<Vertex, Integer> g = new AdjacencyMatrixGraph();
        for (Vertex e : vertexes) {
            g.addVertex(e);
        }
        for (Vertex key : list.keySet()) {
            for (Map.Entry<Vertex, Integer> target : list.get(key)) {
                if(g.getValue(target.getKey(), key) == null || g.getValue(target.getKey(), key).equals(g.getNullValue())){
                    g.addEdge(key, target.getKey(), target.getValue());
                }else{
                    g.addEdge(key, target.getKey(), g.getValue(target.getKey(), key));
                }
            }
        }
        Map<Vertex, Map<Vertex, Integer>> matrix = g.getMatrix();
        int size = list.size();
        assertEquals(matrix.size(), list.size(), "Different result");
        assertDoesNotThrow(() -> {
            for (Vertex key : matrix.keySet()) {
                assertEquals(matrix.get(key).size(), size);
            }
        }, "Different Result");
        assertThrows(IllegalArgumentException.class, () -> {
            g.addEdge(new Vertex("z"), new Vertex("a"), 100);
        }, "vertex not exist");
        assertThrows(IllegalArgumentException.class, () -> {
            g.addEdge(new Vertex("a"), new Vertex("aasg"), 100);
        }, "Vertex don't exist");
        assertThrows(IllegalArgumentException.class, () -> {
            g.addEdge(new Vertex("z"), new Vertex("f"), 100);
        }, "Vertex not exit");
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
        AdjacencyMatrixGraph<Vertex, Integer> g = new AdjacencyMatrixGraph();
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
            Graph<Vertex, Integer> g = new AdjacencyMatrixGraph<>(0);
            g.addVertex(new Vertex("a"));
            g.addVertex(new Vertex("b"));
            g.addEdge(new Vertex("a"), new Vertex("b"), 10);
            g.deleteEdge(new Vertex("g"), new Vertex("x"));
            g.deleteEdge(new Vertex("a"), new Vertex("b"));
            Map<Vertex, Map<Vertex, Integer>> matrix = ((AdjacencyMatrixGraph) g).getMatrix();
            assertEquals(Integer.valueOf(0), matrix.get(new Vertex("a")).get(new Vertex("b")));
        }, "Throw error should not throw");
    }

    @Test
    void getValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            Graph<Vertex, Integer> g = new AdjacencyMatrixGraph<>();
            g.getValue(new Vertex("v"), new Vertex("e"));
        }, "No Vertex");
        assertThrows(IllegalArgumentException.class, () -> {
            Graph<Vertex, Integer> g = new AdjacencyMatrixGraph<>();
            g.addVertex(new Vertex("a"));
            g.getValue(new Vertex("a"), new Vertex("b"));
        }, "Only Start Vertex");
        assertThrows(IllegalArgumentException.class, () -> {
            Graph<Vertex, Integer> g = new AdjacencyMatrixGraph<>();
            g.addVertex(new Vertex("b"));
            g.getValue(new Vertex("a"), new Vertex("b"));
        }, "Only End Vertex");
        assertDoesNotThrow(() -> {
            Graph<Vertex, Integer> g = new AdjacencyMatrixGraph<>();
            g.addVertex(new Vertex("a"));
            g.addVertex(new Vertex("b"));
            assertNull(g.getValue(new Vertex("a"), new Vertex("b")));
        }, "No Edge");
        assertDoesNotThrow(() -> {
            Graph<Vertex, Integer> g = new AdjacencyMatrixGraph<>();
            g.addVertex(new Vertex("a"));
            g.addVertex(new Vertex("b"));
            g.addEdge(new Vertex("a"), new Vertex("b"), 10);
            assertEquals(Integer.valueOf(10), g.getValue(new Vertex("a"), new Vertex("b")), "Not return correct value");
        }, "Normal get Value");
    }

    @Test
    void nullValue() {
        AdjacencyMatrixGraph<Vertex, Integer> g = new AdjacencyMatrixGraph<>();
        g.addVertex(new Vertex("a"));
        g.addVertex(new Vertex("b"));
        Map<Vertex, Map<Vertex, Integer>> matrix = g.getMatrix();
        for (Vertex i : matrix.keySet()) {
            for (Vertex j : matrix.get(i).keySet()) {
                assertNull(matrix.get(i).get(j), "Default null value is not Null");
            }
        }
        AdjacencyMatrixGraph<Vertex, Integer> g2 = new AdjacencyMatrixGraph<>(0);
        g2.addVertex(new Vertex("a"));
        g2.addVertex(new Vertex("b"));
        Map<Vertex, Map<Vertex, Integer>> matrix2 = g2.getMatrix();
        for (Vertex i : matrix2.keySet()) {
            for (Vertex j : matrix2.get(i).keySet()) {
                assertEquals(Integer.valueOf(0), matrix2.get(i).get(j), "Default null value is not set");
            }
        }
        g.setNullValue(0);
        for (Vertex i : matrix.keySet()) {
            for (Vertex j : matrix.get(i).keySet()) {
                assertEquals(Integer.valueOf(0), matrix.get(i).get(j), "Default null value is not change on set");
            }
        }
    }
    @Test
    void isConnected(){
        AdjacencyMatrixGraph<Vertex, Integer> g = new AdjacencyMatrixGraph<>();
        g.addVertex(new Vertex("a"));
        g.addVertex(new Vertex("b"));

        assertFalse(g.isConnected(new Vertex("a"), new Vertex("b")));
        assertFalse(g.isConnected(new Vertex("b"), new Vertex("a")));
        assertFalse(g.isConnected(new Vertex("b"), new Vertex("a"), true));

        g.addEdge(new Vertex("a"), new Vertex("b"), 10);
        assertTrue(g.isConnected(new Vertex("a"), new Vertex("b")));
        assertFalse(g.isConnected(new Vertex("b"), new Vertex("a")));
        assertFalse(g.isConnected(new Vertex("b"), new Vertex("a"), true));

        g.addEdge(new Vertex("b"), new Vertex("a"), 10);
        assertTrue(g.isConnected(new Vertex("a"), new Vertex("b")));
        assertTrue(g.isConnected(new Vertex("b"), new Vertex("a")));
        assertTrue(g.isConnected(new Vertex("b"), new Vertex("a"), true));
    }
}