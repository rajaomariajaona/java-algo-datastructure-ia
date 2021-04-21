package mg.jaona.ia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VertexPMLTest {

    @Test
    void setValue() {
        VertexData vertexData = new VertexData(10f,20f);
        VertexData vertexData2 = new VertexData(100f,200f);
        VertexPML<VertexData> v = new VertexPML<>("label");
        VertexPML<VertexData> v2 = new VertexPML<>("label");
        v2.setData(vertexData2);
        v.setData(vertexData);
        assertEquals(vertexData.getOutput(), v.getData().getOutput());
        assertEquals(vertexData.getSum(), v.getData().getSum());
        vertexData.setSum(30f);
        vertexData.setOutput(40f);
        assertEquals(vertexData.getOutput(), v.getData().getOutput());
        assertEquals(vertexData.getSum(), v.getData().getSum());
        assertEquals(v, v2, "Vertex are not equal by label");
    }
}