package hr.fer.apr.golden_lab.functions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor Farszky on 19.10.2016..
 */
public abstract class IFunctions {

    private Map<String, Integer> counter;
    public IFunctions() {
        this.counter = new HashMap<>();
    }

    public Map<String, Integer> getCounter() {
        return counter;
    }

    public void setCounter(Map<String, Integer> counter) {
        this.counter = counter;
    }

    public void putNewF(String name){
        if(!this.counter.containsKey(name)){
            this.counter.put(name, 0);
        }
    }

    public void count(String name){
        if(this.counter.containsKey(name)){
            this.counter.replace(name, this.counter.get(name)+1);
        }else{
            this.counter.put(name, 0);
        }
    }

    public void resetCounter(){
        this.counter.clear();
    }

    public void printCounter(){
        for(Map.Entry<String, Integer> m: counter.entrySet()){
            System.out.println("Name: " + m.getKey() + " | Counter: " + m.getValue());
        }
    }

    public void printCounter(String name){
        if(this.counter.containsKey(name)){
            System.out.println("Name: " + name + " | Counter: " + this.counter.get(name));
        }
    }

    public abstract double execute(double x);

    public abstract double execute(double[] x);

}
