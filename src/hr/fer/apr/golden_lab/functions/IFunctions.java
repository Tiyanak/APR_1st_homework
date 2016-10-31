package hr.fer.apr.golden_lab.functions;

/**
 * Created by Igor Farszky on 19.10.2016..
 */
public abstract class IFunctions {

    private int[] counter;

    public IFunctions() {
        this.counter = new int[1];
    }

    public int[] getCounter() {
        return counter;
    }

    public void setCounter(int[] counter) {
        this.counter = counter;
    }

    public void riseCounter(int index){
        if(this.counter.length < index) {
            this.counter[index] = this.counter[index] + 1;
        }
    }

    public abstract double execute(double x);

    public abstract double execute(double[] x);

}
