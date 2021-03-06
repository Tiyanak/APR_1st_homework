package hr.fer.apr.linear_algebra;

/**
 * Model matrice
 * sadrzi broj redaka, broj stupaca i double polje elemenata matrice
 * nasljeduje apstraktan razred MathMat koji sadrzi operacije racunanja sa matricama
 */

public class Matrix  extends MathMat{
	
	private int rows;
	private int cols;
	private double[][] elements;

	/**
	 * Prazan konstruktor
	 */
	public Matrix() {
		super();
		this.rows = 0;
		this.cols = 0;
		this.elements = new double[0][0];
	}

	/**
	 * Konstruktor radi matricu popunjenu nulama zadane velicine
	 * @param rows
	 * @param cols
	 */
	public Matrix(int rows, int cols) {
		super();
		this.rows = rows;
		this.cols = cols;
		this.elements = new double[rows][cols];
		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){
				this.elements[i][j] = 0.0;
			}
		}
	}

	/**
	 * Konstruktor stvara novu matricu zadane velicine i sa zadanim elementima
	 * @param rows
	 * @param cols
	 * @param elements
	 */
	public Matrix(int rows, int cols, double[][] elements) {
		super();
		this.rows = rows;
		this.cols = cols;
		this.elements = elements;
	}

	public Matrix(int dimension, boolean U){
		super();
		this.rows = dimension;
		this.cols = dimension;
		this.elements = new double[dimension][dimension];

		for(int i=0; i<dimension; i++){
			this.elements[i][i] = 1.0;
			for(int j=0; j<dimension; j++){
				if(i != j){
					this.elements[i][j] = 0.0;
				}
			}
		}

	}

	public int getRowsCount() {
		return rows;
	}

	public void setRowsCount(int rows) {
		this.rows = rows;
		this.elements = new double[this.rows][this.cols];
	}

	public int getColsCount() {
		return cols;
	}

	public void setColsCount(int cols) {
		this.cols = cols;
		this.elements = new double[this.rows][this.cols];
	}

	public double[][] getElements() {
		return elements;
	}

	public void setElements(double[][] elements) {
		this.elements = elements;
	}
	
	public double getElement(int row, int col){
		if(row <= this.rows && col <= this.cols){
			return this.elements[row][col];				
		}else{
			return 0;
		}
	}
	
	public void setElement(int row, int col, double element){
		if(this.rows >= row && this.cols >= col){
			this.elements[row][col] = element;
		}
	}

	/**
	 * Stvara kopiju ove matrice
	 * @return Matrix
	 */
	public IMatrix copy(){
		double[][] copyElements = new double[this.rows][this.cols];
		for(int i=0; i<this.rows; i++){
			for(int j=0; j<this.cols; j++){
				copyElements[i][j] = this.elements[i][j];
			}
		}
		
		return new Matrix(this.rows, this.cols, copyElements);
	}

	public void setMatrix(IMatrix matrix){
		this.cols = matrix.getColsCount();
		this.rows = matrix.getRowsCount();
		for(int i=0; i<this.rows; i++){
			for(int j=0; j<this.cols; j++){
				this.elements[i][j] = matrix.getElement(i, j);
			}
		}
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Matrix)){
			return false;
		}else{
			Matrix m = (Matrix) o;
			
			if(this.rows != m.getRowsCount() || this.cols != m.getColsCount()){
				return false;
			}else{
				for(int i=0; i<this.rows; i++){
					for(int j=0; j<this.cols; j++){
						if(m.getElements()[i][j] != this.elements[i][j]){
							return false;
						}
					}
				}
				return true;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		for(int i=0; i<this.rows; i++){
			sb.append("\t");
			for(int j=0; j<this.cols; j++){
				sb.append(this.elements[i][j]);
				sb.append("\t");
			}
			sb.append("\n");
		}

		if(sb.length() == 1){
			return "[ ]";
		}else {
			sb.replace(sb.lastIndexOf("\n"), sb.lastIndexOf("\n") + 1, "");
			sb.append("]\n");

			return sb.toString();
		}
	}
	
	
}
