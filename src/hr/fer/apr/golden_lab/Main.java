package hr.fer.apr.golden_lab;

import hr.fer.apr.golden_lab.functions.*;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Igor Farszky on 20.10.2016..
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        Algorithms a = new Algorithms();
        Scanner sc = new Scanner(System.in);
        final String PATH = "C:\\Users\\Igor\\Desktop\\";

        System.out.println("Welcome to my Golden shell");
        System.out.println("You can set functions and constants with: 'e=value, h=value, alfa=value ...'");

        while (true) {

            String line = sc.nextLine();

            if (line.contains("e = ")) {
                a.setE(Double.parseDouble(line.substring(4)));
            } else if (line.contains("h = ")) {
                a.setH(Double.parseDouble(line.substring(4)));
            } else if (line.contains("alfa = ")) {
                a.setAlfa(Double.parseDouble(line.substring(7)));
            } else if (line.contains("beta = ")) {
                a.setBeta(Double.parseDouble(line.substring(7)));
            } else if (line.contains("gama = ")) {
                a.setGama(Double.parseDouble(line.substring(7)));
            } else if (line.contains("f = ")) {
                String fun = line.substring(4);

                if (fun.equals("f1")) {
                    a.setF(new F1());
                } else if (fun.equals("f2")) {
                    a.setF(new F2());
                } else if (fun.equals("f3")) {
                    a.setF(new F3());
                } else if (fun.equals("f4")) {
                    a.setF(new F4());
                } else if (fun.equals("f0")) {
                    a.setF(new F0());
                } else {

                }
            } else if (line.contains("pomak = ")) {
                a.setPomak(Double.parseDouble(line.substring(8)));
            } else if (line.contains("sigma = ")) {
                a.setSigma(Double.parseDouble(line.substring(8)));
            } else if (line.contains("golden")) {

                String params = line.substring(line.indexOf("(") + 1, line.indexOf(")"));

                if (params.split(", ").length == 2) {

                    String aS = line.substring(line.indexOf("(") + 1, line.indexOf(","));
                    String bS = line.substring(line.indexOf(" ") + 1, line.indexOf(")"));
                    double[] ad = ReadXsFromFile.readOneSpot(PATH + aS + ".txt");
                    double[] bd = ReadXsFromFile.readOneSpot(PATH + bS + ".txt");

                    a.getF().resetCounter();
                    double[] res = a.golden_cut(ad, bd);

                    for (double d : res) {
                        System.out.print(d + " ");
                    }

                    System.out.println("F(x) = " + a.getF().execute(res));

                } else if (params.split(", ").length == 1) {

                    double[] tocka = new double[]{Double.parseDouble(params)};
                    a.getF().resetCounter();
                    a.unimodalni(tocka);
                    double[] rez = a.golden_cut(a.getL(), a.getR());

                    for (double d : rez) {
                        System.out.print(d + " ");
                    }

                    System.out.println("F(x) = " + a.getF().execute(rez));

                }
            } else if (line.contains("unimodalni")) {
                String aS = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                double[] value = new double[]{Double.parseDouble(aS)};

                a.getF().resetCounter();
                a.unimodalni(value);

                System.out.print("(" + a.getL()[0] + ", " + a.getR()[0] + ")");

            } else if (line.contains("simplex")) {

                String aS = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                double[] value = ReadXsFromFile.readOneSpot(PATH + aS + ".txt");

                a.getF().resetCounter();
                value = a.simplex(value);

                for (double d : value) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(value));

            } else if (line.contains("hooke")) {

                String aS = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                double[] value = ReadXsFromFile.readOneSpot(PATH + aS + ".txt");

                a.getF().resetCounter();
                value = a.hookeJeves(value);

                for (double d : value) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(value));

            } else if (line.contains("koord")) {

                String aS = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                double[] value = ReadXsFromFile.readOneSpot(PATH + aS + ".txt");

                a.getF().resetCounter();
                value = a.koordinatni(value);

                for (double d : value) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(value));

            } else if (line.contains("print(")) {
                String params = line.substring(line.indexOf("(") + 1, line.indexOf(")"));

                if (params.isEmpty()) {
                    a.getF().printCounter();
                } else {
                    a.getF().printCounter(params);
                }

            } else if(line.contains("reset")){
                a.getF().resetCounter();
            }else if(line.contains("gradopt(")){

                String var = line.substring(line.indexOf("(")+1, line.indexOf(")"));

                double[] value = ReadXsFromFile.readOneSpot(PATH + var + ".txt");

                a.getF().resetCounter();
                value = a.gradijent(value, true);

                for (double d : value) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(value));

            }else if(line.contains("grad(")){
                String var = line.substring(line.indexOf("(")+1, line.indexOf(")"));

                double[] value = ReadXsFromFile.readOneSpot(PATH + var + ".txt");

                a.getF().resetCounter();
                value = a.gradijent(value, false);

                for (double d : value) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(value));
            }else if(line.contains("box(")){
                String vars = line.substring(line.indexOf("(")+1, line.indexOf(")"));

                String poctocka = vars.split(", ")[0];
                String expogr = vars.split(", ")[1];
                String impogr = vars.split(", ")[2];

                double[] poctockavalue = ReadXsFromFile.readOneSpot(PATH + poctocka + ".txt");
                double[] expogrvalue = ReadXsFromFile.readOneSpot(PATH + expogr + ".txt");
                double[] impogrvalue = ReadXsFromFile.readOneSpot(PATH + impogr + ".txt");

                int[] imps = new int[impogrvalue.length];
                for(int i=0; i<imps.length; i++){
                    imps[i] = (int) impogrvalue[i];
                }

                double[] value = a.box(poctockavalue, expogrvalue[0], expogrvalue[1], imps);

                for (double d : value) {
                    System.out.print(d + ", ");
                }

                if(value[0] != 666.0){
                    System.out.println("F(x) = " + a.getF().execute(value));
                }

            }else if(line.contains("trans(")){
                String vars = line.substring(line.indexOf("(")+1, line.indexOf(")"));

                String poctocka = vars.split(", ")[0];
                String gs = vars.split(", ")[1];
                String hs = vars.split(", ")[2];

                double[] poctockavalue = ReadXsFromFile.readOneSpot(PATH + poctocka + ".txt");
                double[] gv = ReadXsFromFile.readOneSpot(PATH + gs + ".txt");
                double[] hv = ReadXsFromFile.readOneSpot(PATH + hs + ".txt");

                int[] gints = new int[gv.length];
                if(gv[0] != 666.0) {
                    for (int i = 0; i < gints.length; i++) {
                        gints[i] = (int) gv[i];
                    }
                }else{
                    gints = new int[0];
                }

                int[] hints = new int[hv.length];
                if(hv[0] != 666.0) {
                    for (int i = 0; i < hints.length; i++) {
                        hints[i] = (int) hv[i];
                    }
                }else{
                    hints = new int[0];
                }

                double[] value = a.transform(poctockavalue, gints, hints);

                for (double d : value) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(value));

            }else if(line.contains("t =")){
                a.setT(Double.parseDouble(line.substring(4)));
            }else if(line.contains("nropt(")){

                String var = line.substring(line.indexOf("(")+1, line.indexOf(")"));

                double[] value = ReadXsFromFile.readOneSpot(PATH + var + ".txt");

                a.getF().resetCounter();
                value = a.nr(value, true);

                for (double d : value) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(value));

            }else if(line.contains("nr(")) {
                String var = line.substring(line.indexOf("(") + 1, line.indexOf(")"));

                double[] value = ReadXsFromFile.readOneSpot(PATH + var + ".txt");

                a.getF().resetCounter();
                value = a.nr(value, false);

                for (double d : value) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(value));
            }else if(line.contains("prvi")){

                a.getF().resetCounter();
                a.setF(new F3());

                double[] grad3 = a.gradijent(new double[]{0.0, 0.0}, false);
                double[] grad3opt = a.gradijent(new double[]{0.0, 0.0}, true);

                for (double d : grad3) {
                    System.out.print(d + ", ");
                }

                System.out.println("Bez optimizacije: F(x) = " + a.getF().execute(grad3));

                for (double d : grad3opt) {
                    System.out.print(d + ", ");
                }

                System.out.println("Uz optimizaciju: F(x) = " + a.getF().execute(grad3opt));

            }else if(line.contains("drugi")){

                a.setF(new F1());
                a.getF().resetCounter();
                double[] grad1 = a.gradijent(new double[]{-1.9, 2.0}, true);

                for (double d : grad1) {
                    System.out.print(d + ", ");
                }

                System.out.print("F(x) = " + a.getF().execute(grad1) + " | Brojac: " );
                a.getF().printCounter();

                a.getF().resetCounter();
                double[] nr1 = a.nr(new double[]{-1.9, 2.0}, true);

                for (double d : nr1) {
                    System.out.print(d + ", ");
                }

                System.out.print("F(x) = " + a.getF().execute(nr1) + " | Brojac: " );
                a.getF().printCounter();

                a.setF(new F2());
                a.getF().resetCounter();
                double[] grad2 = a.gradijent(new double[]{0.1, 0.3}, true);

                for (double d : grad2) {
                    System.out.print(d + ", ");
                }

                System.out.print("F(x) = " + a.getF().execute(grad2) + " | Brojac: " );
                a.getF().printCounter();

                a.getF().resetCounter();
                double[] nr2 = a.nr(new double[]{0.1, 0.3}, true);

                for (double d : nr2) {
                    System.out.print(d + ", ");
                }

                System.out.print("F(x) = " + a.getF().execute(nr2) + " | Brojac: " );
                a.getF().printCounter();

            }else if(line.contains("treci")){

                a.setF(new F1());
                double[] box1 = a.box(new double[]{-1.9, 2.0}, -100, 100, new int[]{0, 1});

                for (double d : box1) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(box1));

                a.setF(new F2());
                double[] box2 = a.box(new double[]{0.1, 0.3}, -100, 100, new int[]{0, 1});

                for (double d : box2) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(box2));

            }else if(line.contains("cetvrti")){

                a.setF(new F1());
                double[] tr1 = a.transform(new double[]{-1.9, 2.0}, new int[]{0, 1}, new int[]{});

                for (double d : tr1) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(tr1));

                a.setF(new F2());
                double[] tr2 = a.transform(new double[]{0.1, 0.3}, new int[]{0, 1}, new int[]{});

                for (double d : tr2) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(tr2));

            }else if(line.contains("peti")){

                a.setF(new F4());
                double[] tr1 = a.transform(new double[]{0.0, 0.0}, new int[]{2, 3}, new int[]{0});

                for (double d : tr1) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(tr1));

                double[] tr2trazi = a.provjeriogr(new double[]{5.0, 5.0}, new int[]{2, 3});

                System.out.println("Nakon sredivanja tocke u ogranicenja: ");
                for (double d : tr2trazi) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(tr2trazi));

                double[] tr2 = a.transform(tr2trazi, new int[]{2, 3}, new int[]{0});

                for (double d : tr2) {
                    System.out.print(d + ", ");
                }

                System.out.println("F(x) = " + a.getF().execute(tr2));

            }

        }

    }

}
