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
        this.l = new double[1];
        this.r = new double[1];
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
            for(int k=0; k<a.length; k++){
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
        for(int k=0; k<a.length; k++){
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

        System.out.println("ZLATNI REZ");
        System.out.println("a\t\tb\t\tc\t\td");

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
            for(int k=0; k<a.length; k++){
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

        String as = "", bs = "", cs = "", ds = "";
        for(int k=0; k<a.length; k++){
            as += a[k] + ",";
            bs += b[k] + ",";
            cs += c[k] + ",";
            ds += d[k] + ",";
        }
        System.out.println(as + "\t" + cs + "\t" + ds + "\t" + bs);

        return ret;

    }


    public void unimodalni(double lamda, double[] v, double[] tocka) {

        System.out.println("UNIMODALNI");
        System.out.println("l:\t\tr:");

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
                System.out.println(this.l[0] + "\t" + this.r[0]);

                this.l[0] = m;
                m = this.r[0];
                fm = fr;
                this.r[0] = lamda + this.h * (step *= 2);
                fr = f.execute(this.r[0], v, tocka);
            } while (fm > fr);

        } else {

            do {
                System.out.println(this.l[0] + "\t" + this.r[0]);

                this.r[0] = m;
                m = this.l[0];
                fm = fl;
                this.l[0] = lamda - this.h * (step *= 2);
                fl = f.execute(this.l[0], v, tocka);
            } while (fm > fl);
        }

        System.out.println(this.l[0] + "\t" + this.r[0]);

    }


    public double[] koordinatni(double[] X0){

        System.out.println("KOORDINATNI");

        double[] x = X0.clone();
        double[] v = new double[X0.length];

        for(int i=0; i<v.length; i++){
            v[i] = 0.0;
        }

        double[] xs = x.clone();
        do{
            String xString = "";
            for(double d: x){
                xString += d + ", ";
            }
            System.out.println("x: " + xString);
            xs = x.clone();
            for (int i=0; i<xs.length; i++){
                v[i] = 1.0;
                unimodalni(xs[i], v, xs);
                double[] lam = golden_cut(this.l.clone(), this.r.clone(), v, xs);
                x[i] = x[i] + lam[0] * v[i];
                v[i] = 0.0;
            }
        }while (distance(x, xs) > e);

        String xString = "";
        for(double d: x){
            xString += d + ", ";
        }
        System.out.println("x: " + xString);

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
                if ( isBigger(Xr, X, h)) {
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
            for(int q=0; q<Xc.length; q++){
                qc += Xc[q] + ",";
                qe += Xe[q] + ",";
                qr += Xr[q] + ",";
                qk += Xk[q] + ",";
            }
            System.out.println(qc + "\t" + qr + "\t" + qe + "\t" + qk);

        } while (uvjet(X, Xc, h));

        String qc = "", qr = "", qe = "", qk = "";
        for(int q=0; q<Xc.length; q++){
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
            for(int q=0; q<Xp.length; q++){
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
        for(int q=0; q<Xp.length; q++){
            qn += Xn[q] + ",";
            qb += Xb[q] + ",";
            qp += Xp[q] + ",";

        }
        System.out.println(qb + "\t" + qp + "\t" + qn);

        return Xb;

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
            c[i] /= x.length-1;
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

    public double[] getL() {
        return l;
    }

    public double[] getR() {
        return r;
    }
}
