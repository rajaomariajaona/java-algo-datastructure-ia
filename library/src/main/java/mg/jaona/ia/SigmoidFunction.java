package mg.jaona.ia;

public class SigmoidFunction {
    public static Float compute(Float x){
        return  Double.valueOf(1 / (1 + Math.exp(-x))).floatValue();
    }
    public static Float derivative(Float x) { return  compute(x) * (1 - compute(x));}
}
