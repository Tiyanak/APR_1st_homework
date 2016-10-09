package hr.fer.apr.linear_algebra;

import java.io.FileNotFoundException;
import java.io.InterruptedIOException;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Pack200;

/**
 * Created by Igor Farszky on 5.10.2016..
 */
public class ConsoleParser {

    private Map<String, IMatrix> matrixMap;
    private Map<String, Double> doubleMap;
    private String dekstopPath;
    private boolean isRightMatrix;
    private IMatrix rightSolvedMatrix;
    private double rightSolvedDouble;
    private IMatrix leftSolvedMatrix;
    private double leftSolvedDouble;
    private static final String fakeVariable = "myOwnPersonalUniqueTempVariable";
    private int fakerCounter;

    public ConsoleParser() {

        this.matrixMap = new HashMap<>();
        this.doubleMap = new HashMap<>();
        this.dekstopPath = "C:\\Users\\Igor Farszky\\Desktop\\";
        this.isRightMatrix = true;
        this.fakerCounter = 0;

    }

    public Map<String, IMatrix> getMatrixMap() {
        return matrixMap;
    }

    public void setMatrixMap(Map<String, IMatrix> matrixMap) {
        this.matrixMap = matrixMap;
    }

    public Map<String, Double> getDoubleMap() {
        return doubleMap;
    }

    public void setDoubleMap(Map<String, Double> doubleMap) {
        this.doubleMap = doubleMap;
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
        }else if(line.matches("^double [a-zA-Z]+$")) {
            createNewDouble(line);
        }else if(line.equalsIgnoreCase("exit")) {
            exit();
        }else if(line.contains(".lu")) {
            LU(line);
        }else if(line.contains(".sf")) {
            SF(line);
        }else if(line.contains(".sb")) {
            SB(line);
        }else if(line.contains(".lup")) {
            LUP(line);
        }else if(line.contains(".solveX")){
            solveX(line);
        }else{
            System.out.println("Unknown operation!");
        }
    }

    private void LU(String line){
        IMatrix m = matrixMap.get(line.substring(0, line.indexOf(".")));

        m.LU();

        matrixMap.replace(line.substring(0, line.indexOf(".")), m);

        m.printMatrix();
    }

    private void SF(String line){
        IMatrix m = matrixMap.get(line.substring(0, line.indexOf(".")));
        IMatrix l = matrixMap.get(line.substring(line.indexOf("(")+1, line.indexOf(")")));

        m.SF(l);

        matrixMap.replace(line.substring(0, line.indexOf(".")), m);

        m.printMatrix();
    }

    private void SB(String line){
        IMatrix m = matrixMap.get(line.substring(0, line.indexOf(".")));
        IMatrix l = matrixMap.get(line.substring(line.indexOf("(")+1, line.indexOf(")")));

        m.SB(l);

        matrixMap.replace(line.substring(0, line.indexOf(".")), m);

        m.printMatrix();
    }

    private void LUP(String line){
        IMatrix m = matrixMap.get(line.substring(0, line.indexOf("[.]")));

        m.LUP();

        matrixMap.replace(line.substring(0, line.indexOf("[.]")), m);

        m.printMatrix();
    }

    private void solveX(String line){
        IMatrix x = matrixMap.get(line.substring(0, line.indexOf(".")));
        IMatrix A = matrixMap.get(line.substring(line.indexOf("(")+1, line.indexOf(",")));
        IMatrix b = matrixMap.get(line.substring(line.indexOf(" ")+1, line.indexOf(")")));

        A.LU().printMatrix();
        b.SF(A).printMatrix();
        b.SB(A).printMatrix();

        x = b.copy();

        matrixMap.replace(line.substring(0, line.indexOf(".")), x);

        x.printMatrix();
    }

    private void createNewDouble(String line){
        String name = line.split(" ")[1].trim();

        if(!doubleMap.containsKey(name) && !matrixMap.containsKey(name)) {
            doubleMap.put(name, 0.0);
            System.out.println(0.0);
        }else{
            System.out.println("That variable already exist!");
        }

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

        right = right.replace(";", "");

        right = solveRightMatrix(right);
        left = solveLeftMatrix(left);

        if(matrixMap.containsKey(left) && matrixMap.containsKey(right)){

            matrixMap.replace(left, matrixMap.get(right));

            matrixMap.get(left).printMatrix();

        }else if(doubleMap.containsKey(left) && (doubleMap.containsKey(right) || right.matches("[0-9]+"))){

            if(right.matches("[0-9]+")){
                double x = Double.parseDouble(right);
                doubleMap.replace(left, x);
                System.out.println(left + " = " + doubleMap.get(left));

            }else {

                doubleMap.replace(left, doubleMap.get(right));
                System.out.println(left + " = " + doubleMap.get(left));
            }

        }else if(left.contains("[") && matrixMap.containsKey(left.substring(0, left.indexOf("["))) && (doubleMap.containsKey(right) || right.matches("[0-9]+"))){

            IMatrix l = matrixMap.get(left.substring(0, left.indexOf("[")));
            int i = Integer.parseInt(left.substring(left.indexOf("[")+1, left.indexOf("]")));
            int j = Integer.parseInt(left.substring(left.lastIndexOf("[")+1, left.lastIndexOf("]")));

            if(right.matches("[0-9]+")){
                double x = Double.parseDouble(right);
                l.setElement(i, j, x);
            }else{
                l.setElement(i, j, doubleMap.get(right));
            }

            matrixMap.replace(left.substring(0, left.indexOf("[")), l);

            System.out.println(left + " = " + l.getElement(i, j));

        }else{

            System.out.println("Left and right are not the same math types!");

        }
    }

    private void sumAssociate(String line){
        String left = line.split(" [+]= ")[0];
        String right = line.split(" [+]= ")[1];

        right = right.replace(";", "");

        right = solveRightMatrix(right);
        left = solveLeftMatrix(left);

        if(matrixMap.containsKey(left) && matrixMap.containsKey(right)){

            IMatrix l = matrixMap.get(left);
            IMatrix r = matrixMap.get(right);

            l.sum(r);
            matrixMap.replace(left, l);

            l.printMatrix();

        }else if(doubleMap.containsKey(left) && (doubleMap.containsKey(right) || right.matches("[0-9]+"))){

            double l = doubleMap.get(left);

            if(right.matches("[0-9]+")){

                double r = Double.parseDouble(right);
                doubleMap.replace(left, l + r);

            }else {

                doubleMap.replace(left, doubleMap.get(right) + l);

            }

            System.out.println(left + " = " + doubleMap.get(left));

        }else if(left.contains("[") && matrixMap.containsKey(left.substring(0, left.indexOf("["))) && (doubleMap.containsKey(right) || right.matches("[0-9]+"))){

            IMatrix l = matrixMap.get(left.substring(0, left.indexOf("[")));
            int i = Integer.parseInt(left.substring(left.indexOf("[")+1, left.indexOf("]")));
            int j = Integer.parseInt(left.substring(left.lastIndexOf("[")+1, left.lastIndexOf("]")));
            double lEl = l.getElement(i, j);

            if(right.matches("[0-9]+")){
                double x = Double.parseDouble(right);
                l.setElement(i, j, x + lEl);
            }else{
                l.setElement(i, j, doubleMap.get(right) + lEl);
            }

            matrixMap.replace(left.substring(0, left.indexOf("[")), l);

            System.out.println(left + " = " + l.getElement(i, j));

        }else {

            System.out.println("Left and right are not the same math types!");

        }
    }

    private void subAssociate(String line){
        String left = line.split(" [-]= ")[0];
        String right = line.split(" [-]= ")[1];

        right = right.replace(";", "");

        right = solveRightMatrix(right);
        left = solveLeftMatrix(left);

        if(matrixMap.containsKey(left) && matrixMap.containsKey(right)){

            IMatrix l = matrixMap.get(left);
            IMatrix r = matrixMap.get(right);

            l.sub(r);
            matrixMap.replace(left, l);

            l.printMatrix();

        }else if(doubleMap.containsKey(left) && (doubleMap.containsKey(right) || right.matches("[0-9]+"))){

            double l = doubleMap.get(left);

            if(right.matches("[0-9]+")){

                double r = Double.parseDouble(right);
                doubleMap.replace(left, l - r);

            }else {

                doubleMap.replace(left, l - doubleMap.get(right));

            }

            System.out.println(left + " = " + doubleMap.get(left));

        }else if(left.contains("[") && matrixMap.containsKey(left.substring(0, left.indexOf("["))) && (doubleMap.containsKey(right) || right.matches("[0-9]+"))){

            IMatrix l = matrixMap.get(left.substring(0, left.indexOf("[")));
            int i = Integer.parseInt(left.substring(left.indexOf("[")+1, left.indexOf("]")));
            int j = Integer.parseInt(left.substring(left.lastIndexOf("[")+1, left.lastIndexOf("]")));
            double lEl = l.getElement(i, j);

            if(right.matches("[0-9]+")){
                double x = Double.parseDouble(right);
                l.setElement(i, j, lEl - x);
            }else{
                l.setElement(i, j, lEl - doubleMap.get(right));
            }

            matrixMap.replace(left.substring(0, left.indexOf("[")), l);

            System.out.println(left + " = " + l.getElement(i, j));

        }else {

            System.out.println("Left and right are not the same math types!");

        }
    }

    private void isEquals(String line){
        String left = line.split(" == ")[0];
        String right = line.split(" == ")[1];

        right = right.replace(";", "");

        right = solveRightMatrix(right);
        left = solveLeftMatrix(left);

        if(matrixMap.containsKey(left) && matrixMap.containsKey(right)){
            if(matrixMap.get(left).equals(matrixMap.get(right))){
                System.out.println("True");
            }else{
                System.out.println("False");
            }

        }else if(doubleMap.containsKey(left) && (doubleMap.containsKey(right) || right.matches("[0-9]+"))){
            double x = 0.0;
            if(right.matches("[0-9]+")){
                x = Double.parseDouble(right);
            }else{
                x = doubleMap.get(right);
            }

            if(doubleMap.get(left) == x){
                System.out.println("True");
            }else{
                System.out.println("False");
            }

        }else if(left.contains("[") && matrixMap.containsKey(left.substring(0, left.indexOf("["))) && (doubleMap.containsKey(right) || right.matches("[0-9]+"))){
            int i = Integer.parseInt(left.substring(left.indexOf("[")+1, left.indexOf("]")));
            int j = Integer.parseInt(left.substring(left.lastIndexOf("[")+1, left.lastIndexOf("]")));

            double x = 0.0;
            if(right.matches("[0-9]+")){
                x = Double.parseDouble(right);
            }else{
                x = doubleMap.get(right);
            }

            if(matrixMap.get(left.substring(0, left.indexOf("["))).getElement(i, j) == x){
                System.out.println("True");
            }else{
                System.out.println("False");
            }
        }else{
            System.out.println("Left and right are not the same math types!");
        }
    }

    private String solveRightMatrix(String right){
        if(right.split(" ").length == 1){
            if(right.contains("[")){
                int i = Integer.parseInt(right.substring(right.indexOf("[")+1, right.indexOf("]")));
                int j = Integer.parseInt(right.substring(right.lastIndexOf("[")+1, right.lastIndexOf("]")));

                double t = matrixMap.get(right.substring(0, right.indexOf("["))).getElement(i, j);

                this.fakerCounter = fakerCounter + 1;
                doubleMap.put(fakeVariable + fakerCounter, t);

                return right.replace(right, fakeVariable + fakerCounter);
            }else {
                String r = right;
                return r;
            }
        }else if(right.contains("(")){
            String brackets = right.substring(right.indexOf("(")+1, right.indexOf(")"));
            String solvedBrackets = expression(brackets);

            String r = solveRightMatrix(right.replace("(" + brackets + ")", solvedBrackets));
            return r;
        }else{
            String r = solveRightMatrix(expression(right));
            return r;
        }
    }

    private String solveLeftMatrix(String left){
        String[] l = left.split(" ");

        if(l.length == 1){
            if(l[0].contains("[")){
                return  l[0];
            }else if(matrixMap.containsKey(l[0])){
                return l[0];
            }else if(doubleMap.containsKey(l[0])){
                return l[0];
            }else{
                matrixMap.put(l[0], new Matrix());
                return l[0];
            }
        }else if(l[0].equalsIgnoreCase("double")){
            createNewDouble(left);
            return l[1].trim();
        }else{
            System.out.println("Wrong left side of equesion!");
            return left;
        }

    }

    private String expression(String exp){
        if(exp.split(" ").length == 1){
            return exp;
        }else if(exp.contains("~")){
            String variable = exp.substring(exp.indexOf("~")-1, exp.indexOf("~"));

            IMatrix m = this.matrixMap.get(variable);
            this.matrixMap.replace(variable, m, m.transpose());

            String ret = exp.replaceFirst(variable+"~", variable);

            return expression(ret);

        }else if(exp.contains("*")){

            String before = exp.split(" [*] ")[0];
            String after = exp.split(" [*] ")[1];

            before = before.split(" ")[before.split(" ").length-1];
            after = after.split(" ")[0];

            this.fakerCounter = this.fakerCounter + 1;

            calculate(before, after, "*", this.fakerCounter);

            String ret = expression(exp.replace(before + " * " + after, fakeVariable + this.fakerCounter));

            return ret;

        }else if(exp.contains("/")){

            String before = exp.split(" [/] ")[0];
            String after = exp.split(" [/] ")[1];

            before = before.split(" ")[before.split(" ").length-1];
            after = after.split(" ")[0];

            this.fakerCounter = this.fakerCounter + 1;

            calculate(before, after, "/", this.fakerCounter);

            String ret = expression(exp.replace(before + " - " + after, fakeVariable + this.fakerCounter));

            return ret;

        } else if(exp.contains("+")){

            String before = exp.split(" [+] ")[0];
            String after = exp.split(" [+] ")[1];

            before = before.split(" ")[before.split(" ").length-1];
            after = after.split(" ")[0];

            this.fakerCounter = this.fakerCounter + 1;

            calculate(before, after, "+", this.fakerCounter);

            String ret = expression(exp.replace(before + " + " + after, fakeVariable + this.fakerCounter));

            return ret;

        } else {

            String before = exp.split(" [-] ")[0];
            String after = exp.split(" [-] ")[1];

            before = before.split(" ")[before.split(" ").length-1];
            after = after.split(" ")[0];

            this.fakerCounter = this.fakerCounter + 1;

            calculate(before, after, "-", this.fakerCounter);

            String ret = expression(exp.replace(before + " - " + after, fakeVariable + this.fakerCounter));

            return ret;

        }
    }

    private void calculate(String first, String second, String operation, int fakerCounter){

        IMatrix firstMatrix = null;
        IMatrix secondMatrix = null;
        Double firstDouble = null;
        Double secondDouble = null;

        if(matrixMap.containsKey(first)){
            firstMatrix = matrixMap.get(first);
        }else if(first.contains("[")){

            int i = Integer.parseInt(first.substring(first.indexOf("[")+1, first.indexOf("]")));
            int j = Integer.parseInt(first.substring(first.lastIndexOf("[")+1, first.lastIndexOf("]")));

            firstDouble = matrixMap.get(first.substring(0, first.indexOf("["))).getElement(i, j);
        }else if(doubleMap.containsKey(first)){
            firstDouble = doubleMap.get(first);
        }else{
            firstDouble  = Double.parseDouble(first);
        }

        if(matrixMap.containsKey(second)){
            secondMatrix = matrixMap.get(second);
        }else if(second.contains("[")){

            int i = Integer.parseInt(second.substring(second.indexOf("[")+1, second.indexOf("]")));
            int j = Integer.parseInt(second.substring(second.lastIndexOf("[")+1, second.lastIndexOf("]")));

            secondDouble = matrixMap.get(second.substring(0, second.indexOf("["))).getElement(i, j);
        }else if(doubleMap.containsKey(second)){
            secondDouble = doubleMap.get(second);
        }else{
            secondDouble  = Double.parseDouble(second);
        }

        if(firstMatrix != null && secondMatrix != null){
            if(operation.equals("*")){
                IMatrix m = firstMatrix.mul(secondMatrix);
                matrixMap.put(fakeVariable + fakerCounter, m);
            }else if(operation.equals("/")){
                System.out.println("I dont know how to calculate matrix dividing");
            }else if(operation.equals("+")){
                IMatrix m = firstMatrix.nSum(secondMatrix);
                matrixMap.put(fakeVariable + fakerCounter, m);
            }else{
                IMatrix m = firstMatrix.nSub(secondMatrix);
                matrixMap.put(fakeVariable + fakerCounter, m);
            }
        }else if(firstMatrix != null && secondDouble != null){
            if(operation.equals("*")){
                IMatrix m = firstMatrix.nScalarMul(secondDouble);
                matrixMap.put(fakeVariable + fakerCounter, m);
            }else if(operation.equals("/")){
                IMatrix m = firstMatrix.nScalarMul(1.0 / secondDouble);
                matrixMap.put(fakeVariable + fakerCounter, m);
            }else if(operation.equals("+")){
                System.out.println("I dont know how to sum matrix and scalar");
            }else{
                System.out.println("I dont know how to sub matrix and scalar");
            }
        }else if(firstDouble != null && secondMatrix != null){
            if(operation.equals("*")){
                IMatrix m = secondMatrix.nScalarMul(firstDouble);
                matrixMap.put(fakeVariable + fakerCounter, m);
            }else if(operation.equals("/")){
                IMatrix m = secondMatrix.nScalarMul(1.0 / firstDouble);
                matrixMap.put(fakeVariable + fakerCounter, m);
            }else if(operation.equals("+")){
                System.out.println("I dont know how to sum matrix and scalar");
            }else{
                System.out.println("I dont know how to sub matrix and scalar");
            }
        }else{
            if(operation.equals("*")){
                double d = firstDouble * secondDouble;
                doubleMap.put(fakeVariable + fakerCounter, d);
            }else if(operation.equals("/")){
                double d = firstDouble / secondDouble;
                doubleMap.put(fakeVariable + fakerCounter, d);
            }else if(operation.equals("+")){
                double d = firstDouble + secondDouble;
                doubleMap.put(fakeVariable + fakerCounter, d);
            }else{
                double d = firstDouble - secondDouble;
                doubleMap.put(fakeVariable + fakerCounter, d);
            }
        }


    }

    private void createEmptyMatrix(String line){
        if(!matrixMap.containsKey(line) && !doubleMap.containsKey(line)) {
            this.matrixMap.put(line, new Matrix());
            System.out.println(line + " = [ ]");
        }else{
            if(matrixMap.containsKey(line)){
                matrixMap.get(line).printMatrix();
            }else if(doubleMap.containsKey(line)){
                System.out.println(doubleMap.get(line));
            }else{
                System.out.println();
            }
        }
    }

    private void readMatrix(String line) throws FileNotFoundException {
        if(!line.matches("^[a-zA-Z]+[(][\"][a-zA-Z]+[.]txt[\"][)]$")){
            System.out.println("Unknown command for read matrix!");
            return;
        }
        String name = line.substring(0, line.indexOf('('));
        String file = line.substring(line.indexOf("\"")+1, line.lastIndexOf("\""));

        IMatrix m = MatrixFile.readMatrix(dekstopPath + file);

        this.matrixMap.put(name, m);

        m.printMatrix();

    }

    private void exit(){
        System.out.println("Goodbye");
        System.exit(1);
    }
}
