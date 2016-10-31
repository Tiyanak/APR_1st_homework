package hr.fer.apr.golden_lab;

import com.sun.org.apache.xml.internal.resolver.readers.XCatalogReader;
import hr.fer.apr.golden_lab.functions.F1;
import hr.fer.apr.golden_lab.functions.IFunctions;

import java.util.concurrent.Callable;

/**
 * Created by Igor Farszky on 19.10.2016..
 */
public class Algorithms {

    private final double k;
    private double[] l;
    private double[] r;
    private double h;
    private double e;
    private double alfa;
    private double beta;
    private double gama;
    private double pomak;
    private double sigma;
    private IFunctions f;

    public Algorithms() {
        this.l = new double[100];
        this.r = new double[100];
        this.h = 1.0;
        this.e = 0.000001;
        this.alfa = 1;
        this.beta = 0.5;
        this.gama = 2;
        this.pomak = 1.0;
        this.sigma = 0.5;
        this.f = new F1();
        this.k = 0.5 * (Math.sqrt(5) - 1);
    }

    public double[] golden_cut(double[] a, double[] b) {

        double[] c = new double[a.length];
        double[] d = new double[a.length];

        for (int i = 0; i < a.length; i++) {
            c[i] = b[i] - k * (b[i] - a[i]);
            d[i] = a[i] - k * (b[i] - a[i]);
        }

        double fc = f.execute(c);
        double fd = f.execute(d);

        int i = 0;
        while (distance(a, b) > this.e) {
            if (fc < fd) {
                b = d;
                d = c;
                for (int j = 0; j < a.length; j++) {
                    c[j] = b[j] - k * (b[j] - a[j]);
                }
                fd = fc;
                fc = f.execute(c);
            } else {
                a = c;
                c = d;
                for (int j = 0; j < a.length; j++) {
                    d[j] = a[j] + k * (b[j] - a[j]);
                }
                fc = fd;
                fd = f.execute(d);
            }
        }

        double[] ret = new double[a.length];
        for (int j = 0; j < a.length; j++) {
            ret[j] = (a[j] + b[j]) / 2;
        }

        return ret;

    }

    public void unimodalni(double[] tocka) {

        for (int i = 0; i < l.length; i++) {
            this.l[i] = 0.0;
            this.r[i] = 0.0;
        }

        for (int i = 0; i < tocka.length; i++) {
            this.l[i] = tocka[i] - this.h;
            this.r[i] = tocka[i] + this.h;
        }

        double[] m = tocka;
        double fl, fm, fr;
        int step = 1;

        fm = f.execute(tocka);
        fl = f.execute(this.l);
        fr = f.execute(this.r);

        if (fm < fr && fm < fl) {

            return;

        } else if (fm > fr) {

            do {
                this.l = m;
                m = this.r;
                fm = fr;
                for (int i = 0; i < tocka.length; i++) {
                    this.r[i] = tocka[i] + this.h * (step *= 2);
                }
                fr = f.execute(this.r);
            } while (fm > fr);

        } else {

            do {
                this.r = m;
                m = this.l;
                fm = fl;
                for (int i = 0; i < tocka.length; i++) {
                    this.l[i] = tocka[i] - this.h * (step *= 2);
                }
                fr = f.execute(this.l);
            } while (fm > fl);
        }

    }

    public double[] koordinatni(double[] X0){
        double[] x = X0.clone();

        double[] xs = x.clone();
        do{
            xs = x.clone();
            for (int i=0; i<xs.length; i++){
                unimodalni(xs);
                this.l[1] = 0.0;
                this.r[1] = 0.0;
                double[] lam = golden_cut(this.l, this.r);
                x[i] = x[i] + lam[0] * e;
            }
        }while (distance(x, xs) > e);

        return x;
    }

    public double[] simplex(double[] X0) {

        double[][] X = new double[X0.length + 1][X0.length];

        X[0] = X0.clone();
        for (int i = 1; i < X0.length + 1; i++) {
            X[i] = X0.clone();
            X[i][i - 1] += pomak;
        }

        double[] Xc = new double[X0.length];
        int h = 0;
        int l = 0;

        do {

            h = maxIndex(X);
            l = minIndex(X);

            Xc = centroid(X, h);

            double[] Xr = new double[X0.length];
            Xr = refleksija(X, Xc, h);

            if (f.execute(Xr) < f.execute(X[l])) {
                double[] Xe = new double[X0.length];
                Xe = ekspancija(Xc, Xr);

                if (f.execute(Xe) < f.execute(X[h])) {
                    X[h] = Xe;
                } else {
                    X[h] = Xr;
                }

            } else {

                int j = 0;
                // j = 0..n, j != h
                if ( isBigger(Xr, X, h)) {
                    if (f.execute(Xr) < f.execute(X[h])) {
                        X[h] = Xr;
                    }

                    double[] Xk = new double[X0.length];
                    Xk = kontrakcija(Xc, X, h);

                    if (f.execute(Xk) < f.execute(X[h])) {
                        X[h] = Xk;
                    } else {
                        X = pomakPremaXl(X, l);
                    }

                } else {
                    X[h] = Xr;
                }

            }
        } while (uvjet(X, Xc, h));

        return Xc;

    }

    public double[] hookeJeves(double[] X0) {

        double[] Xp = X0.clone(), Xb = X0.clone();
        double Dx = 0.0;

        do {
            double[] Xn = istrazi(Xp, Dx);

            if (f.execute(Xn) < f.execute(Xb)) {
                for (int i = 0; i < Xp.length; i++) {
                    Xp[i] = 2 * Xn[i] * Xb[i];
                }
                Xb = Xn.clone();
            } else {
                Dx = Dx / 2.0;
                Xp = Xb.clone();
            }
        } while (Dx > e); // dok nije zadovoljen uvjet

        return Xb;

    }

    public double[] istrazi(double[] Xp, double Dx) {
        double[] x = Xp.clone();
        int n = x.length; // ne znam neki n

        for (int i = 1; i < n; i++) {
            double P = f.execute(x);
            x[i] = x[i] + Dx;
            double N = f.execute(x);

            if (N > P) {
                x[i] = x[i] - 2 * Dx;
                N = f.execute(x);
                if (N > P) {
                    x[i] = x[i] + Dx;
                }
            }
        }

        return x;
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
            c[i] /= x.length;
        }

        return c;
    }

    private double[] refleksija(double[][] x, double[] xc, int h){
        double[] xr = new double[xc.length];
        for(int i=0; i<xc.length; i++){
            xr[i] = (1+alfa) * xc[i] - alfa * x[h][i];
        }

        return xr;
    }

    private double[] ekspancija(double[] xc, double[] xr){
        double[] xe = new double[xc.length];

        for (int i=0; i<xe.length; i++){
            xe[i] = (1 - gama) * xc[i] + gama * xr[i];
        }

        return xe;
    }

    private boolean isBigger(double[] xr, double[][] x, int h){

        for(int i=0; i<x.length; i++){
            if(i != h) {
                if (f.execute(xr) < f.execute(x[i])) {
                    return false;
                }
            }
        }

        return true;
    }

    private double[] kontrakcija(double[] xc, double[][] x, int h){
        double[] xk = new double[xc.length];

        for(int i=0; i<xc.length; i++){
            xk[i] = (1-beta) * xc[i] + beta * x[h][i];
        }

        return xk;
    }

    private double[][] pomakPremaXl(double[][] x, int l){
        double[] xl = x[l].clone();

        for (int i=0; i<x.length; i++){
            for (int j=0; j<x[0].length; j++){
                x[i][j] = (x[i][j] + xl[j]) * sigma;
            }
        }

        return x;
    }

    private boolean uvjet(double[][] x, double[] xc, int h){
        for (int i=0; i<x.length; i++){
           if(distance(x[h], xc) > e){
                return true;
           }
        }

        return false;
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
}
