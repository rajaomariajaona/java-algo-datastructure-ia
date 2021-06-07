package mg.jaona.datastructure.matrix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    @Test
    void id() {
        assertEquals(new Matrix(new float[][]{{1,0,0}, {0,1,0}}), Matrix.id(new MatrixSize(2,3)));
        assertEquals(new Matrix(new float[][]{{1,0}, {0,1}, {0,0}}), Matrix.id(new MatrixSize(3,2)));
    }

    @Test
    void getSize() {
        assertTrue(Matrix.id(new MatrixSize(10,20)).getSize().equals(new MatrixSize(10,20)));
        assertTrue(Matrix.id(new MatrixSize(5,8)).getSize().equals(new MatrixSize(5,8)));
    }

    @Test
    void dot() {
        assertEquals(new Matrix(new float[][]{{58,64}, {139,154}}),Matrix.dot(new Matrix(new float[][]{{1,2,3}, {4, 5, 6}}), new Matrix(new float[][]{{7, 8}, {9, 10}, {11, 12}})));
    }

    @Test
    void set() {
    }

    @Test
    void get() {
    }

    @Test
    void transpose() {
    }

    @Test
    void getData() {
    }

    @Test
    void isSymetric() {
    }

    @Test
    void testEquals() {
    }
}