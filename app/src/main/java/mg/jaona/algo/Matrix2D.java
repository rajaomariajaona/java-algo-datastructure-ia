package mg.jaona.algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matrix2D{
    private float[][] data;
    public Matrix2D(float[][] data){
        check(data);
        // Clone array to avoid passing by reference
        this.data = Arrays.stream(data).map(float[]::clone).toArray(float[][]::new);
    }
    public Matrix2D(Matrix2D m){
        this.data = Arrays.stream(m.getData()).map(float[]::clone).toArray(float[][]::new);
    }
    public static Matrix2D id(int len){
        float[][] data = new float[len][len];
        for(int i = 0; i < len; i++){
            data[i][i] = 1;
        }
        return new Matrix2D(data);
    }
    public static Matrix2D covariance(float[] d){
        int len = d.length;
        float[][] res = new float[len][len];
        for(int i = 0; i < len; i++){
            for (int j = 0; j < len; j++) {
                res[i][j] = d[i] * d[j];
            }
        }
        return new Matrix2D(res);
    }

    public int getLength(){
        return data.length;
    }

    public static Matrix2D dot(Matrix2D m1, Matrix2D m2) {
        if (m1.getLength() != m2.getLength()){
            throw new ArithmeticException("Column length at matrix one is not corresponding at Row length at matrix two");
        }
        int len = m1.getLength();
        Matrix2D res = new Matrix2D(new float[len][len]);
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                for (int k = 0; k < len; k++) {
                    res.set(i, j, res.get(i,j) + m1.get(i,k) * m2.get(k,j));
                }
            }
        }
        return res;
    }

    public void set(int i, int j, float d){
        this.data[i][j] = d;
    }
    public float get(int i, int j){
        return this.data[i][j];
    }
    public static Matrix2D transpose(Matrix2D m) {
        Matrix2D res = new Matrix2D(m.getData());
        for(int i = 0; i < m.getLength(); i++){
            for(int j = 0; j < m.getLength(); j++){
                if(i == j){
                    continue;
                }
                res.set(i,j, m.get(j,i));
            }
        }
        return res;
    }

    public float[][] getData() {
        return data;
    }

    private void check(float[][] data) throws IllegalArgumentException{
        int rowLen = data.length;
        int colLen = data[0].length;
        if(rowLen != colLen){
            throw new IllegalArgumentException("Matrix should be 2D");
        }
        for (float[] row : data) {
            if (row.length != colLen) {
                throw new IllegalArgumentException("Not correct matrix");
            }
        }
    }

    public boolean isSymetric(){
        for(int i = 0; i < this.getLength(); i++){
            for(int j = 0; j < this.getLength() / 2; j++){
                if(i == j){
                    continue;
                }
                if(this.get(i,j) != this.get(j,i)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if( data != null && obj instanceof Matrix2D){
            Matrix2D m = (Matrix2D) obj;
            float[][] data2 = m.getData();
            for(int i = 0; i < data.length; i++){
                for(int j = 0; j < data[i].length; j++){
                    if(this.data[i][j] != data2[i][j]){
                        return false;
                    }
                }
            }
                return true;
        }else{
            return false;
        }
    }
}
