package mg.jaona.ia;

public class VertexData {
    private Float sum;
    private Float output;
    private Float delta;

    public VertexData(Float sum, Float output, Float delta) {
        this.sum = sum;
        this.output = output;
        this.delta = delta;
    }

    public VertexData() {
        this.setOutput(0f);
        this.setSum(0f);
        this.setDelta(0f);
    }
    public void incrementSum(Float value){
        this.sum += value;
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

    public Float getDelta() {
        return delta;
    }

    public void setDelta(Float delta) {
        this.delta = delta;
    }
}
