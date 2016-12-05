package hr.fer.apr.golden_lab.functions;

import java.util.*;

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
        return Math.pow(x[0]-3, 2) + Math.pow(x[1], 2);
    }

    public double f4(double lamda, double[] v, double[] x) {
       return Math.pow(x[0]+lamda * v[0] - 3, 2) + Math.pow(x[1] + lamda * v[1], 2);
    }

    public double f4x1(double[] x){
        return 2 * (x[0] - 3);
    }

    public double f4x2(double[] x){
        return 2 * x[1];
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

    @Override
    public double[] d(double[] x) {
        List<Double> dervs = new ArrayList<>();

        dervs.add(f4x1(x));
        dervs.add(f4x2(x));

        double[] dervsArray = new double[dervs.size()];
        for(int i=0; i<dervs.size(); i++){
            dervsArray[i] = dervs.get(i);
        }

        this.countgrad("f4");

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

        this.counthess("f4");

        return hesseova;
    }

}
