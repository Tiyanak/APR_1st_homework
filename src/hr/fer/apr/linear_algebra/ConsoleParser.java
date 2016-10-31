package hr.fer.apr.linear_algebra;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Pack200;

/**
 * Razred predstavlja konzolu
 * Parsira ulaz koji korisnik upisuje te poziva zadane metode prema parsiranom ulazu
 */

/**
 * Created by Igor Farszky on 5.10.2016..
 */
public class ConsoleParser {

    private Map<String, IMatrix> matrixMap; // Mapa varijabli matrica
    private Map<String, Double> doubleMap; // Mapa varijabli tipa double
    private Map<String, int[]> indexingMap; // Mapa indexa poretka matrice
    private String dekstopPath; // Path do desktopa gdje se nalaze datoteka sa matricama
    private static final String fakeVariable = "myOwnPersonalUniqueTempVariable"; // Lazno ime varijable za racunanje izraza
    private int fakerCounter; // Brojac lazne varijable (kako se nebi reusala ista u krive svrhe)

    public ConsoleParser() {

        this.matrixMap = new HashMap<>();
        this.doubleMap = new HashMap<>();
        this.indexingMap = new HashMap<>();
        this.dekstopPath = "C:\\Users\\Igor Farszky\\Desktop\\";
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

    /**
     * Glavna metoda za parsiranje koja se pozica svaki put kad korisnik nesto upise
     * Parsira stvaranje varijabli, exit, metode dekompozicije, pridruzivanje, pisanje u datoteku
     * @param line - String korinsikova naredba
     * @throws FileNotFoundException
     */
    public void parser(String line) throws FileNotFoundException {
        if(line.contains(".txt") && !line.contains(".write(")){
            readMatrix(line);
        }else if(line.matches("^[a-zA-Z]+$")){
            createEmptyMatrix(line);
        }else if(line.contains("=")){
            signEquals(line);
        }else if(line.matches("^double [a-zA-Z]+$")) {
            createNewDouble(line);
        }else if(line.equalsIgnoreCase("exit")) {
            exit();
        }else if(line.contains(".lup")) {
            LUP(line);
        }else if(line.contains(".lu")) {
            LU(line);
        }else if(line.contains(".sf")) {
            SF(line);
        }else if(line.contains(".sb")) {
            SB(line);
        }else if(line.contains(".solveXlup")){
            solveXlup(line);
        }else if(line.contains(".solveXlu")) {
            solveXlu(line);
        }else if(line.contains(".write(")){
            writeMatrix(line);
        }else{
            System.out.println("Unknown operation!");
        }
    }

    /**
     * Metoda pise matricu u datoteku
     * usage: A.write(ime)
     * @param line
     */
    private void writeMatrix(String line){

        IMatrix matrix = matrixMap.get(line.substring(0, line.indexOf(".")));
        String filename = line.substring(line.indexOf("(")+1, line.indexOf(")"));

        try {
            MatrixFile.writeMatrix(dekstopPath + filename, matrix);
            System.out.println("Matrix saved on Desktop under name: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Metoda za parsiranje LU dekompozicije
     * usage: A.lu()
     * @param line
     */
    private void LU(String line){
        String name = line.substring(0, line.indexOf("."));
        IMatrix m = matrixMap.get(name);

        if(indexingMap.containsKey(name)){
            indexingMap.remove(name);
        }

        m.LU();

        matrixMap.replace(line.substring(0, line.indexOf(".")), m);

        System.out.println("LU: \n");
        m.printMatrix();
    }

    /**
     * Metoda za parsiranje substitucije unaprijed
     * usage: b.sf(A)
     * @param line
     */
    private void SF(String line){
        String name = line.substring(0, line.indexOf("."));
        IMatrix m = matrixMap.get(name);
        if(!line.matches("[a-zA-Z]+[.][a-zA-Z]+[(][a-zA-Z]+[)]")){
            System.out.println("You didnt gave me an L matrix!");
            return;
        }
        IMatrix l = matrixMap.get(line.substring(line.indexOf("(")+1, line.indexOf(")")));

        if(indexingMap.containsKey(name)){
            m.SF(l, indexingMap.get(name), true);
        }else{
            m.SF(l, new int[1], false);
        }

        matrixMap.replace(line.substring(0, line.indexOf(".")), m);

        System.out.println("SF: \n");
        m.printMatrix();
    }

    /**
     * Metoda za parsiranje substitucije unazad
     * usage: b.sb(A)
     * @param line
     */
    private void SB(String line){
        IMatrix m = matrixMap.get(line.substring(0, line.indexOf(".")));
        if(!line.matches("[a-zA-Z]+[.][a-zA-Z]+[(][a-zA-Z]+[)]")){
            System.out.println("You didnt gave me an U matrix!");
            return;
        }
        IMatrix l = matrixMap.get(line.substring(line.indexOf("(")+1, line.indexOf(")")));

        m.SB(l);

        matrixMap.replace(line.substring(0, line.indexOf(".")), m);

        System.out.println("SB: \n");
        m.printMatrix();
    }

    /**
     * Metoda za parsiranje lup dekompozicije
     * usage: A.lup()
     * @param line
     */
    private void LUP(String line){
        String name = line.substring(0, line.indexOf("."));
        IMatrix m = matrixMap.get(name);

        int[] P = m.LUP();

        if(indexingMap.containsKey(name)){
            indexingMap.replace(name, P);
        }else{
            indexingMap.put(name, P);
        }

        matrixMap.replace(line.substring(0, line.indexOf(".")), m);

        System.out.println("LUP: \n");
        m.printMatrix();
    }

    /**
     * Metoda za parsiranje rjesavanja sustava metodom lu dekompozicije
     * usage: x.solveXlu(A, b)
     * @param line
     */
    private void solveXlu(String line){
        IMatrix x = matrixMap.get(line.substring(0, line.indexOf(".")));
        IMatrix A = matrixMap.get(line.substring(line.indexOf("(")+1, line.indexOf(","))).copy();
        IMatrix b = matrixMap.get(line.substring(line.indexOf(" ")+1, line.indexOf(")")));

        boolean success = A.LU();
        System.out.println("LU finished successfully?: " + success);

        if(success) {
            x = b.copy();
            System.out.println("A: \n");
            A.printMatrix();
            System.out.println("y (SF): \n");
            x.SF(A, new int[1], false).printMatrix();
            System.out.println("x (SB): \n");
            x.SB(A).printMatrix();

            matrixMap.replace(line.substring(0, line.indexOf(".")), x);

            System.out.println("solved (LU): \n");
            x.printMatrix();
        }
    }

    /**
     * Metoda za parsiranje rjesavanja sustava metodom lup dekompozicije
     * usage: x.solveXlup(A, b)
     * @param line
     */
    private void solveXlup(String line){
        IMatrix x = matrixMap.get(line.substring(0, line.indexOf(".")));
        IMatrix A = matrixMap.get(line.substring(line.indexOf("(")+1, line.indexOf(","))).copy();
        IMatrix b = matrixMap.get(line.substring(line.indexOf(" ")+1, line.indexOf(")")));

        int[] P = A.LUP();
        System.out.println("A: \n");
        A.printMatrix();
        int k = 0;
        System.out.println("Indexing: \n");
        for(int i: P){
            System.out.print("P[" + k + "] : " + i + "; ");
            k++;
        }
        x = b.copy();
        System.out.println("");
        System.out.println("y (SF): \n");
        x.SF(A, P, true).printMatrix();
        System.out.println("x (SB): \n");
        x.SB(A).printMatrix();

        matrixMap.replace(line.substring(0, line.indexOf(".")), x);

        System.out.println("solved (lup): \n");
        x.printMatrix();
    }

    /**
     * Metoda za stvaranje nove varijable tipa double
     * usage: double x
     * @param line
     */
    private void createNewDouble(String line){
        String name = line.split(" ")[1].trim();

        if(!doubleMap.containsKey(name) && !matrixMap.containsKey(name)) {
            doubleMap.put(name, 0.0);
            System.out.println(0.0);
        }else{
            System.out.println("That variable already exist!");
        }

    }

    /**
     * Metoda za parsiranje znaka pridruzivanja
     * moze biti: =, +=, -=, ==
     * @param line
     */
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

    /**
     * Metoda za parsiranje metode pridruzivanja: x = 5 + y - (3 - z)
     * Moguce sa matricama, double virjednostima, elemntima matrice...
     * x[1][1] = 5 * (y + z[0][3])
     * X = U * L + B
     * @param line
     */
    private void associate(String line){
        String left = line.split(" = ")[0];
        String right = line.split(" = ")[1];

        right = right.replace(";", "");

        left = solveLeftMatrix(left);
        right = solveRightMatrix(right);

        // lijevo i desno su matrice
        if(matrixMap.containsKey(left) && matrixMap.containsKey(right)){

            matrixMap.replace(left, matrixMap.get(right).copy());

            matrixMap.get(left).printMatrix();

        // lijevo je double desno je double varijabla ili broj
        }else if(doubleMap.containsKey(left) && (doubleMap.containsKey(right) || right.matches("[0-9]+"))){

            if(right.matches("[0-9]+")){
                double x = Double.parseDouble(right);
                doubleMap.replace(left, x);
                System.out.println(left + " = " + doubleMap.get(left));

            }else {

                doubleMap.replace(left, doubleMap.get(right));
                System.out.println(left + " = " + doubleMap.get(left));
            }

        // lijevo je element matrice desno je double varijabla ili broj
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

        // inace linija nije tocna
        }else{

            System.out.println("Left and right are not the same math types!");

        }
    }

    /**
     * Metoda kao i kod pridruzivanja sve isto, samo jos zbraja lijevu stranu sa desnom i sprema u lijevu
     * X += B * C
     * @param line
     */
    private void sumAssociate(String line){
        String left = line.split(" [+]= ")[0];
        String right = line.split(" [+]= ")[1];

        right = right.replace(";", "");

        right = solveRightMatrix(right);
        left = solveLeftMatrix(left);

        if(matrixMap.containsKey(left) && matrixMap.containsKey(right)){

            IMatrix l = matrixMap.get(left);
            IMatrix r = matrixMap.get(right);

            matrixMap.replace(left, l.nSum(r));

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

    /**
     * Metoda za parsiranje oduzimanja od lijeve strane i spremanje u lijevu
     * X -= B - A
     * @param line
     */
    private void subAssociate(String line){
        String left = line.split(" [-]= ")[0];
        String right = line.split(" [-]= ")[1];

        right = right.replace(";", "");

        right = solveRightMatrix(right);
        left = solveLeftMatrix(left);

        if(matrixMap.containsKey(left) && matrixMap.containsKey(right)){

            IMatrix l = matrixMap.get(left);
            IMatrix r = matrixMap.get(right);

            matrixMap.replace(left, l.nSub(r));

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

    /**
     * Metoda parsira provjeru lijeve i desne strane jednakosti
     * x == 5
     * A == B
     * @param line
     */
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

    /**
     * Metoda parsira desnu stranu jednakosti, ako smo prije utvrdili da je rjesenje desne strane matrica
     * @param right
     * @return
     */
    private String solveRightMatrix(String right){
        // desno je samo jedna varijabla, zavrsi parsiranje
        if(right.split(" ").length == 1){
            if(right.contains("[")) {
                int i = Integer.parseInt(right.substring(right.indexOf("[") + 1, right.indexOf("]")));
                int j = Integer.parseInt(right.substring(right.lastIndexOf("[") + 1, right.lastIndexOf("]")));

                double t = matrixMap.get(right.substring(0, right.indexOf("["))).getElement(i, j);

                this.fakerCounter = fakerCounter + 1;
                doubleMap.put(fakeVariable + fakerCounter, t);

                return right.replace(right, fakeVariable + fakerCounter);

            }else if(right.contains("inv()")) {
                String name = right.substring(0, right.indexOf("."));
                IMatrix inv = matrixMap.get(name).inv();

                this.fakerCounter = fakerCounter + 1;
                String fake = "" + fakeVariable + fakerCounter;
                matrixMap.put(fake, inv);

                return fake;
            }else if(right.contains("~")){
                String r = solveRightMatrix(expression(right));
                return r;
            }else {
                String r = right;
                return r;
            }
        // desna strana ima zagrade, rijesi prvo sve izraze sa zagradama rekurzivno pozivajuci ezpression(izraz zagrade)
        // mjenjajuci izraz zagrade sa varijablom koja predstavlja njeno rjesenje
        }else if(right.contains("(")){
            String brackets = right.substring(right.indexOf("(")+1, right.indexOf(")"));
            String solvedBrackets = expression(brackets);

            String r = solveRightMatrix(right.replace("(" + brackets + ")", solvedBrackets));
            return r;
        // nema zagrada, nije jedna varijabla, znaci komplezan izraz je na desnoj strani
        // x + 5 - 3 * 1 / 10
        }else{
            String r = solveRightMatrix(expression(right));
            return r;
        }
    }

    /**
     * Metoda za rjesavanje lijeve strane izraza
     * ako je lijevo double x - stvara novu varijablu x tipa double
     * ako je samo ime varijable, ako je nepoznata stvara novu matricu pod tim imenom
     * ako je poznata vrijdenost racuna sa varijablom koju to ime predstavlja
     * @param left
     * @return
     */
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

    /**
     * Metoda za rekurzivno rjesavanje komplexnog racunskog izraza
     * 10 - 5 * 10 / 9 + 9
     * Prvo rjesava mnozenje i djeljenje zatim + i -
     * Radi tako da izdvaja varijablu sa lijeve strane znaka, i desne te znak, i pozove metodu calculate
     * te joj rpedaje te dvije varijable i znak operacije
     * @param exp
     * @return
     */
    private String expression(String exp){
        if(exp.split(" ").length == 1 && !exp.contains("~")){
            return exp;
        }else if(exp.contains("~")){
            String variable = exp.substring(exp.indexOf("~")-1, exp.indexOf("~"));

            IMatrix m = this.matrixMap.get(variable);

            fakerCounter = fakerCounter + 1;
            String fake = "" + fakeVariable + fakerCounter;

            this.matrixMap.put(fake, m.transpose());

            String ret = expression(exp.replaceFirst(variable + "~", fake));

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

            String ret = expression(exp.replace(before + " / " + after, fakeVariable + this.fakerCounter));

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

    /**
     * Metoda racuna rezultat zadane dvije varijable i operatora izmedu njih
     * rezultat sprema u pripadnu mapu kao fake varijablu
     * @param first - String prva varijabla
     * @param second - String druga varijabla
     * @param operation - String operator
     * @param fakerCounter - broj lazne varijable
     */
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

    /**
     * Metoda parsira izraz za stvarnaje prazne matrice
     * A
     * @param line
     */
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

    /**
     *  Metoda parsira izraz za citanje matrice iz datoteke\
     *  usage: A(A.txt)
     * @param line
     * @throws FileNotFoundException
     */
    private void readMatrix(String line) throws FileNotFoundException {
        if(!line.matches("^[a-zA-Z]+[(][a-zA-Z]+[0-9]*[.]txt[)]$")){
            System.out.println("Unknown command for read matrix!");
            return;
        }
        String name = line.substring(0, line.indexOf('('));
        String file = line.substring(line.indexOf("(")+1, line.lastIndexOf(")"));

        IMatrix m = MatrixFile.readMatrix(dekstopPath + file);

        this.matrixMap.put(name, m);

        m.printMatrix();

    }

    /**
     * Exit
     */
    private void exit(){
        System.out.println("Goodbye");
        System.exit(1);
    }
}
