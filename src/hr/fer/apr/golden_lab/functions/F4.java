package hr.fer.apr.golden_lab.functions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor Farszky on 30.10.2016..
 */
public class F4 extends IFunctions {

    private Map<double[], Double> dotMap;

    public F4() {
        this.putNewF("f4");
        this.dotMap = new HashMap<>();
    }

    public double f4(double[] x) {
        return Math.abs((x[0] - x[1]) * (x[0] + x[1])) + Math.sqrt(x[0] * x[0] + x[1] * x[1]);
    }

    public double f4(double lamda, double[] v, double[] x) {
        double x1 = x[0] + lamda * v[0];
        double x2 = x[1] + lamda * v[1];
        return Math.abs((x1 - x2) * (x1 + x2)) + Math.sqrt(Math.pow(x1, 2) + Math.pow(x2, 2));
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
            this.count("f4");
            this.dotMap.put(x.clone(), f4(x.clone()));
            return f4(x.clone());
        }
    }

    @Override
    public double execute(double lamda, double[] v, double[] x) {
        this.count("f4");
        return f4(lamda, v, x);
    }
}
