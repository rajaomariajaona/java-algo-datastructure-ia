package mg.jaona.ia;


import java.util.List;

public class NMSE {
    private float test;
    private float fit;

    private NMSE(float test, float fit) {
        this.test = test;
        this.fit = fit;
    }

    public static NMSE compute(List<Float> expectedFit, List<Float> actualFit, List<Float> expectedTest, List<Float> actualTest) {
        if (expectedFit.size() != actualFit.size() || expectedTest.size() != actualTest.size()) {
            throw new IllegalArgumentException("ERROR value not corresponding");
        }
        return new NMSE(computeNMSE(expectedFit, actualFit), computeNMSE(expectedTest, actualTest));
    }

    private static float computeNMSE(List<Float> expected, List<Float> actual) {
        float variance = 0;
        float sum = 0;
        float mean = 0;
        int size = expected.size();
        for (int i = 0; i < size; i++) {
            sum += Math.pow(expected.get(i) - actual.get(i), 2);
            variance += Math.pow(expected.get(i), 2);
            mean += expected.get(i);
        }
        mean /= size;
        variance -= Math.pow(mean, 2);
        return (float) ((1.0 / (size * variance)) * sum);
    }

    public float getTest() {
        return test;
    }

    public void setTest(float test) {
        this.test = test;
    }

    public float getFit() {
        return fit;
    }

    public void setFit(float fit) {
        this.fit = fit;
    }

    @FunctionalInterface
    public interface NMSECallback {
        void pass(NMSE value);
    }

    @Override
    public String toString() {
        return "NMSE{" +
                "test=" + test +
                ", fit=" + fit +
                '}';
    }
}
