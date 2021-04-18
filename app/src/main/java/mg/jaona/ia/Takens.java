package mg.jaona.ia;

import mg.jaona.algo.Matrix2D;

import java.util.*;
import java.util.stream.Collectors;

public class Takens {
    private float[] data;
    private int decomp = 10;
    private float epsilon = 0.0001f;

    public Takens(float[] data) {
        this.data = data;
    }

    public Takens(float[] data, int decomp) {
        this.data = data;
        this.decomp = decomp;
    }

    public int firstLayerLength() throws Exception {
        List<Float> lambda = new ArrayList<>();
        for (int i = 0; i < data.length; i += decomp) {
            float[] d;
            if (i + decomp <= data.length) {
                d = Arrays.copyOfRange(data, i, i + decomp);
            } else {
                d = Arrays.copyOfRange(data, i, data.length);
            }
            Matrix2D m2 = Matrix2D.covariance(d);
            List<Float> eig = new Jacobi(m2).eig();
            lambda.addAll(eig);
            lambda.sort(Comparator.reverseOrder());
            for (float f : lambda) {
                System.out.println(Math.sqrt(f + 1));
            }
            // Apply sqrt(x + 1) function
            lambda = lambda.stream().map(aFloat -> Double.valueOf(Math.sqrt(aFloat + 1)).floatValue()).collect(Collectors.toList());

            float c1,c2;
            for (int j = 1; j < lambda.size(); j++) {
                c1 = lambda.get(j - 1);
                c2 = lambda.get(j);
                System.out.print(c1);
                System.out.print(" ");
                System.out.println(c2);
                if (Math.abs(c2 - c1) < epsilon) {
                    return j;
                }
            }
            lambda.clear();
        }
        return 1;
    }
}
