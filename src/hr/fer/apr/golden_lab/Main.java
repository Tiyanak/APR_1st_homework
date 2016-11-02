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
        final String PATH = "C:\\Users\\Igor Farszky\\Desktop\\";

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
                } else if (fun.equals("f6")) {
                    a.setF(new F6());
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
                    double[] ad = ReadXsFromFile.readOneSpot(PATH + aS);
                    double[] bd = ReadXsFromFile.readOneSpot(PATH + bS);

                    double[] res = a.golden_cut(ad, bd);

                    for (double d : res) {
                        System.out.println(d + " ");
                    }
                } else if (params.split(", ").length == 1) {

                    double[] tocka = new double[]{Double.parseDouble(params)};
                    a.unimodalni(tocka);
                    double[] rez = a.golden_cut(a.getL(), a.getR());

                    for (double d : rez) {
                        System.out.print(d + " ");
                    }

                }
            } else if (line.contains("unimodalni")) {
                String aS = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                double[] value = new double[]{Double.parseDouble(aS)};

                a.unimodalni(value);

                System.out.println("(" + a.getL()[0] + ", " + a.getR()[0] + ")");

            } else if (line.contains("simplex")) {

                String aS = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                double[] value = ReadXsFromFile.readOneSpot(PATH + aS);

                value = a.simplex(value);

                for (double d : value) {
                    System.out.println(d + ", ");
                }

            } else if (line.contains("hooke")) {

                String aS = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                double[] value = ReadXsFromFile.readOneSpot(PATH + aS);

                value = a.hookeJeves(value);

                for (double d : value) {
                    System.out.println(d + ", ");
                }

            } else if (line.contains("koord")) {

                String aS = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                double[] value = ReadXsFromFile.readOneSpot(PATH + aS);

                value = a.koordinatni(value);

                for (double d : value) {
                    System.out.println(d + ", ");
                }

            } else if (line.contains("print(")) {
                String params = line.substring(line.indexOf("(") + 1, line.indexOf(")"));

                if (params.isEmpty()) {
                    a.getF().printCounter();
                } else {
                    a.getF().printCounter(params);
                }

            } else if(line.contains("resetC")){
                a.getF().resetCounter();
            }

        }

    }

}
