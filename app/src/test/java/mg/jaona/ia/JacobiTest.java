package mg.jaona.ia;

import mg.jaona.algo.Matrix2D;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JacobiTest {

    @Test
    void eig() {

        assertThrows(IllegalArgumentException.class, () -> new Jacobi(new Matrix2D(new float[][]{{1, 2}, {3, 4}})));
        assertDoesNotThrow(() -> {

            jacobiTest(new float[][]{{5, 91, 22, 84, 8, 14, 19, 62, 15, 21},
                            {91, 43, 23, 30, 70, 64, 40, 82, 62, 72},
                            {22, 23, 65, 80, 76, 30, 80, 17, 28, 25},
                            {84, 30, 80, 41, 67, 33, 49, 37, 50, 97},
                            {8, 70, 76, 67, 98, 86, 50, 51, 5, 29},
                            {14, 64, 30, 33, 86, 96, 35, 84, 84, 9},
                            {19, 40, 80, 49, 50, 35, 80, 34, 98, 81},
                            {62, 82, 17, 37, 51, 84, 34, 83, 96, 48},
                            {15, 62, 28, 50, 5, 84, 98, 96, 83, 75},
                            {21, 72, 25, 97, 29, 9, 81, 48, 75, 1}},
                    new float[]
                            {
                                    533.28030998f, 140.5449223f, 120.73111635f, 89.28117088f,
                                    6.97216827f, -8.3048979f, -19.46017319f, -51.50022541f,
                                    -74.28090886f, -142.26348242f
                            });

            jacobiTest(new float[][]
                    {{21, 25, 12, 48, 46}, {25, 15, 38, 49, 45}, {12, 38, 36, 49, 39}, {48, 49, 49, 49, 30}, {46, 45, 39, 30, 3}}, new float[]{180.62900000f, 18.07800000f, -2.69500000f, -18.97600000f, -53.036f});
            jacobiTest(new float[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 9}}, new float[]{1.40000000e+01f, 3.58824538e-16f, -5.87239501e-16f});

        });


    }

    private void jacobiTest(float[][] matrix, float[] toBeAns) {
        Jacobi jacobi = new Jacobi(new Matrix2D(matrix));
        List<Float> res;
        try {
            res = jacobi.eig();
            float[] eig = new float[res.size()];
            int i = 0;
            for (Float r : res) {
                eig[i++] = r;
            }
            assertArrayEquals(toBeAns, eig, 0.1f, "The difference is greater than 0.1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}