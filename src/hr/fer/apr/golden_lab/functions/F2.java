package hr.fer.apr.golden_lab.functions;

/**
 * Created by Igor Farszky on 20.10.2016..
 */
public class F2 extends IFunctions{

    public F2() {
        this.putNewF("f2");
    }

    public double f2(double[] x){
        return (x[0] - 4) * (x[0] - 4) + 4 * (x[1] - 2) * (x[1] - 2);
    }

    @Override
    public double execute(double x) {
        return 0.0;
    }

    @Override
    public double execute(double[] x) {
        this.count("f2");
        return f2(x);
    }
}
