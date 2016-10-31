package hr.fer.apr.golden_lab;

import hr.fer.apr.golden_lab.functions.*;

import java.util.Scanner;

/**
 * Created by Igor Farszky on 20.10.2016..
 */
public class Main {

    public static void main(String[] args){

        Algorithms a = new Algorithms();
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to my Golden shell");
        System.out.println("You can set functions and constants with: 'e=value, h=value, alfa=value ...'");

        while(true){

            String line = sc.nextLine();

            if(line.contains("e = ")){
                a.setE(Double.parseDouble(line.substring(4)));
            }else if(line.contains("h = ")){
                a.setH(Double.parseDouble(line.substring(4)));
            }else if(line.contains("alfa = ")){
                a.setAlfa(Double.parseDouble(line.substring(7)));
            }else if(line.contains("beta = ")){
                a.setBeta(Double.parseDouble(line.substring(7)));
            }else if(line.contains("gama = ")){
                a.setGama(Double.parseDouble(line.substring(7)));
            }else if(line.contains("f = ")){
                String fun = line.substring(4);

                if(fun.equals("f1")){
                    a.setF(new F1());
                }else if(fun.equals("f2")){
                    a.setF(new F2());
                }else if(fun.equals("f3")){
                    a.setF(new F3());
                }else if(fun.equals("f4")){
                    a.setF(new F4());
                }else if(fun.equals("f6")){
                    a.setF(new F6());
                }else{
                    // nothing
                }
            }else if(line.contains("pomak = ")){
                a.setPomak(Double.parseDouble(line.substring(8)));
            }else if(line.contains("sigma = ")){
                a.setSigma(Double.parseDouble(line.substring(8)));
            }else if(line.contains("golden")){

            }else if(line.contains("unimodalni")){

            }else if(line.contains("simplex")){

            }else if(line.contains("hooke")){

            }else{
                // Do nothing
            }

        }

    }

}
