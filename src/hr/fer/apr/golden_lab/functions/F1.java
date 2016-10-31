package hr.fer.apr.golden_lab.functions;

/**
 * Created by Igor Farszky on 19.10.2016..
 */
public class F1 extends IFunctions{

    public F1() {
    }

    public double f1(double[] x){
        return 100 * (x[1] - x[0]) * (x[1] - x[0]) + (1 - x[0]) * (1 - x[0]);
    }

    @Override
    public double execute(double x) {
        return 0.0;
    }

    @Override
    public double execute(double[] x) {
        return f1(x);
    }
}
