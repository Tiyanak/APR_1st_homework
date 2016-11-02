package hr.fer.apr.golden_lab.functions;

/**
 * Created by Igor Farszky on 30.10.2016..
 */
public class F6 extends IFunctions {

    public F6() {
        this.putNewF("f6");
    }

    public double f6(double[] x){
        double sum = 0.0;
        for(int i=0; i<x.length; i++){
            sum += x[i] * x[i];
        }

        return 0.5 + ((Math.sin(Math.sqrt(sum)) * Math.sin(Math.sqrt(sum)) - 0.5) / ((1 + 0.001 * sum) * (1 + 0.001 * sum)));

    }

    @Override
    public double execute(double x) {
        return 0.0;
    }

    @Override
    public double execute(double[] x) {
        this.count("f6");
        return f6(x);
    }
}
