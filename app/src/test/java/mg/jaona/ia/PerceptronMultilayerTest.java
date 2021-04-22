package mg.jaona.ia;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PerceptronMultilayerTest {
    @Test
    void xorTest(){
        List<List<Float>> input = List.of(List.of(0f,0f),List.of(1f,1f),List.of(0f,1f),List.of(1f,0f));
        List<Float> output = List.of(1f, 1f, 0f, 0f);
        PerceptronMultilayer pmc = new PerceptronMultilayer(new PerceptronMultilayer.Structure(2, 2));
        pmc.initializeWeight();
        pmc.setAlpha(0.001f);
        for (int i = 0; i < 5000; i++) {
            pmc.propagateOneValue(input.get(i % 4), output.get(i % 4));
        }
        boolean[] res = new boolean[4];
        for (int i = 0; i < 4; i++) {
            System.out.println(output.get(i) + ":" + pmc.feedForward(input.get(i)));
            res[i] = ((pmc.feedForward(input.get(i)) > 0.5 ? 1f : 0f) == output.get(i));
        }
        assertArrayEquals(new boolean[]{true,true, true,true}, res);
    }
    @Test
    void generateNetworkVariableScript() {
    }

    @Test
    void fit() {
    }

    @Test
    void predict() {
    }

    @Test
    void initializeWeight() {
        var pmc = new PerceptronMultilayer(new PerceptronMultilayer.Structure(2, 3));
        pmc.initializeWeight();
        for (int i = 0; i < 100; i++) {
            assertTrue(pmc.isWeightInitialized());
        }
    }

    @Test
    void feedForward() {

    }

    @Test
    void getAlpha() {
    }

    @Test
    void setAlpha() {
    }
}