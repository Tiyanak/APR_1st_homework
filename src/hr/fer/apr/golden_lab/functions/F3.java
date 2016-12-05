package hr.fer.apr.golden_lab.functions;

import java.util.*;

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
        return Math.pow(x[0]-2, 2) + Math.pow(x[1]+3, 2);
    }

    public double f3(double lamda, double[] v, double[] x) {
        return Math.pow(x[0] + lamda * v[0] - 2, 2) + Math.pow(x[1] + lamda * v[1] + 3, 2);
    }

    public double f3x1(double[] x){
        return 2 * (x[0] - 2);
    }

    public double f3x2(double[] x){
        return 2 * (x[1] + 3);
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

    @Override
    public double[] d(double[] x){
        List<Double> dervs = new ArrayList<>();

        dervs.add(f3x1(x));
        dervs.add(f3x2(x));

        double[] dervsArray = new double[dervs.size()];
        for(int i=0; i<dervs.size(); i++){
            dervsArray[i] = dervs.get(i);
        }

        return dervsArray;
    }

    public double[][] H(double[] x) {
        List<List<Double>> hess = new ArrayList<>();

        hess.add(new ArrayList<Double>(Arrays.asList(2.0, 0.0)));
        hess.add(new ArrayList<Double>(Arrays.asList(0.0, 2.0)));

        double[][] hesseova = new double[hess.size()][hess.get(0).size()];
        for (int i = 0; i < hess.size(); i++) {
            for (int j = 0; j < hess.get(i).size(); j++) {
                hesseova[i][j] = hess.get(i).get(j);
            }
        }

        return hesseova;
    }

}
