package hr.fer.apr.golden_lab.functions;

import java.util.*;

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

    public double f1x1(double[] x) {
        return 2 * (200 * Math.pow(x[0], 2) - 200 * x[0] * x[1] + x[0] - 1);
    }

    public double f1x2(double[] x) {
        return 200 * (x[1] - Math.pow(x[0], 2));
    }

    private double dx1x1(double[] x){ return 2 * (400 * x[0] - 200 * x[1] + 1); }

    private double dx1x2(double[] x){ return -400 * x[0]; }

    private double dx2x2(double[] x){ return 200; }

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

    @Override
    public double[] d(double[] x) {
        List<Double> dervs = new ArrayList<>();

        dervs.add(f1x1(x));
        dervs.add(f1x2(x));

        double[] dervsArray = new double[dervs.size()];
        for (int i = 0; i < dervs.size(); i++) {
            dervsArray[i] = dervs.get(i);
        }

        this.countgrad("f1");

        return dervsArray;
    }

    public double[][] H(double[] x) {
        List<List<Double>> hess = new ArrayList<>();

        hess.add(new ArrayList<Double>(Arrays.asList(dx1x1(x), dx1x2(x))));
        hess.add(new ArrayList<Double>(Arrays.asList(dx1x2(x), dx2x2(x))));

        double[][] hesseova = new double[hess.size()][hess.get(0).size()];
        for (int i = 0; i < hess.size(); i++) {
            for (int j = 0; j < hess.get(i).size(); j++) {
                hesseova[i][j] = hess.get(i).get(j);
            }
        }

        this.counthess("f1");

        return hesseova;
    }

}
