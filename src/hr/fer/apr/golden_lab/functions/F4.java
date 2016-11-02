package hr.fer.apr.golden_lab.functions;

/**
 * Created by Igor Farszky on 30.10.2016..
 */
public class F4 extends IFunctions{

    public F4() {
        this.putNewF("f4");
    }

    public double f4(double[] x){
        return Math.abs((x[0] - x[1]) * (x[0] + x[1])) + Math.sqrt(x[0] * x[0] + x[1] * x[1]);
    }

    @Override
    public double execute(double x) {
        return 0.0;
    }

    @Override
    public double execute(double[] x) {
        this.count("f4");
        return f4(x);
    }
}
