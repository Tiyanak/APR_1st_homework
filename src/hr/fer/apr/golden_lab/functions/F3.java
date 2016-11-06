package hr.fer.apr.golden_lab.functions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor Farszky on 30.10.2016..
 */
public class F3 extends IFunctions {

    private Map<double[], Double> dotMap;

    public F3() {
        this.putNewF("f3");
        this.dotMap = new HashMap<>();
    }

    public double f3(double[] x) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum = sum + Math.pow(x[i] - i, 2);
        }

        return sum;
    }

    public double f3(double lamda, double[] v, double[] x) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum = sum + Math.pow(x[i] + lamda * v[i] - i, 2);
        }

        return sum;
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
            this.count("f3");
            this.dotMap.put(x.clone(), f3(x.clone()));
            return f3(x.clone());
        }
    }

    @Override
    public double execute(double lamda, double[] v, double[] x) {
        this.count("f3");
        return f3(lamda, v, x);

    }
}
