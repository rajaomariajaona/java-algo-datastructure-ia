package mg.jaona.ia;

import org.junit.jupiter.api.Test;

import javax.sound.midi.SysexMessage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PerceptronMultilayerTest {
    @Test
    void xorTest(){
        List<List<Float>> input = List.of(List.of(-1f,-1f),List.of(1f,1f),List.of(-1f,1f),List.of(1f,-1f));
        List<Float> output = List.of(1f, 0f, 1f, 0f);
        PerceptronMultilayer pmc = new PerceptronMultilayer(new PerceptronMultilayer.Structure(2, 2));
        pmc.initializeWeight();
        pmc.setAlpha(10f);
        for (int i = 0; i < 1000; i++) {
            if(true){
                pmc.propagateOneValue(input.get(i % 4), output.get(i % 4));
            }else{
                System.out.println(pmc.propagateOneValue(input.get(i % 4), output.get(i % 4)));
            }
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
        System.out.println(SigmoidFunction.compute(1.2f));
        System.out.println(SigmoidFunction.compute(0.507409908f));
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
        var pmc = new PerceptronMultilayer(new PerceptronMultilayer.Structure(2,2));
        pmc.initializeWeight(0.2f);
        assertEquals(Float.valueOf(0.6241991f), pmc.feedForward(List.of(2f,3f)));
        pmc.setAlpha(1f);
        for (int i = 0; i < 100; i++) {
            pmc.propagateOneValue(List.of(2f,3f), 0f);
        }
        System.out.println(pmc.feedForward(List.of(2f,3f)));
        System.out.println(pmc.feedForward(List.of(2f,3f)));

    }

    @Test
    void getAlpha() {
    }

    @Test
    void setAlpha() {
    }
}