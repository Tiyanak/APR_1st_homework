package hr.fer.apr.golden_lab.functions;

/**
 * Created by Igor Farszky on 31.10.2016..
 */
public class F0 extends IFunctions{

    public F0() {
        this.putNewF("f0");
    }

    public double f0(double[] x){
        return (x[0]-1) * (x[0]-1);
    }

    @Override
    public double execute(double x) {
        return 0;
    }

    @Override
    public double execute(double[] x) {
        this.count("f0");
        return f0(x);
    }
}
