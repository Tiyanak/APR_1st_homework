package hr.fer.apr.golden_lab.functions;

import java.util.*;

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

    public double f2x1(double[] x){
        return 2 * (x[0] - 4);
    }

    public double f2x2(double[] x){
        return 8 * (x[1] - 2);
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

    @Override
    public double[] d(double[] x) {
        List<Double> dervs = new ArrayList<>();

        dervs.add(f2x1(x));
        dervs.add(f2x2(x));

        double[] dervsArray = new double[dervs.size()];
        for(int i=0; i<dervs.size(); i++){
            dervsArray[i] = dervs.get(i);
        }

        return dervsArray;
    }

    public double[][] H(double[] x) {
        List<List<Double>> hess = new ArrayList<>();

        hess.add(new ArrayList<Double>(Arrays.asList(2.0, 0.0)));
        hess.add(new ArrayList<Double>(Arrays.asList(0.0, 8.0)));

        double[][] hesseova = new double[hess.size()][hess.get(0).size()];
        for (int i = 0; i < hess.size(); i++) {
            for (int j = 0; j < hess.get(i).size(); j++) {
                hesseova[i][j] = hess.get(i).get(j);
            }
        }

        return hesseova;
    }

}
