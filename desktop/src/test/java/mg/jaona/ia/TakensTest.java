package mg.jaona.ia;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TakensTest {

    @Test
    void firstLayerLength() throws Exception {
        assertDoesNotThrow(() -> {
            testFirstLayer(2, 2);
            testFirstLayer(4, 2);
        });
    }
    void testFirstLayer(int a, int res) throws Exception {
        float[] f = new float[500];
        f[0] = 0.1f;
        for (int i = 1; i < 500; i++) {
            f[i] = a * f[i - 1] *(1 - f[i - 1]);
        }
        assertEquals(res, (new Takens(f)).firstLayerLength());
    }
}