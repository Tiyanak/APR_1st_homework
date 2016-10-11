package hr.fer.apr.linear_algebra;

import javax.swing.*;

public abstract class MathMat implements IMatrix{

	public MathMat() {
	}
	
	public abstract int getRowsCount();
	public abstract void setRowsCount(int rows);
	public abstract int getColsCount();
	public abstract void setColsCount(int cols);
	public abstract double[][] getElements();
	public abstract void setElements(double[][] elements);
	public abstract double getElement(int row, int col);	
	public abstract void setElement(int row, int col, double element);
	public abstract IMatrix copy();
	public abstract void setMatrix(IMatrix matrix);

	@Override
	public IMatrix transpose() {
		double[][] nTransEls = new double[getColsCount()][getRowsCount()];
		for(int i=0; i<getRowsCount(); i++){
			for(int j=0; j<getColsCount(); j++){
				nTransEls[j][i] = getElement(i, j);
			}
		}
		
		return new Matrix(getColsCount(), getRowsCount(), nTransEls);
	}

	@Override
	public IMatrix sum(IMatrix matrix) {
		if(getRowsCount() != matrix.getRowsCount() || getColsCount() != matrix.getColsCount()){
			// Do nothing
		}else{
			for(int i=0; i<this.getRowsCount(); i++){
				for(int j=0; j<this.getColsCount(); j++){
					double sum = this.getElement(i, j) + matrix.getElement(i, j);
					this.setElement(i, j, sum);
				}
			}
		}
		
		return this;
		
	}

	@Override
	public IMatrix nSum(IMatrix matrix) {
		return this.copy().sum(matrix);
	}

	@Override
	public IMatrix sub(IMatrix matrix) {
		if(getRowsCount() != matrix.getRowsCount() || getColsCount() != matrix.getColsCount()){
			// Do nothing cuz its an error
			return this;
		}else{
			for(int i=0; i<this.getRowsCount(); i++){
				for(int j=0; j<this.getColsCount(); j++){
					double sum = this.getElement(i, j) - matrix.getElement(i, j);
					this.setElement(i, j, sum);
				}
			}

			return this;
		}
		
	}

	@Override
	public IMatrix nSub(IMatrix matrix) {
		return this.copy().sub(matrix);
	}

	@Override
	public IMatrix mul(IMatrix matrix) {
		if (this.getColsCount() != matrix.getRowsCount()) {
			// Do nothing cuz its an error
			return this;
		} else {
			double[][] mulMat = new double[this.getRowsCount()][matrix.getColsCount()];

			for (int i = 0; i < this.getRowsCount(); i++) {
				for (int j = 0; j < matrix.getColsCount(); j++) {
					double sum = 0;
					for (int l = 0; l < this.getColsCount(); l++) {
						sum += this.getElement(i, l) * matrix.getElement(l, j);
					}
					mulMat[i][j] = sum;
				}
			}
			Matrix mulMatrix = new Matrix(this.getRowsCount(), matrix.getColsCount(), mulMat);
			return mulMatrix;
		}
	}

	@Override
	public IMatrix scalarMul(double value) {
		for(int i=0; i<this.getRowsCount(); i++){
			for(int j=0; j<this.getColsCount(); j++){
				this.setElement(i, j, value*this.getElement(i, j));
			}
		}
		return this;
		
	}

	@Override
	public IMatrix nScalarMul(double value) {
		return this.copy().scalarMul(value);
	}
	
	@Override
	public boolean LU(){
		IMatrix backUp = this.copy();
		for(int i=0; i<this.getRowsCount()-1; i++){
			for(int j=i+1; j<this.getColsCount(); j++){
				if(this.getElement(i, i) == 0.0){
					System.out.format("At LU, element (%d, %d) in matrix U is 0!\n", i, i);
					this.setMatrix(backUp);
					return false;
				}
				this.setElement(j, i, this.getElement(j, i) / this.getElement(i, i));
				for(int k=i+1; k<this.getColsCount(); k++){
					double result = this.getElement(j, k) - (this.getElement(j, i) * this.getElement(i, k));
					this.setElement(j, k, result);
				}
			}
		}
		
		return true;
	}
	
	@Override
	public IMatrix SF(IMatrix L, int[] P, boolean indexing){
		if(indexing) {
			this.changeRows(P);
		}

		for(int i=0; i<getRowsCount()-1; i++){
			for(int j=i+1; j<getRowsCount(); j++){
				double result = this.getElement(j, 0) - (L.getElement(j, i) * this.getElement(i, 0));
				this.setElement(j, 0, result);
			}
		}
		
		return this;
	}
	
	@Override
	public IMatrix SB(IMatrix U){
		IMatrix backup = this.copy();
		for(int i=this.getRowsCount()-1; i>-1; i--){
			if(U.getElement(i, i) == 0){
				System.out.format("At SB, element (%d, %d) in matrix U is 0!\n", i, i);
				this.setMatrix(backup);
				return this;
			}
			this.setElement(i, 0, (this.getElement(i, 0) / U.getElement(i, i)));
			for(int j=0; j<=i-1; j++){
				double result = this.getElement(j, 0) - (U.getElement(j, i) * this.getElement(i, 0));
				this.setElement(j, 0, result);
			}
		}
		
		return this;
	}
	
	@Override
	public int[] LUP(){
		int P[] = new int[this.getRowsCount()];
		IMatrix backup = this.copy();

		// Vector indexes
		for(int i=0; i<this.getRowsCount(); i++){
			P[i] = i;
		}
		
		for(int i=0; i<this.getRowsCount()-1; i++){
			// Choose pivot
			int pivot = i;
			if(Math.abs(this.getElement(P[pivot], i)) < 0.000001){
				System.out.println("Element : (" + P[pivot] + ", " + i  + ") is 0!");
				this.setMatrix(backup);
				return P;
			}
			for(int j=i+1; j<this.getRowsCount(); j++){
				if(Math.abs(this.getElement(P[j], i)) > Math.abs(this.getElement(P[pivot], i))){
					pivot = j;
				}
				if(Math.abs(this.getElement(P[j], i)) < 0.000001){
					System.out.println("Element : " + this.getElement(P[j], i) + " is 0!");
					this.setMatrix(backup);
					return P;
				}
			}

			// Change row indexes
			int temp = P[i];
			P[i] = P[pivot];
			P[pivot] = temp;

			for(int j=i+1; j<this.getRowsCount(); j++){
				if(this.getElement(P[i], i) == 0){
					System.out.format("At LUP, element (%d, %d) in matrix U is 0!\n", P[i], i);
					this.setMatrix(backup);
					return P;
				}
				this.setElement(P[j], i, (this.getElement(P[j], i) / this.getElement(P[i], i)));
			
				for(int k=i+1; k<this.getRowsCount(); k++){
					double result = this.getElement(P[j], k) - (this.getElement(P[j], i) * this.getElement(P[i], k));
					this.setElement(P[j], k, result);
				}
			
			}
			
		}

		this.changeRows(P);
		return P;
	}

	public void changeRows(int[] P){
		double[][] change = new double[this.getRowsCount()][this.getColsCount()];
		for(int i=0; i<this.getRowsCount(); i++){
			for(int j=0; j<this.getColsCount(); j++){
				change[i][j] = this.getElement(P[i], j);
			}
		}
		this.setElements(change);
		System.out.println("Changed rows matrix b: \n");
		this.printMatrix();
	}
	
	@Override
	public void printMatrix(){
		System.out.println(this.toString());
	}
	

}
