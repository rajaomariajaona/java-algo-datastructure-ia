package mg.jaona.datastructure.graph;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VertexTest {
    @Test
    void isEqual(){
        assertEquals(new Vertex("COUCOU"), new Vertex("COUCOU"));
        assertEquals(new Vertex("g"), new Vertex("g"));
        assertNotEquals(new Vertex("Coucou"), new Vertex("COUCOU"));
        assertNotEquals(new Vertex("g"), new Vertex("h"));
    }

    @Test
    void testInsideSet(){
        Set<Vertex> vertexes = new HashSet<>();
        vertexes.add(new Vertex("lol"));
        vertexes.add(new Vertex("lol"));
        assertEquals(1, vertexes.size());
        Vertex v2 = new Vertex("lol2");
        vertexes.add(v2);
        assertEquals(2, vertexes.size());
    }

    @Test
    void isImmutable(){
        String label = "c";
        Vertex v = new Vertex(label);
        assertEquals("c", v.getLabel());
        label = "lol";
        assertEquals("c", v.getLabel());
    }
}