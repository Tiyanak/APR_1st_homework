package hr.fer.apr.golden_lab.functions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 02.12.2016..
 */
public abstract class Limits {

    private static double g1(double[] x){
        return x[1] - x[0];
    }

    private static double g2(double[] x){
        return 2 - x[0];
    }

    private static double g3(double[] x){
        return 3 - x[0] - x[1];
    }

    private static double g4(double[] x){
        return 3 + 1.5 * x[0] - x[1];
    }

    public static double expl(double x, double down, double top){
        if(x < down){
            return -1;
        }else if(x > top){
            return 1;
        }else{
            return 0;
        }
    }

    private static double h1(double[] x){
        return x[1] - 1;
    }

    public static double gLimits(int index, double[] x){
        List<Double> limits = new ArrayList<>();

        limits.add(g1(x));
        limits.add(g2(x));
        limits.add(g3(x));
        limits.add(g4(x));

        return limits.get(index);
    }

    public static double hLimits(int index, double[] x){
        List<Double> limits = new ArrayList<>();

        limits.add(h1(x));

        return limits.get(index);
    }

}
