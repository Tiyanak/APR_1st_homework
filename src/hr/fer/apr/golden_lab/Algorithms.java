package hr.fer.apr.golden_lab;

import com.sun.prism.Image;
import hr.fer.apr.golden_lab.functions.F1;
import hr.fer.apr.golden_lab.functions.IFunctions;
import hr.fer.apr.golden_lab.functions.Limits;
import hr.fer.apr.linear_algebra.IMatrix;
import hr.fer.apr.linear_algebra.Matrix;

import java.util.*;

/**
 * Created by Igor Farszky on 19.10.2016..
 */
@SuppressWarnings("all")
public class Algorithms {

    private final double k;
    private double[] l, r;
    private double h, e, alfa, beta, gama, pomak, sigma, t, p, pk, T;
    private List<Double> dg, gg;
    private IFunctions f;
    private int treshold, popsize, M, inacica, brojVarijabli, itNumber, tmin, tmax;
    private boolean binarniPrikaz;
    private List<Integer> brojBitova;
    private int[][] radijVektor;
    private double pradij;

    public Algorithms() {
        this.l = new double[1];
        this.r = new double[1];
        this.h = 1.0;
        this.e = 0.01;
        this.alfa = 1.3;
        this.beta = 0.5;
        this.gama = 2;
        this.pomak = 1.0;
        this.sigma = 0.5;
        this.t = 1;
        this.f = new F1();
        this.k = 0.5 * (Math.sqrt(5) - 1);
        this.treshold = 50000;
        this.popsize = 10;
        this.binarniPrikaz =  false;
        this.pk = 1.0;
        this.p = 0.1;
        this.M = 1;
        this.inacica = 3;
        this.dg = new ArrayList<>();
        this.gg = new ArrayList<>();
        this.brojBitova = new ArrayList<>();
        this.brojVarijabli = 2;
        this.pradij = 1.0;
        this.T = 1.0;
        this.tmin = 0;
        this.tmax = 30;
        this.itNumber = 1;

        initOgranicenja();

        this.radijVektor = new int[brojVarijabli][];
    }

    public void initOgranicenja(){

        for (int i=0; i<this.brojVarijabli; i++){
            this.dg.add(-50.0);
            this.gg.add((150.0));
        }

    }

    public void setRadijCustom(int[] vektor){
        for(int i=0; i<brojVarijabli; i++){
            this.radijVektor[i] = vektor;
        }
    }

    public void setLimits(double dg, double gg, int index, boolean all){
        if(all) {
            for (int i = 0; i < brojVarijabli; i++) {
                this.dg.set(i, dg);
                this.gg.set(i, gg);
            }
        }else{
            this.dg.set(index, dg);
            this.gg.set(index, gg);
        }
    }

    public double[] golden_cut(double[] a, double[] b) {

        System.out.println("ZLATNI REZ");
        System.out.println("a\t\tb\t\tc\t\td");

        double[] c = new double[a.length];
        double[] d = new double[a.length];

        for (int i = 0; i < a.length; i++) {
            c[i] = b[i] - k * (b[i] - a[i]);
            d[i] = a[i] + k * (b[i] - a[i]);
        }

        double fc = f.execute(c);
        double fd = f.execute(d);

        int i = 0;
        while (distance(a, b) > this.e) {
            String as = "";
            String bs = "";
            String cs = "";
            String ds = "";
            for (int k = 0; k < a.length; k++) {
                as += a[k] + ",";
                bs += b[k] + ",";
                cs += c[k] + ",";
                ds += d[k] + ",";
            }
            System.out.println(as + "\t" + cs + "\t" + ds + "\t" + bs);


            if (fc < fd) {
                b = d.clone();
                d = c.clone();
                for (int j = 0; j < a.length; j++) {
                    c[j] = b[j] - k * (b[j] - a[j]);
                }
                fd = fc;
                fc = f.execute(c);
            } else {
                a = c.clone();
                c = d.clone();
                for (int j = 0; j < a.length; j++) {
                    d[j] = a[j] + k * (b[j] - a[j]);
                }
                fc = fd;
                fd = f.execute(d);
            }

        }

        double[] ret = new double[a.length];
        for (int j = 0; j < a.length; j++) {
            ret[j] = (a[j] + b[j]) / 2.0;
        }

        String as = "", bs = "", cs = "", ds = "";
        for (int k = 0; k < a.length; k++) {
            as += a[k] + ",";
            bs += b[k] + ",";
            cs += c[k] + ",";
            ds += d[k] + ",";
        }
        System.out.println(as + "\t" + cs + "\t" + ds + "\t" + bs);

        return ret;

    }

    public void unimodalni(double[] tocka) {

        System.out.println("UNIMODALNI");
        System.out.println("l:\t\tr:");

        for (int i = 0; i < l.length; i++) {
            this.l[i] = 0.0;
            this.r[i] = 0.0;
        }

        for (int i = 0; i < l.length; i++) {
            this.l[i] = tocka[i] - this.h;
            this.r[i] = tocka[i] + this.h;
        }

        double[] m = tocka.clone();
        double fl, fm, fr;
        int step = 1;

        fm = f.execute(tocka);
        fl = f.execute(this.l);
        fr = f.execute(this.r);

        if (fm < fr && fm < fl) {

            return;

        } else if (fm > fr) {

            do {
                System.out.println(this.l[0] + "\t" + this.r[0]);

                this.l = m.clone();
                m = this.r.clone();
                fm = fr;
                for (int i = 0; i < l.length; i++) {
                    this.r[i] = tocka[i] + this.h * (step *= 2);
                }
                fr = f.execute(this.r);
            } while (fm > fr);

        } else {

            do {
                System.out.println(this.l[0] + "\t" + this.r[0]);

                this.r = m.clone();
                m = this.l.clone();
                fm = fl;
                for (int i = 0; i < l.length; i++) {
                    this.l[i] = tocka[i] - this.h * (step *= 2);
                }
                fl = f.execute(this.l);
            } while (fm > fl);
        }

        System.out.println(this.l[0] + "\t" + this.r[0]);

    }

    public double[] golden_cut(double[] a, double[] b, double[] v, double[] tocka) {


        double[] c = new double[a.length];
        double[] d = new double[a.length];

        for (int i = 0; i < a.length; i++) {
            c[i] = b[i] - k * (b[i] - a[i]);
            d[i] = a[i] + k * (b[i] - a[i]);
        }

        double fc = f.execute(c[0], v, tocka);
        double fd = f.execute(d[0], v, tocka);

        int i = 0;
        while (distance(a, b) > this.e) {
            String as = "";
            String bs = "";
            String cs = "";
            String ds = "";
            for (int k = 0; k < a.length; k++) {
                as += a[k] + ",";
                bs += b[k] + ",";
                cs += c[k] + ",";
                ds += d[k] + ",";
            }


            if (fc < fd) {
                b = d.clone();
                d = c.clone();
                for (int j = 0; j < a.length; j++) {
                    c[j] = b[j] - k * (b[j] - a[j]);
                }
                fd = fc;
                fc = f.execute(c[0], v, tocka);
            } else {
                a = c.clone();
                c = d.clone();
                for (int j = 0; j < a.length; j++) {
                    d[j] = a[j] + k * (b[j] - a[j]);
                }
                fc = fd;
                fd = f.execute(d[0], v, tocka);
            }

        }

        double[] ret = new double[a.length];
        for (int j = 0; j < a.length; j++) {
            ret[j] = (a[j] + b[j]) / 2.0;
        }


        return ret;

    }


    public void unimodalni(double lamda, double[] v, double[] tocka) {


        for (int i = 0; i < l.length; i++) {
            this.l[i] = 0.0;
            this.r[i] = 0.0;
        }

        this.l[0] = lamda - this.h;
        this.r[0] = lamda + this.h;

        double m = lamda;
        double fl, fm, fr;
        int step = 1;

        fm = f.execute(lamda, v, tocka);
        fl = f.execute(this.l[0], v, tocka);
        fr = f.execute(this.r[0], v, tocka);

        if (fm < fr && fm < fl) {

            return;

        } else if (fm > fr) {

            do {

                this.l[0] = m;
                m = this.r[0];
                fm = fr;
                this.r[0] = lamda + this.h * (step *= 2);
                fr = f.execute(this.r[0], v, tocka);
            } while (fm > fr);

        } else {

            do {

                this.r[0] = m;
                m = this.l[0];
                fm = fl;
                this.l[0] = lamda - this.h * (step *= 2);
                fl = f.execute(this.l[0], v, tocka);
            } while (fm > fl);
        }


    }


    public double[] koordinatni(double[] X0) {


        double[] x = X0.clone();
        double[] v = new double[X0.length];

        for (int i = 0; i < v.length; i++) {
            v[i] = 0.0;
        }

        double dist = 0.0;
        double[] xs = x.clone();
        do {
            String xString = "";
            for (double d : x) {
                xString += d + ", ";
            }
            xs = x.clone();
            for (int i = 0; i < xs.length; i++) {
                v[i] = 1.0;
                this.unimodalni(x[i], v, xs);
                double[] lam = golden_cut(this.l.clone(), this.r.clone(), v.clone(), x.clone());
                x[i] = x[i] + lam[0] * v[i];
                v[i] = 0.0;
            }
            dist = distance(x, xs);
        } while (dist > e);

        String xString = "";
        for (double d : x) {
            xString += d + ", ";
        }

        return x;
    }

    public double[] simplex(double[] X0) {

        System.out.println("SIMPLEX");
        System.out.println("Xc\t\tXr\t\tXe\t\tXk");

        double[][] X = new double[X0.length + 1][X0.length];

        X[0] = X0.clone();
        for (int i = 1; i < X0.length + 1; i++) {
            X[i] = X0.clone();
            X[i][i - 1] += pomak;
        }

        double[] Xc = new double[X0.length];
        double[] Xk = new double[X0.length];
        double[] Xe = new double[X0.length];
        double[] Xr = new double[X0.length];

        int h = 0;
        int l = 0;

        do {

            h = maxIndex(X);
            l = minIndex(X);

            Xc = centroid(X, h);

            Xr = refleksija(X, Xc, h).clone();

            if (f.execute(Xr) < f.execute(X[l])) {
                Xe = ekspancija(Xc, Xr).clone();

                if (f.execute(Xe) < f.execute(X[l])) {
                    X[h] = Xe.clone();
                } else {
                    X[h] = Xr.clone();
                }

            } else {

                int j = 0;
                // j = 0..n, j != h
                if (isBigger(Xr, X, h)) {
                    if (f.execute(Xr) < f.execute(X[h])) {
                        X[h] = Xr.clone();
                    }

                    Xk = kontrakcija(Xc, X, h).clone();

                    if (f.execute(Xk) < f.execute(X[h])) {
                        X[h] = Xk;
                    } else {
                        X = pomakPremaXl(X, l).clone();
                    }

                } else {
                    X[h] = Xr.clone();
                }

            }

            String qc = "", qr = "", qe = "", qk = "";
            for (int q = 0; q < Xc.length; q++) {
                qc += Xc[q] + ",";
                qe += Xe[q] + ",";
                qr += Xr[q] + ",";
                qk += Xk[q] + ",";
            }
            System.out.println(qc + "\t" + qr + "\t" + qe + "\t" + qk);

        } while (uvjet(X, Xc, h));

        String qc = "", qr = "", qe = "", qk = "";
        for (int q = 0; q < Xc.length; q++) {
            qc += Xc[q] + ",";
            qe += Xe[q] + ",";
            qr += Xr[q] + ",";
            qk += Xk[q] + ",";
        }
        System.out.println(qc + "\t" + qr + "\t" + qe + "\t" + qk);

        return Xc;

    }

    public double[] hookeJeves(double[] X0) {

        System.out.println("HOOKE-JEVES");
        System.out.println("XB\t\tXP\t\tXN");

        double[] Xp = X0.clone(), Xb = X0.clone();
        double[] Xn = new double[Xp.length];
        double Dx = 1.0;

        do {
            Xn = istrazi(Xp.clone(), Dx).clone();

            String qn = "", qb = "", qp = "";
            for (int q = 0; q < Xp.length; q++) {
                qn += Xn[q] + ",";
                qb += Xb[q] + ",";
                qp += Xp[q] + ",";

            }
            System.out.println(qb + "\t" + qp + "\t" + qn);

            if (f.execute(Xn) < f.execute(Xb)) {
                for (int i = 0; i < Xp.length; i++) {
                    Xp[i] = 2 * Xn[i] - Xb[i];
                }
                Xb = Xn.clone();
            } else {
                Dx = Dx / 2.0;
                Xp = Xb.clone();
            }
        } while (Dx > e); // dok nije zadovoljen uvjet

        String qn = "", qb = "", qp = "";
        for (int q = 0; q < Xp.length; q++) {
            qn += Xn[q] + ",";
            qb += Xb[q] + ",";
            qp += Xp[q] + ",";

        }
        System.out.println(qb + "\t" + qp + "\t" + qn);

        return Xb;

    }

    public double[] hookeJeves(double[] X0, int[] gs, int[] hs, double t) {

        double[] Xp = X0.clone(), Xb = X0.clone();
        double[] Xn = new double[Xp.length];
        double Dx = 1.0;

        do {
            Xn = istrazi(Xp.clone(), Dx, gs, hs, t).clone();

            if (mixfcilja(Xn, gs, hs, t) < mixfcilja(Xb, gs, hs, t)) {
                for (int i = 0; i < Xp.length; i++) {
                    Xp[i] = 2 * Xn[i] - Xb[i];
                }
                Xb = Xn.clone();
            } else {
                Dx = Dx / 2.0;
                Xp = Xb.clone();
            }
        } while (Dx > e); // dok nije zadovoljen uvjet

        return Xb;

    }

    private double mixfcilja(double[] x, int[] gs, int[] hs, double t){

        double sumg = 0.0;
        double sumh = 0.0;

        for(int i=0; i<gs.length; i++){
            double g = Limits.gLimits(gs[i], x);
            if(g < 0){
                sumg += -9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999.0;
            }else if(g == 0){
                sumg += -99999999999999999999999999999999999999999999999999999999999999999999999999.0;
            }else{
                sumg += Math.log(g);
            }
        }

        for(int i=0; i<hs.length; i++){
            double h = Limits.hLimits(hs[i], x);
            sumh += Math.pow(h, 2);
        }

        return f.execute(x) - (1/t) * sumg + t * sumh;

    }

    public double[] istrazi(double[] Xp, double Dx) {
        double[] x = Xp.clone();
        int n = x.length; // ne znam neki n

        for (int i = 0; i < n; i++) {
            double P = f.execute(x.clone());
            x[i] = x[i] + Dx;
            double N = f.execute(x.clone());

            if (N > P) {
                x[i] = x[i] - 2 * Dx;
                N = f.execute(x.clone());
                if (N > P) {
                    x[i] = x[i] + Dx;
                }
            }
        }

        return x.clone();
    }

    public double[] istrazi(double[] Xp, double Dx, int[] gs, int[] hs, double t) {
        double[] x = Xp.clone();
        int n = x.length; // ne znam neki n

        for (int i = 0; i < n; i++) {
            double P = mixfcilja(x, gs, hs, t);
            x[i] = x[i] + Dx;
            double N = mixfcilja(x, gs, hs, t);

            if (N > P) {
                x[i] = x[i] - 2 * Dx;
                N = mixfcilja(x, gs, hs, t);
                if (N > P) {
                    x[i] = x[i] + Dx;
                }
            }
        }

        return x.clone();
    }

    public double[] istrazi(double[] Xp, double Dx, int[] gs) {
        double[] x = Xp.clone();
        int n = x.length; // ne znam neki n

        for (int i = 0; i < n; i++) {
            double P = untockafcilja(x, gs);
            x[i] = x[i] + Dx;
            double N = untockafcilja(x, gs);

            if (N > P) {
                x[i] = x[i] - 2 * Dx;
                N = untockafcilja(x, gs);
                if (N > P) {
                    x[i] = x[i] + Dx;
                }
            }
        }

        return x.clone();
    }

    private double distance(double[] a, double[] b) {
        double dist = 0.0;
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(b[i] - a[i], 2);
        }
        dist = Math.sqrt(sum);

        return dist;
    }

    private int maxIndex(double[][] x) {
        double max = f.execute(x[0]);
        int maxIndex = 0;
        if (x.length > 1) {
            for (int i = 1; i < x.length; i++) {
                if (f.execute(x[i]) > max) {
                    max = f.execute(x[i]);
                    maxIndex = i;
                }
            }
        }

        return maxIndex;
    }

    private int minIndex(double[][] x) {
        double min = f.execute(x[0]);
        int minIndex = 0;
        if (x.length > 1) {
            for (int i = 1; i < x.length; i++) {
                if (f.execute(x[i]) < min) {
                    min = f.execute(x[i]);
                    minIndex = i;
                }
            }
        }

        return minIndex;
    }

    private double[] centroid(double[][] x, int h) {
        double[] c = new double[x[0].length];
        for (int i = 0; i < c.length; i++) {
            c[i] = 0.0;
        }

        for (int i = 0; i < x[0].length; i++) {
            for (int j = 0; j < x.length; j++) {
                if (j != h) {
                    c[i] += x[j][i];
                }
            }
            c[i] /= x.length - 1;
        }

        return c;
    }

    private double[] centroid(List<double[]> x) {
        double[] c = new double[x.get(0).length];
        for (int i = 0; i < c.length; i++) {
            c[i] = 0.0;
        }

        for (int i = 0; i < x.get(0).length; i++) {
            for (int j = 0; j < x.size(); j++) {
                c[i] += x.get(j)[i];
            }

            c[i] /= x.size();
        }

        return c;
    }

    private double[] refleksija(double[][] x, double[] xc, int h) {
        double[] xr = new double[xc.length];
        for (int i = 0; i < xc.length; i++) {
            xr[i] = (1 + alfa) * xc[i] - alfa * x[h][i];
        }

        return xr;
    }

    private double[] refleksija(List<double[]> x, double[] xc, int h) {
        double[] xr = new double[xc.length];
        for (int i = 0; i < xc.length; i++) {
            xr[i] = (1 + alfa) * xc[i] - alfa * x.get(h)[i];
        }

        return xr;
    }

    private double[] ekspancija(double[] xc, double[] xr) {
        double[] xe = new double[xc.length];

        for (int i = 0; i < xe.length; i++) {
            xe[i] = (1 - gama) * xc[i] + gama * xr[i];
        }

        return xe;
    }

    private boolean isBigger(double[] xr, double[][] x, int h) {

        for (int i = 0; i < x.length; i++) {
            if (i != h) {
                if (f.execute(xr) < f.execute(x[i])) {
                    return false;
                }
            }
        }

        return true;
    }

    private double[] kontrakcija(double[] xc, double[][] x, int h) {
        double[] xk = new double[xc.length];

        for (int i = 0; i < xc.length; i++) {
            xk[i] = (1 - beta) * xc[i] + beta * x[h][i];
        }

        return xk;
    }

    private double[][] pomakPremaXl(double[][] x, int l) {
        double[] xl = x[l].clone();

        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                x[i][j] = (x[i][j] + xl[j]) * sigma;
            }
        }

        return x;
    }

    private boolean uvjet(double[][] x, double[] xc, int h) {
        if (distance(x[h], xc) > e) {
            return true;
        }

        return false;
    }

    private boolean uvjet(List<double[]> x, double[] xc, int h) {
        if (distance(x.get(h), xc) > e) {
            return true;
        }

        return false;
    }

    public double[] gradijent(double[] x, boolean opt) {

        double[] v = new double[x.length];

        int counter = 0;
        double min = f.execute(x);
        double[] ret = x.clone();
        do {

            v = f.d(x);

            for (int i = 0; i < v.length; i++) {
                v[i] *= -1;
            }

            if (opt) {

                double[] xs = x.clone();

                for (int i = 0; i < xs.length; i++) {
                    this.unimodalni(x[i], v, xs);
                    double[] lam = golden_cut(this.l.clone(), this.r.clone(), v.clone(), x.clone());
                    x[i] = x[i] + lam[0] * v[i];
                }

            } else {

                for (int i = 0; i < v.length; i++) {
                    x[i] += v[i];
                }

            }

            if (f.execute(x) < min) {
                min = f.execute(x);
                counter = 1;
                ret = x.clone();
            } else {
                counter++;
            }

        } while (norm(v) > e && counter < 100);

        return ret;

    }

    public double norm(double[] x) {
        double sum = 0.0;

        for (int i = 0; i < x.length; i++) {
            sum += Math.pow(x[i], 2);
        }

        return Math.sqrt(sum);

    }

    public double[] nr(double[] x, boolean opt) {

        double[] v = new double[x.length];

        int counter = 0;
        double min = f.execute(x);
        double[] ret = x.clone();
        do {

            v = hessxgrad(x);

            for (int i = 0; i < v.length; i++) {
                v[i] *= -1;
            }

            if (opt) {

                double[] xs = x.clone();

                for (int i = 0; i < xs.length; i++) {
                    this.unimodalni(x[i], v, xs);
                    double[] lam = golden_cut(this.l.clone(), this.r.clone(), v.clone(), x.clone());
                    x[i] = x[i] + lam[0] * v[i];
                }

            } else {

                for (int i = 0; i < v.length; i++) {
                    x[i] += v[i];
                }

            }

            if (f.execute(x) < min) {
                min = f.execute(x);
                counter = 1;
                ret = x.clone();
            } else {
                counter++;
            }

        } while (norm(v) > e && counter < 100);

        return ret;

    }

    private double[] hessxgrad(double[] x){

        IMatrix matrixHess = new Matrix();

        matrixHess.setRowsCount(x.length);
        matrixHess.setColsCount(x.length);

        double[][] hess = f.H(x);
        matrixHess.setElements(hess);

        IMatrix matrixGrad = new Matrix();

        double[] grad = f.d(x);

        matrixGrad.setColsCount(1);
        matrixGrad.setRowsCount(grad.length);

        for(int i=0; i<matrixGrad.getRowsCount(); i++){
            matrixGrad.setElement(i, 0, grad[i]);
        }

        int[] P = matrixHess.LUP();
        int k = 0;
        matrixGrad.SF(matrixHess, P, true);
        matrixGrad.SB(matrixHess);

        for(int i=0; i<matrixGrad.getRowsCount(); i++){
            grad[i] = matrixGrad.getElement(i, 0);
        }

        return grad;

    }

    public double[] box(double[] x, double down, double top, int[] imps) {

        Random R = new Random();

        // pregledaj explicitna ogranicenja i baci tocku na granicu ako nije dobra
        for (int i = 0; i < x.length; i++) {
            if (Limits.expl(x[i], down, top) < 0) {
                x[i] = down;
            } else if (Limits.expl(x[i], down, top) > 0) {
                x[i] = top;
            } else {
                // nothing
            }
        }

        // ako ne zadovoljava implicintna gotovo
        for (int i = 0; i < imps.length; i++) {
            if (Limits.gLimits(imps[i], x) < 0) {
                System.out.println("Tocka ne zadovoljava implicintna ogranicnenja!");
                return new double[]{666.0};
            }
        }

        double[] Xc = x.clone();
        List<double[]> dots = new ArrayList<>();
        dots.add(Xc);

        // Generiraj 2*n tocaka
        for (int t = 0; t < 2 * x.length; t++) {
            double[] xt = new double[x.length];
            for (int i = 0; i < x.length; i++) {
                double r = R.nextDouble();
                xt[i] = down + r * (top - down);
            }

            boolean zadovoljavaimpl = false;
            int counterZad1 = 0;
            while (!zadovoljavaimpl) {
                boolean nezad = false;
                for (int j = 0; j < imps.length; j++) {
                    if (Limits.gLimits(j, xt) < 0) {
                        xt = pomakC(xt, Xc);
                        zadovoljavaimpl = false;
                        nezad = true;
                        break;
                    }
                }

                if (!nezad) {
                    zadovoljavaimpl = true;
                }
                if (counterZad1 > 100) {
                    System.out.println("Too many times moved to centroid and still nothing");
                    return new double[]{666.0};
                } else {
                    counterZad1++;
                }

            }

            dots.add(xt);

            Xc = centroid(dots);

        }

        int h = 0;
        int h2 = 0;

        do {
            h = findH(dots);
            h2 = findH2(dots);

            Xc = centroid(dots, h);

            double[] Xr = refleksija(dots, Xc, h);

            // zadovolji explicitna
            for (int i = 0; i < x.length; i++) {
                if (Limits.expl(Xr[i], down, top) < 0) {
                    Xr[i] = down;
                } else if (Limits.expl(Xr[i], down, top) > 0) {
                    Xr[i] = top;
                } else {
                    // nothing
                }
            }

            // zadovolji implicintna
            boolean zadovoljavaimpl = false;
            int counterZad = 0;
            while (!zadovoljavaimpl) {
                boolean nezad = false;
                for (int j = 0; j < imps.length; j++) {
                    if (Limits.gLimits(j, Xr) < 0) {
                        Xr = pomakC(Xr, Xc);
                        zadovoljavaimpl = false;
                        nezad = true;
                        break;
                    }
                }

                if (!nezad) {
                    zadovoljavaimpl = true;
                }

                if (counterZad > 100) {
                    System.out.println("Too many times moved to centroid and still nothing");
                    return new double[]{666.0};
                } else {
                    counterZad++;
                }
            }

            if(f.execute(Xr) > f.execute(dots.get(h2))){
                Xr = pomakC(Xr, Xc);
            }

            dots.set(h, Xr.clone());

        }while (uvjet(dots, Xc, h));

        return Xc;

    }

    private double[] pomakC(double[] x, double[] Xc) {
        double[] noviX = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            noviX[i] = 0.5 * (x[i] + Xc[i]);
        }

        return noviX;
    }

    private int findH(List<double[]> x) {
        int index = 0;
        double max = -999999;

        for (int i = 0; i < x.size(); i++) {
            if (f.execute(x.get(i)) > max) {
                max = f.execute(x.get(i));
                index = i;
            }
        }

        return index;
    }

    private int findH2(List<double[]> x) {
        int index = 0;
        int index2 = 0;
        double max = -999999;

        for (int i = 0; i < x.size(); i++) {
            if (f.execute(x.get(i)) > max) {
                max = f.execute(x.get(i));
                index = i;
            }
        }

        max = -999999;

        for (int i = 0; i < x.size(); i++) {
            if (i != index) {
                if (f.execute(x.get(i)) > max) {
                    max = f.execute(x.get(i));
                    index2 = i;
                }
            }
        }

        return index2;
    }

    private double[] centroid(List<double[]> x, int h) {
        double[] c = new double[x.get(0).length];
        for (int i = 0; i < c.length; i++) {
            c[i] = 0.0;
        }

        for (int i = 0; i < x.get(0).length; i++) {
            for (int j = 0; j < x.size(); j++) {
                if (j != h) {
                    c[i] += x.get(j)[i];
                }
            }
            c[i] /= (x.size() - 1);
        }

        return c;
    }

    public double[] transform(double[] x, int[] gs, int[] hs){

        double[] xs = null;

        int counter = 0;
        double min = 9999999999.0;
        double[] ret = x.clone();
        do{

            xs = x.clone();
            x = provjeriogr(x, gs);
            x = hookeJeves(x, gs, hs, this.t);

            if (mixfcilja(x, gs, hs, t) < min) {
                min = mixfcilja(x, gs, hs, t);
                counter = 1;
                ret = x.clone();
            } else {
                counter++;
            }

            this.t += 10;

        }while(distance(x, xs) > e && counter < 100);

        this.t = 1;

        return x;

    }

   public double[] provjeriogr(double[] x, int[] gs){

        boolean zad = true;
        for(int i=0; i<gs.length; i++){

            if(Limits.gLimits(gs[i], x) < 0){
                zad = false;
                break;
            }

        }

        if(zad){
            return x;
        }

        double[] Xp = x.clone(), Xb = x.clone();
        double[] Xn = new double[Xp.length];
        double Dx = 1.0;

        do {
            Xn = istrazi(Xp.clone(), Dx, gs).clone();

            if (untockafcilja(Xn, gs) < untockafcilja(Xb, gs)) {
                for (int i = 0; i < Xp.length; i++) {
                    Xp[i] = 2 * Xn[i] - Xb[i];
                }
                Xb = Xn.clone();
            } else {
                Dx = Dx / 2.0;
                Xp = Xb.clone();
            }
        } while (Dx > e); // dok nije zadovoljen uvjet

        return Xb;

    }

    private double untockafcilja(double[] x, int[] gs){

        double fsum = 0.0;

        for(int i=0; i<gs.length; i++){
            int t = 0;
            double g = Limits.gLimits(gs[i], x);
            if(g >= 0){
                t = 0;
            }else{
                t = 1;
            }

            fsum += t * g;

        }

        return fsum * -1;

    }

    public double[][] GA(){

        int tresholdCopy = this.treshold;

        List<double[]> populacija = stvoriPopulaciju();
        Map<double[][], Double> fitnessPopulacija = new HashMap<>();

        fitnessPopulacija = evaulirajPopulaciju(populacija);

        do{
            Map<double[][], Double> jedinke = odaberiSlucajno(fitnessPopulacija);
            double[][] najlosija = izbaciNajlosiju(jedinke);

            fitnessPopulacija.remove(najlosija);
            jedinke.remove(najlosija);

            List<double[][]> roditelji = izaberiRoditelje(jedinke);
            double[][] dijete = krizajUniformno(roditelji);
            dijete = mutiraj(dijete);
            Double fitnessNoveJedinke = fitness(dijete);

            fitnessPopulacija.put(dijete, fitnessNoveJedinke);

            tresholdCopy--;

        }while(tresholdCopy > 0 && fitnessPopulacija.get(najboljaJedinka(fitnessPopulacija)) > this.e);

        double[][] najbolja = najboljaJedinka(fitnessPopulacija);

        if(binarniPrikaz){
            System.out.println("Najbolja jedinka: " );
            for(double[] d: najbolja){
                String bitovniPrikaz = "";
                for(double db: d){
                    bitovniPrikaz += db + ",  ";
                }
                System.out.println(bitovniPrikaz);
            }
            System.out.println("Value: " + fitness(najbolja));

            return najbolja;

        }else{
            System.out.println("Najbolja jedinka: ");
            int i = 0;
            double[] h = new double[this.brojVarijabli];
            String dekPrikaz = "";
            for(double[] d: najbolja){
                double o = binUdek(d, i);
                h[i] = o;
                dekPrikaz += o + ", ";
                i++;
            }
            System.out.println(dekPrikaz);
            System.out.println("Value: " + this.f.execute(h));

            double[][] ret = new double[this.brojVarijabli][1];
            int q = 0;
            for(double p: h){
                ret[q][0] = p;
                q++;
            }

            return ret;
        }

    }

    public List<double[]> stvoriPopulaciju(){

        List<double[]> populacija = new ArrayList<>();
        Random r = new Random();

        for(int i=0; i<popsize; i++){

            double[] jedinka = new double[this.brojVarijabli];

            for(int j=0; j<this.brojVarijabli; j++){

                double range = this.gg.get(j) - this.dg.get(j);
                jedinka[j] = ((r.nextDouble() * (range)) + this.dg.get(j));

            }

            populacija.add(jedinka);

        }

        return populacija;

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public List<Integer> potrebniBrojBitova(){
        List<Integer> brojBitova = new ArrayList<>();

        for(int i=0; i<this.brojVarijabli; i++){
            int x = (int) ((this.gg.get(i) - this.dg.get(i)) / this.e);
            brojBitova.add(nadiMinBrojBitova(x));

            this.radijVektor[i] = napraviRadijVektor(brojBitova.get(i));
        }

        this.brojBitova = brojBitova;
        return this.brojBitova;

    }

    public int[] napraviRadijVektor(int brojBitova){

        int[] radij = new int[brojBitova];
        Random r = new Random();

        for(int i=0; i<brojBitova; i++){
            if(r.nextDouble() <= pradij){
                radij[i] = 1;
            }else{
                radij[i] = 0;
            }
        }

        return radij;

    }

    public int nadiMinBrojBitova(int x){

        int bitovi = 0;
        int sum = 0;
        while(sum < x){
            bitovi++;
            sum += Math.pow(2, bitovi-1);
        }

        return bitovi;

    }

    public Map<double[][], Double> evaulirajPopulaciju(List<double[]> populacija){

        Map<double[][], Double> evoPopulacija = new HashMap<>();

        List<Integer> n = potrebniBrojBitova();

        for(int i=0; i<populacija.size(); i++){
            double[][] binarnaJedinka = new double[this.brojVarijabli][n.size()];
            for(int j=0; j<this.brojVarijabli; j++){
                binarnaJedinka[j] = pretvoriUbinarno(populacija.get(i)[j], n.get(j), j);
            }
            evoPopulacija.put(binarnaJedinka, this.f.execute(populacija.get(i)));
        }

        return evoPopulacija;

    }

    public double[] pretvoriUbinarno(double x, int n, int j){
        int bin = (int)Math.round(((x - this.dg.get(j)) / (this.gg.get(j) - this.dg.get(j))) * (Math.pow(2, n) - 1));

        double[] binarno = new double[n];

        for(int i=n-1; i>=0; i--){
            if(bin - Math.pow(2, i) > 0){
                binarno[n-1-i] = 1;
                bin -= Math.pow(2, i);
            }else{
                binarno[n-1-i] = 0;
            }
        }

        return binarno;

    }

    public Map<double[][], Double> odaberiSlucajno(Map<double[][], Double> fitnessPopulacija){

        int counter = fitnessPopulacija.size();
        Random r = new Random();
        Map<double[][], Double> odabir = new HashMap<>();
        List<double[][]> mapaUlistu = new ArrayList<>();

        for(Map.Entry<double[][], Double> e: fitnessPopulacija.entrySet()){
            mapaUlistu.add(e.getKey());
        }

        while(odabir.size() < inacica){
            double[][] randJedinka = mapaUlistu.get((int)r.nextDouble() * (mapaUlistu.size() - 1));

            odabir.put(randJedinka, fitnessPopulacija.get(randJedinka));

            mapaUlistu.remove(randJedinka);
        }


        return odabir;

    }

    public double[][] izbaciNajlosiju(Map<double[][], Double> najlosija){

        double[][] najlosijaKey = new double[this.brojVarijabli][];
        Double najlosijaValue = -99999999.0;
        for(Map.Entry<double[][], Double> entry: najlosija.entrySet()){

            if(entry.getValue() > najlosijaValue){
                najlosijaKey = entry.getKey();
                najlosijaValue = entry.getValue();
            }

        }

        return najlosijaKey;

    }

    public List<double[][]> izaberiRoditelje(Map<double[][], Double> jedinke){

        Random r = new Random();

        double totalFitness = 0.0;
        for(Map.Entry<double[][], Double> entry: jedinke.entrySet()){
            totalFitness += entry.getValue();
        }

        Map<double[][], Double> pomMap = new HashMap<>();
        double totalFit2 = 0.0;
        for(Map.Entry<double[][], Double> entry: jedinke.entrySet()) {
            double secondFit = totalFitness - entry.getValue();
            pomMap.put(entry.getKey(), secondFit);
            totalFit2 += secondFit;
        }

        Map<double[][], double[]> vjerojatnosti = new HashMap<>();

        double gornjaVjerojatnost = 0.0;
        double donjaVjerojatnost = 0.0;
        for(Map.Entry<double[][], Double> entry: pomMap.entrySet()){
            double vjerojatnost = entry.getValue() / totalFit2;

            gornjaVjerojatnost += vjerojatnost;

            if(gornjaVjerojatnost > 1.0){
                gornjaVjerojatnost = 1.0;
            }

            vjerojatnosti.put(entry.getKey(), new double[]{donjaVjerojatnost, gornjaVjerojatnost});

            donjaVjerojatnost = gornjaVjerojatnost;

        }

        List<double[][]> roditelji = new ArrayList<>();

        double prviVj = r.nextDouble();
        double[][] prviRoditelj = new double[dg.size()][];
        for(Map.Entry<double[][], double[]> entry: vjerojatnosti.entrySet()){
            if(entry.getValue()[0] < prviVj && entry.getValue()[1] > prviVj){
                prviRoditelj = entry.getKey();
                roditelji.add(prviRoditelj);
                break;
            }
        }

        vjerojatnosti.remove(prviRoditelj);

        while(true){
            boolean zastavica = false;
            double drugiVj = r.nextDouble();
            for(Map.Entry<double[][], double[]> entry: vjerojatnosti.entrySet()){
                if(entry.getValue()[0] < drugiVj && entry.getValue()[1] > drugiVj){
                    roditelji.add(entry.getKey());
                    zastavica = true;
                    break;
                }
            }

            if(zastavica){
                break;
            }
        }

        return roditelji;

    }

    public double[][] krizaj(List<double[][]> jedinke){

        double[][] dijete = new double[brojVarijabli][];
        Random r = new Random();

        double[][] prvi = jedinke.get(0);
        double[][] drugi = jedinke.get(1);
        for(int i=0; i<brojVarijabli; i++){
            double[] bitovi = new double[this.brojBitova.get(i)];

            if(M < this.brojBitova.get(i)){
                int cutPeace = (int) this.brojBitova.get(i) / (M+1);

                int kojiRoditelj = 2;
                int sumBitovaPrije = 0;
                int sumBitovaPoslije = cutPeace;
                while(sumBitovaPoslije <= this.brojBitova.get(i)){

                    if(this.brojBitova.get(i) - sumBitovaPoslije < cutPeace){
                        sumBitovaPoslije = this.brojBitova.get(i);
                    }

                    double vjKrizanja = r.nextDouble();
                    if(pk >= 1.0) {
                        if (kojiRoditelj % 2 == 0) {
                            for (int j = sumBitovaPrije; j < sumBitovaPoslije; j++) {
                                bitovi[j] = prvi[i][j];
                            }
                        } else {
                            for (int j = sumBitovaPrije; j < sumBitovaPoslije; j++) {
                                bitovi[j] = drugi[i][j];
                            }
                        }
                    }else{
                        if (vjKrizanja < pk) {
                            for (int j = sumBitovaPrije; j < sumBitovaPoslije; j++) {
                                bitovi[j] = prvi[i][j];
                            }
                        } else {
                            for (int j = sumBitovaPrije; j < sumBitovaPoslije; j++) {
                                bitovi[j] = drugi[i][j];
                            }
                        }
                    }

                    sumBitovaPrije = sumBitovaPoslije;
                    sumBitovaPoslije += cutPeace;
                    kojiRoditelj++;

                }
            }else{

                if(r.nextDouble() < 0.5){
                    for(int j=0; j<brojBitova.get(i); j++){
                        bitovi[j] = prvi[i][j];
                    }
                }else{
                    for(int j=0; j<brojBitova.get(i); j++){
                        bitovi[j] = drugi[i][j];
                    }
                }

            }

            dijete[i] = bitovi;

        }

        return dijete;

    }

    public double[][] krizajUniformno(List<double[][]> jedinke){

        double[][] dijete = new double[brojVarijabli][];
        double[][] prva = jedinke.get(0);
        double[][] druga = jedinke.get(1);

        for(int i=0; i<brojVarijabli; i++){
            double[] pom = new double[brojBitova.get(i)];
            for(int j=0; j<brojBitova.get(i); j++){
                pom[j] = ((int)prva[i][j] ^ (int)druga[i][j]) & (int)radijVektor[i][j] | ((int)prva[i][j] & (int)druga[i][j]);
            }
            dijete[i] = pom;
        }

        return dijete;

    }

    public double[][] mutiraj(double[][] jedinka){

        Random r = new Random();
        int N = 0;
        for(int ni: this.brojBitova){
            N += ni;
        }
        double pMutacije = 1 - Math.pow(1 - this.p, N);

        for(int i=0; i<jedinka.length; i++){
            for(int j=0; j<jedinka[i].length; j++){
                if(r.nextDouble() < pMutacije){
                    jedinka[i][j] = 1 ^ (int)jedinka[i][j];
                }
            }
        }

        return jedinka;

    }

    public double[][] najboljaJedinka(Map<double[][], Double> fitnessPopulacija){

        double[][] najbolja = new double[this.brojVarijabli][];
        Double najFitness = 999999999999999999999999999999999999999999999999999999999999999999999.0;
        for(Map.Entry<double[][], Double> entry: fitnessPopulacija.entrySet()){
            if(entry.getValue() < najFitness){
                najFitness = entry.getValue();
                najbolja = entry.getKey();
            }
        }

        return najbolja;

    }

    public double fitness(double[][] jedinka){

        List<Double> dekJedinka = new ArrayList<>();

        int i = 0;
        for(double[] d: jedinka){
            double vrijednost = binUdek(d, i);
            dekJedinka.add(vrijednost);
            i++;
        }

        double[] ah = new double[dekJedinka.size()];
        int j = 0;
        for(Double d: dekJedinka){
            ah[j] = d;
            j++;
        }

        return this.f.execute(ah);

    }

    public double binUdek(double[] d, int index){

        double sum = 0.0;
        for(int i=d.length-1; i>=0; i--){
            sum += Math.pow(2, i) * d[i];
        }

        return (sum / (Math.pow(2, d.length) - 1)) * (this.gg.get(index) - this.dg.get(index)) + this.dg.get(index);

    }

    public List<IMatrix> rk(IMatrix A, IMatrix B, IMatrix xk){

        List<IMatrix> xks = new ArrayList<>();
        xks.add(xk);

        int tmin = this.tmin, tmax = this.tmax;
        while(tmin < tmax){

            IMatrix m1 = getM1(A, B, xks.get(tmin).copy());
            IMatrix m2 = getM2(A, B, m1.copy(), xks.get(tmin).copy());
            IMatrix m3 = getM3(A, B, m2.copy(), xks.get(tmin).copy());
            IMatrix m4 = getM4(A, B, m3.copy(), xks.get(tmin).copy());

            IMatrix xkplus1 = getXkplus1(xks.get(tmin).copy(), m1, m2, m3, m4);

            xks.add(xkplus1);

            if(tmax % this.itNumber == 0){
                System.out.println("k = " + tmin);
                xks.get(tmin).printMatrix();
            }

            tmin += 1;

        }

        return xks;

    }

    public IMatrix getM1(IMatrix A, IMatrix B, IMatrix xk){
        return A.mul(xk);
    }

    public IMatrix getM2(IMatrix A, IMatrix B, IMatrix m1, IMatrix xk){
        IMatrix temp = new Matrix();
        temp = m1.scalarMul(this.T / 2.0);
        temp = temp.nSum(xk);

        return A.mul(temp);
    }

    public IMatrix getM3(IMatrix A, IMatrix B, IMatrix m2, IMatrix xk){
        IMatrix temp = new Matrix();
        temp = m2.scalarMul(this.T / 2.0);
        temp = temp.nSum(xk);

        return A.mul(temp);
    }

    public IMatrix getM4(IMatrix A, IMatrix B, IMatrix m3, IMatrix xk){
        IMatrix temp = new Matrix();
        temp = m3.scalarMul(this.T);
        temp = temp.nSum(xk);

        return A.mul(temp);
    }

    public IMatrix getXkplus1(IMatrix xk, IMatrix m1, IMatrix m2, IMatrix m3, IMatrix m4){
        IMatrix temp = new Matrix();
        temp = m1.nSum(m2.scalarMul(2)).nSum(m3.scalarMul(2)).nSum(m4).scalarMul(this.T / 6.0).nSum(xk);

        return temp;
    }

    public List<IMatrix> trapez(IMatrix A, IMatrix B, IMatrix xk){

        List<IMatrix> xks = new ArrayList<>();
        xks.add(xk);

        int tmin = this.tmin, tmax = this.tmax;
        while(tmin < tmax){

            IMatrix a = UATinv(A).copy().mul(UAT(A)).copy().mul(xks.get(tmin).copy()).copy();
            IMatrix b = UATinv(A).copy().mul(B.copy().scalarMul(this.T)).copy();
            IMatrix xkplus1 = a.nSum(b);

            xks.add(xkplus1);

            if(tmax % this.itNumber == 0){
                System.out.print("k = " + tmin);
                xks.get(tmin).printMatrix();
            }

            tmin += 1;

        }

        return xks;

    }

    public IMatrix UATinv(IMatrix A){

        IMatrix U = new Matrix(A.getRowsCount(), true);
        IMatrix AT = A.copy().scalarMul(this.T / 2.0);

        return  U.nSub(AT).inv();

    }

    public IMatrix UAT(IMatrix A){

        IMatrix U = new Matrix(A.getRowsCount(), true);
        IMatrix AT = A.copy().scalarMul(this.T / 2.0);

        return U.nSum(AT);

    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.e = e;
    }

    public IFunctions getF() {
        return f;
    }

    public void setF(IFunctions f) {
        this.f = f;
    }

    public double getAlfa() {
        return alfa;
    }

    public void setAlfa(double alfa) {
        this.alfa = alfa;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getGama() {
        return gama;
    }

    public void setGama(double gama) {
        this.gama = gama;
    }

    public double getPomak() {
        return pomak;
    }

    public void setPomak(double pomak) {
        this.pomak = pomak;
    }

    public void setL(double[] l) {
        this.l = l;
    }

    public void setR(double[] r) {
        this.r = r;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public double[] getL() {
        return l;
    }

    public double[] getR() {
        return r;
    }

    public int getTreshold() {
        return treshold;
    }

    public void setTreshold(int treshold) {
        this.treshold = treshold;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getPk() {
        return pk;
    }

    public void setPk(double pk) {
        this.pk = pk;
    }

    public List<Double> getDg() {
        return dg;
    }

    public void setDg(List<Double> dg) {
        this.dg = dg;
    }

    public List<Double> getGg() {
        return gg;
    }

    public void setGg(List<Double> gg) {
        this.gg = gg;
    }

    public int getPopsize() {
        return popsize;
    }

    public void setPopsize(int popsize) {
        this.popsize = popsize;
    }

    public int getM() {
        return M;
    }

    public void setM(int m) {
        M = m;
    }

    public int getInacica() {
        return inacica;
    }

    public void setInacica(int inacica) {
        this.inacica = inacica;
    }

    public boolean isBinarniPrikaz() {
        return binarniPrikaz;
    }

    public void setBinarniPrikaz(boolean binarniPrikaz) {
        this.binarniPrikaz = binarniPrikaz;
    }

    public List<Integer> getBrojBitova() {
        return brojBitova;
    }

    public void setBrojBitova(List<Integer> brojBitova) {
        this.brojBitova = brojBitova;
    }

    public int getBrojVarijabli() {
        return brojVarijabli;
    }

    public void setBrojVarijabli(int brojVarijabli) {
        this.brojVarijabli = brojVarijabli;
    }

    public int[][] getRadijVektor() {
        return radijVektor;
    }

    public void setRadijVektor(int[][] radijVektor) {
        this.radijVektor = radijVektor;
    }

    public double getPradij() {
        return pradij;
    }

    public void setPradij(double pradij) {
        this.pradij = pradij;
    }

    public int getItNumber() {
        return itNumber;
    }

    public void setItNumber(int itNumber) {
        this.itNumber = itNumber;
    }

    public int getTmin() {
        return tmin;
    }

    public void setTmin(int tmin) {
        this.tmin = tmin;
    }

    public int getTmax() {
        return tmax;
    }

    public void setTmax(int tmax) {
        this.tmax = tmax;
    }

    public double getT() {
        return this.T;
    }

    public void setT(double T) {
        this.T = T;
    }
}
