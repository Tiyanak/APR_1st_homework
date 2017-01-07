package hr.fer.apr.golden_lab.functions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor Farszky on 19.10.2016..
 */
public abstract class IFunctions {

    private Map<String, Integer> counter;
    private Map<String, Integer> hessCounter;
    private Map<String, Integer> gradCoutner;

    public IFunctions() {
        this.counter = new HashMap<>();
        this.hessCounter = new HashMap<>();
        this.gradCoutner = new HashMap<>();
    }

    public Map<String, Integer> getCounter() {
        return counter;
    }

    public void setCounter(Map<String, Integer> counter) {
        this.counter = counter;
    }

    public Map<String, Integer> getHessCounter() {
        return hessCounter;
    }

    public void setHessCounter(Map<String, Integer> hessCounter) {
        this.hessCounter = hessCounter;
    }

    public Map<String, Integer> getGradCoutner() {
        return gradCoutner;
    }

    public void setGradCoutner(Map<String, Integer> gradCoutner) {
        this.gradCoutner = gradCoutner;
    }

    public void putNewF(String name){
        if(!this.counter.containsKey(name)){
            this.counter.put(name, 0);
            this.hessCounter.put(name, 0);
            this.gradCoutner.put(name, 0);
        }
    }

    public void resetCounter(){
        this.counter.clear();
        this.hessCounter.clear();
        this.gradCoutner.clear();
    }

    public void printCounter(String name){
        if(this.counter.containsKey(name)){
            System.out.println("Funckija cilja --> Name: " + name + " | Counter: " + this.counter.get(name));
            System.out.println("Hesseova --> Name: " + name + " | Counter: " + this.hessCounter.get(name));
            System.out.println("Gradijent --> Name: " + name + " | Counter: " + this.gradCoutner.get(name));
        }
    }

    public void printCounter(){
        for(Map.Entry<String, Integer> m: counter.entrySet()){
            System.out.println("Funkcija cilja --> Name: " + m.getKey() + " | Counter: " + m.getValue());
        }
        for(Map.Entry<String, Integer> m: hessCounter.entrySet()){
            System.out.println("Hesseova --> Name: " + m.getKey() + " | Counter: " + m.getValue());
        }
        for(Map.Entry<String, Integer> m: gradCoutner.entrySet()){
            System.out.println("Gradijent --> Name: " + m.getKey() + " | Counter: " + m.getValue());
        }
    }

    public void count(String name){
        if(this.counter.containsKey(name)){
            this.counter.replace(name, this.counter.get(name)+1);
        }else{
            this.counter.put(name, 0);
        }
    }

    public void counthess(String name){
        if(this.hessCounter.containsKey(name)){
            this.hessCounter.replace(name, this.hessCounter.get(name)+1);
        }else{
            this.hessCounter.put(name, 0);
        }
    }

    public void countgrad(String name){
        if(this.gradCoutner.containsKey(name)){
            this.gradCoutner.replace(name, this.gradCoutner.get(name)+1);
        }else{
            this.gradCoutner.put(name, 0);
        }
    }

    public abstract double execute(double x);

    public abstract double execute(double[] x);

    public abstract double execute(double lamda, double[] v, double[] x);

    public abstract double[] d(double[] x);

    public abstract double[][] H(double[] x);


}
