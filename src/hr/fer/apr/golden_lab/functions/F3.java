package hr.fer.apr.golden_lab.functions;

/**
 * Created by Igor Farszky on 30.10.2016..
 */
public class F3 extends IFunctions{

    public F3() {
        this.putNewF("f3");
    }

    public double f3(double[] x){
        double sum = 0.0;
        for(int i=0; i<x.length; i++){
            sum = sum + (x[i] - i * 3) * (x[i] - i * 3);
        }

        return sum;
    }

    @Override
    public double execute(double x) {
        return 0.0;
    }

    @Override
    public double execute(double[] x) {
        this.count("f3");
        return f3(x);
    }
}
