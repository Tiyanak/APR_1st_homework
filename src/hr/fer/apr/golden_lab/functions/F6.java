package hr.fer.apr.golden_lab.functions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor Farszky on 30.10.2016..
 */
public class F6 extends IFunctions {

    private Map<double[], Double> dotMap;

    public F6() {
        this.putNewF("f6");
        this.dotMap = new HashMap<>();
    }

    public double f6(double[] x) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i] * x[i];
        }

        return 0.5 + ((Math.sin(Math.sqrt(sum)) * Math.sin(Math.sqrt(sum)) - 0.5) / ((1 + 0.001 * sum) * (1 + 0.001 * sum)));

    }

    public double f6(double lamda, double[] v, double[] x) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(x[i] + lamda * v[i], 2);
        }

        return 0.5 + ((Math.pow(Math.sin(Math.sqrt(sum)), 2) - 0.5) / Math.pow(1 + 0.001 * sum, 2));
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
            this.count("f6");
            this.dotMap.put(x.clone(), f6(x.clone()));
            return f6(x.clone());
        }
    }

    @Override
    public double execute(double lamda, double[] v, double[] x) {
        this.count("f6");
        return f6(lamda, v, x);
    }
}
