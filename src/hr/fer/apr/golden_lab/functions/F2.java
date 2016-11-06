package hr.fer.apr.golden_lab.functions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor Farszky on 20.10.2016..
 */
public class F2 extends IFunctions {

    private Map<double[], Double> dotMap;

    public F2() {
        this.putNewF("f2");
        this.dotMap = new HashMap<>();
    }

    public double f2(double[] x) {
        return (x[0] - 4) * (x[0] - 4) + 4 * (x[1] - 2) * (x[1] - 2);
    }

    public double f2(double lambda, double[] v, double[] x) {
        return Math.pow((x[0] + lambda * v[0]) - 4, 2) + 4 * Math.pow((x[1] + lambda * v[1]) - 2, 2);
    }

    @Override
    public double execute(double x) {
        return 0.0;
    }

    @Override
    public double execute(double[] x) {
        if (this.dotMap.containsKey(x.clone())) {
            return this.dotMap.get(x.clone());
        } else {
            this.count("f2");
            this.dotMap.put(x.clone(), f2(x.clone()));
            return f2(x.clone());
        }
    }

    @Override
    public double execute(double lamda, double[] v, double[] x) {
        this.count("f2");
        return f2(lamda, v, x);
    }
}
