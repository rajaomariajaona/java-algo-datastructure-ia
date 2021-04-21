package mg.jaona.ia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VertexPMLTest {

    @Test
    void setValue() {
        VertexData vertexData = new VertexData(10f,20f);
        VertexPML<VertexData> v = new VertexPML<>("label");
        v.setValue(vertexData);
        assertEquals(vertexData.getOutput(), v.getValue().getOutput());
        assertEquals(vertexData.getSum(), v.getValue().getSum());
        vertexData.setSum(30f);
        vertexData.setOutput(40f);
        assertEquals(vertexData.getOutput(), v.getValue().getOutput());
        assertEquals(vertexData.getSum(), v.getValue().getSum());
    }
}