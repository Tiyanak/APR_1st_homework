package hr.fer.apr.linear_algebra;

public class Matrix  extends MathMat{
	
	private int rows;
	private int cols;
	private double[][] elements;
	
	public Matrix() {
		super();
	}
	
	

	public Matrix(int rows, int cols) {
		super();
		this.rows = rows;
		this.cols = cols;
	}



	public Matrix(int rows, int cols, double[][] elements) {
		super();
		this.rows = rows;
		this.cols = cols;
		this.elements = elements;
	}

	public int getRowsCount() {
		return rows;
	}

	public void setRowsCount(int rows) {
		this.rows = rows;
	}

	public int getColsCount() {
		return cols;
	}

	public void setColsCount(int cols) {
		this.cols = cols;
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
	
	public IMatrix copy(){
		double[][] copyElements = new double[this.rows][this.cols];
		for(int i=0; i<this.rows; i++){
			for(int j=0; j<this.cols; j++){
				copyElements[i][j] = this.elements[i][j];
			}
		}
		
		return new Matrix(this.rows, this.cols, copyElements);
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
		
		sb.replace(sb.lastIndexOf("\n"), sb.lastIndexOf("\n")+1, "");
		sb.append("]");
		
		return sb.toString();
	}
	
	
}
