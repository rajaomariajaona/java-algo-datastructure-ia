package mg.jaona.ia;

public class VertexData {
    private Float sum;
    private Float output;

    public VertexData(Float sum, Float output) {
        this.sum = sum;
        this.output = output;
    }

    public VertexData() {
        this.setOutput(0f);
        this.setSum(0f);
    }

    public Float getSum() {
        return sum;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }

    public Float getOutput() {
        return output;
    }

    public void setOutput(Float output) {
        this.output = output;
    }
}
