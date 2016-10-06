package hr.fer.apr.linear_algebra;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor Farszky on 5.10.2016..
 */
public class ConsoleParser {

    private Map<String, IMatrix> matrixMap;
    private Map<String, Double> doubleVar;
    private String dekstopPath;
    private boolean isRightMatrix;
    private IMatrix rightSolvedMatrix;
    private double rightSolvedDouble;
    private IMatrix leftSolvedMatrix;
    private double leftSolvedDouble;

    public ConsoleParser() {
        this.matrixMap = new HashMap<>();
        this.doubleVar = new HashMap<>();
        this.dekstopPath = "C:\\Users\\Igor Farszky\\Desktop\\";
        this.isRightMatrix = true;
    }

    public Map<String, IMatrix> getMatrixMap() {
        return matrixMap;
    }

    public void setMatrixMap(Map<String, IMatrix> matrixMap) {
        this.matrixMap = matrixMap;
    }

    public Map<String, Double> getDoubleVar() {
        return doubleVar;
    }

    public void setDoubleVar(Map<String, Double> doubleVar) {
        this.doubleVar = doubleVar;
    }

    public String getDekstopPath() {
        return dekstopPath;
    }

    public void setDekstopPath(String dekstopPath) {
        this.dekstopPath = dekstopPath;
    }

    public void parser(String line) throws FileNotFoundException {
        if(line.contains(".txt")){
            readMatrix(line);
        }else if(line.matches("^[a-zA-Z]+$")){
            createEmptyMatrix(line);
        }else if(line.contains("=")){
            signEquals(line);
        }else if(line.matches("^double [a-zA-Z]+$")){
            createNewDouble(line);
        }else{
            System.out.println("Unknown operation!");
        }
    }

    private void createNewDouble(String line){
        String name = line.substring(line.indexOf(" ")+1);

        doubleVar.put(name, 0.0);
    }

    private void signEquals(String line){
        if(line.contains(" = ")){
            associate(line);
        }else if(line.contains(" += ")){
            sumAssociate(line);
        }else if(line.contains(" -= ")){
            subAssociate(line);
        }else if(line.contains(" == ")){
            isEquals(line);
        }else{
            System.out.println("Unknown operation!");
        }
    }

    private void associate(String line){
        String left = line.split(" = ")[0];
        String right = line.split(" = ")[1];

        String name = "";
        if(left.contains("double")){
            String var = left.split(" ")[1];
            if(!doubleVar.containsKey(var)){

            }
        }

        for(String s: right.split(" ")){
            String variable = s;
            if(variable.contains("(")){
                variable = variable.substring(1);
            }else if(variable.contains(")")){
                variable = variable.substring(0, variable.lastIndexOf(")"));
            }else{
                // Do nothing
            }
            if(matrixMap.containsKey(variable)){

            }
        }
    }

    private void sumAssociate(String line){

    }

    private void subAssociate(String line){

    }

    private void isEquals(String line){

    }

    private IMatrix solveRightMatrix(String right){

    }

    private double solveRightDouble(String right){

    }

    private void solveLeftMatrix(IMatrix matrix){

    }

    private void solveLeftDouble(double value){

    }

    private void createEmptyMatrix(String line){
        this.matrixMap.put(line, new Matrix());
    }

    private void readMatrix(String line) throws FileNotFoundException {
        String name = line.substring(0, line.indexOf('('));
        String file = line.substring(line.indexOf("\"")+1, line.lastIndexOf("\""));

        this.matrixMap.put(name, MatrixFile.readMatrix(dekstopPath + file));
    }

    private void exit(){
        System.out.println("Goodbye");
        System.exit(1);
    }
}
