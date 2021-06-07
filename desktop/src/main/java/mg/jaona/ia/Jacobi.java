package mg.jaona.ia;

import mg.jaona.algo.Matrix2D;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class Jacobi {
    private Matrix2D m;
    private float epsilon = 0.001f;
    private boolean isComputed = false;
    final private int MAX_ITER = 10000;

    public Jacobi(Matrix2D m) {
        this.m = new Matrix2D(m);
        if (!this.m.isSymetric())
            throw new IllegalArgumentException("Matrix is not symetric");
    }

    public Jacobi(Matrix2D m, float epsilon) {
        this.m = new Matrix2D(m);
        if (!this.m.isSymetric())
            throw new IllegalArgumentException("Matrix is not symetric");
        this.epsilon = epsilon;
    }

    private float computeValueToCompareWithEpsilon() {
        float value = 0f;
        for (int i = 0; i < this.m.getLength(); i++) {
            for (int j = 0; j < this.m.getLength(); j++) {
                if (i != j)
                    value += Math.abs(this.m.get(i, j));
            }
        }
        return value;
    }


    private int[] maxDiagonal() {
        int[] result = new int[]{0,1};
        for (int i = 0; i < this.m.getLength(); i++) {
            for (int j = 0; j < this.m.getLength(); j++) {
                if (i == j) break;
                if (Math.abs(this.m.get(result[0], result[1])) < Math.abs(this.m.get(i, j))) {
                    result[0] = i;
                    result[1] = j;
                }
            }
        }
        if (result[0] > result[1]) {
            int tmp = result[0];
            result[0] = result[1];
            result[1] = tmp;
        }
        return result;
    }

    private Matrix2D rotationMatrix() {

        int[] diagonals = this.maxDiagonal();
        int j = diagonals[0];
        int k = diagonals[1];
        float cosTheta, sinTheta;
        if (this.m.get(j, j) != this.m.get(k, k)) {
            float b = Math.abs(this.m.get(j, j) - this.m.get(k, k));
            float c = 2 * this.m.get(j, k) * (this.m.get(j, j) > this.m.get(k, k) ? 1 : -1);
            cosTheta = Double.valueOf(Math.sqrt((0.5f) * (1 + b / Math.sqrt(c * c + b * b)))).floatValue();
            sinTheta = Double.valueOf(c / (2 * cosTheta * Math.sqrt(c * c + b * b))).floatValue();
        } else {
            sinTheta = Double.valueOf(Math.sqrt(2) / 2).floatValue();
            cosTheta = sinTheta;
        }
        Matrix2D res = Matrix2D.id(this.m.getLength());
        res.set(k, j, sinTheta);
        res.set(j, j, cosTheta);
        res.set(j, k, sinTheta);
        res.set(k, k, cosTheta);
        res.set(j, k, -res.get(j, k));
        return res;
    }

    private void diagonalize() throws Exception {
        int i = 0;
        do {
            if (i++ > MAX_ITER) {
                throw new Exception("MAX ITER REACHED");
            }
            Matrix2D rot = this.rotationMatrix();
            Matrix2D transRot = Matrix2D.transpose(rot);
            Matrix2D copy = new Matrix2D(this.m);
            this.m = Matrix2D.dot(Matrix2D.dot(transRot, copy), rot);
        } while (this.computeValueToCompareWithEpsilon() > this.epsilon);
    }

    public void compute() throws Exception {
        this.diagonalize();
        isComputed = true;
    }

    public List<Float> eig() throws Exception {
        if (!isComputed) {
            this.diagonalize();
            isComputed = true;
        }
        List<Float> result = new ArrayList<>();
        for (int i = 0; i < this.m.getLength(); i++) {
            result.add(this.m.get(i, i));
        }
        result.sort(Comparator.reverseOrder());
        return result;
    }

}
