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

    public void incrementSum(Float value){
        this.sum += value;
    }

    public Float getOutput() {
        return output;
    }

    public void setOutput(Float output) {
        this.output = output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VertexData that = (VertexData) o;

        if (!sum.equals(that.sum)) return false;
        return output.equals(that.output);
    }

    @Override
    public int hashCode() {
        int result = sum.hashCode();
        result = 31 * result + output.hashCode();
        return result;
    }
}
