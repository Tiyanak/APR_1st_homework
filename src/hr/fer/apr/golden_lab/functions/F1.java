package hr.fer.apr.golden_lab.functions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor Farszky on 19.10.2016..
 */
public class F1 extends IFunctions {

    private Map<double[], Double> dotMap;

    public F1() {
        this.putNewF("f1");
        this.dotMap = new HashMap<>();
    }

    public double f1(double[] x) {
        return 100 * (x[1] - x[0] * x[0]) * (x[1] - x[0] * x[0]) + (1 - x[0]) * (1 - x[0]);
    }

    public double f1(double lambda, double[] v, double[] x) {
        return 100 * (Math.pow(x[1] + lambda * v[1] - Math.pow(x[0] + lambda * v[0], 2), 2)) + Math.pow(1 - (x[0] + lambda * v[0]), 2);
    }

    @Override
    public double execute(double x) {
        return 0.0;
    }

    @Override
    public double execute(double[] x) {
        if (dotMap.containsKey(x.clone())) {
            return dotMap.get(x.clone());
        } else {
            this.count("f1");
            this.dotMap.put(x.clone(), f1(x.clone()));
            return f1(x.clone());
        }
    }

    @Override
    public double execute(double lamda, double[] v, double[] x) {
        this.count("f1");
        return f1(lamda, v, x);
    }


}
