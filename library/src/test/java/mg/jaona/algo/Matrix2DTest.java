package mg.jaona.algo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Matrix2DTest {

    @Test
    void create() {
        assertDoesNotThrow(() -> {
            new Matrix2D(new float[][]{{1, 2}, {3, 4}});
        }, "Error in constructor");


        assertThrows(IllegalArgumentException.class, () -> new Matrix2D(new float[][]{{1, 2}, {3}}));

        assertThrows(IllegalArgumentException.class, () -> new Matrix2D(new float[][]{{1, 2}, {3, 4, 5}}));

        assertThrows(IllegalArgumentException.class, () -> new Matrix2D(new float[][]{{1}, {3, 4}}));

        assertThrows(IllegalArgumentException.class, () -> new Matrix2D(new float[][]{{1, 2, 4}, {3, 4}}));
    }

    @Test
    void id() {
        assertArrayEquals((new float[][]{{1}}), Matrix2D.id(1).getData());
        assertArrayEquals((new float[][]{{1, 0}, {0, 1}}), Matrix2D.id(2).getData());
        assertArrayEquals((new float[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}), Matrix2D.id(3).getData());
    }

    @Test
    void dot() {
        assertArrayEquals(new float[][]{{30, 24, 18}, {84, 69, 54}, {138, 114, 90}}, Matrix2D.dot(new Matrix2D(new float[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}), new Matrix2D(new float[][]{{9, 8, 7}, {6, 5, 4}, {3, 2, 1}})).getData());
        assertThrows(ArithmeticException.class ,() -> Matrix2D.dot(new Matrix2D(new float[][]{{1, 31}, {1, 2}}), new Matrix2D(new float[][]{{1, 2, 4}, {5, 2, 1}, {5, 2, 1}})));
    }

    @Test
    void substract() {
    }

    @Test
    void sum() {

    }

    @Test
    void symetric(){
        assertTrue(Matrix2D.id(3).isSymetric());
        assertTrue(Matrix2D.id(2).isSymetric());
        assertTrue(Matrix2D.id(1).isSymetric());
        assertTrue(new Matrix2D(new float[][]{{1, 2, 3}, {2, 4, 6}, {3, 6, 9}}).isSymetric());
        assertFalse(new Matrix2D(new float[][] {{1, 2, 4}, {2, 4, 6}, {3, 7, 9}}).isSymetric());
    }

    @Test
    void transpose() {
        assertArrayEquals(Matrix2D.transpose(new Matrix2D(new float[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}})).getData(),(new float[][]{{1, 4, 7},{2, 5, 8},{3, 6, 9}}));
        assertArrayEquals(Matrix2D.transpose(Matrix2D.id(2)).getData(),Matrix2D.id(2).getData());
    }

    @Test
    void equal() {
        float[][] d1 = {{1, 2, 3}, {1, 2, 3}, {1, 2, 4}};
        float[][] d2 = {{2, 2, 3}, {1, 2, 3}, {2, 2, 4}};
        Matrix2D m = new Matrix2D(d1);
        Matrix2D m2 = new Matrix2D(d1);
        Matrix2D m3 = new Matrix2D(d2);
        assertEquals(m2, m);
        assertEquals(m, m2);
        assertEquals(m, m);
        assertNotEquals(m3, m);
        assertNotEquals(m3, m2);
    }
    @Test
    void covariance(){
        assertEquals(Matrix2D.covariance(new float[]{2,4,5}), new Matrix2D(new float[][]{{4,8,10 }, {8 , 16, 20}, {10,20,25}}));
    }

}