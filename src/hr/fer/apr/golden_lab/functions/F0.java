package hr.fer.apr.golden_lab.functions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor Farszky on 31.10.2016..
 */
public class F0 extends IFunctions {

    private Map<double[], Double> dotMap;

    public F0() {
        this.putNewF("f0");
        this.dotMap = new HashMap<>();
    }

    public double f0(double[] x) {
        return Math.pow(x[0] - 1, 2) + Math.pow(x[1] + 2, 2) + Math.pow(x[2] - 1, 2);
    }

    public double f0(double lamda, double[] v, double[] x) {
        return Math.pow(x[0] + lamda * v[0], 2) + 4 * Math.pow(x[1] + lamda * v[1], 2);
    }

    @Override
    public double execute(double x) {
        return 0;
    }

    @Override
    public double execute(double[] x) {
        if (dotMap.containsKey(x.clone())) {
            return dotMap.get(x.clone());
        } else {
            this.count("f0");
            this.dotMap.put(x.clone(), f0(x.clone()));
            return f0(x.clone());
        }
    }

    @Override
    public double execute(double lamda, double[] v, double[] x) {
        this.count("f0");
        return f0(lamda, v, x);
    }

    @Override
    public double[] d(double[] x) {
        return new double[5];
    }

    @Override
    public double[][] H(double[] x) {
        return new double[0][];
    }
}

