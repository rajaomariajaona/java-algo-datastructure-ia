package mg.jaona.datastructure.matrix;

import java.util.Arrays;

public class Matrix {
    private float[][] data;
    public Matrix(float[][] data){
        check(data);
        // Clone array to avoid passing by reference
        this.data = Arrays.stream(data).map(float[]::clone).toArray(float[][]::new);
    }
    public Matrix(Matrix m){
        this.data = Arrays.stream(m.getData()).map(float[]::clone).toArray(float[][]::new);
    }
    public static Matrix id(MatrixSize ms){
        int row = ms.getRow();
        int col = ms.getCol();
        float[][] data = new float[row][col];
        for(int i = 0; i < (row < col ? row : col); i++){
            data[i][i] = 1;
        }
        return new Matrix(data);
    }

    public int getLength(){
        return data.length;
    }

    public static Matrix dot(Matrix m1, Matrix m2) {
        if (m1.getLength() != m2.getLength()){
            throw new ArithmeticException("Column length at matrix one is not corresponding at Row length at matrix two");
        }
        int len = m1.getLength();
        Matrix res = new Matrix(new float[len][len]);
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
    public static Matrix transpose(Matrix m) {
        Matrix res = new Matrix(m.getData());
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
        int colLen = data[0].length;
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

    public MatrixSize getSize(){
        return new MatrixSize(this.data.length,this.data[0].length);
    }

    @Override
    public boolean equals(Object obj) {
        if( data != null && obj instanceof Matrix){
            Matrix m = (Matrix) obj;
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
