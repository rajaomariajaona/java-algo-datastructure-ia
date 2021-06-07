package mg.jaona.ia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SerieTemporelle {
    final static public float X0 = 0.1f;
    public static List<DataSet> compute500Values(float a, float x0){
        Float[] values = new Float[500];
        values[0] = x0;
        for (int i = 1; i < 500; i++) {
            values[i] = a * values[i - 1] * (1 - values[i-1]);
        }
        return IntStream.range(0, values.length).mapToObj(index -> new DataSet(index, values[index])).collect(Collectors.toList());
    }
    public static class DataSet{
        private int x = 0;
        private float y = 0;
        public  DataSet() {

        }
        public DataSet(int x, float y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return "DataSet{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
