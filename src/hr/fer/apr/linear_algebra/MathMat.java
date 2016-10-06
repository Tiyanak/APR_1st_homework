package hr.fer.apr.linear_algebra;

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
	public IMatrix LU(){
		for(int i=0; i<getRowsCount()-1; i++){
			for(int j=i+1; j<getColsCount(); j++){
				if(getElement(i, i) == 0){
					System.out.format("At LU, element (%d, %d) in matrix U is 0!\n", i, i);
					System.exit(1);
				}
				setElement(j, i, getElement(j, i) / getElement(i, i));
				for(int k=i+1; k<getColsCount(); k++){
					double result = getElement(j, k) - (getElement(j, i) * getElement(i, k));
					setElement(j, k, result);
				}
			}
		}
		
		return this;
	}
	
	@Override
	public IMatrix SF(IMatrix L){
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
		for(int i=this.getRowsCount()-1; i>-1; i--){
			if(U.getElement(i, i) == 0){
				System.out.format("At SB, element (%d, %d) in matrix U is 0!\n", i, i);
				System.exit(1);
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
	public IMatrix LUP(){
		int P[] = new int[this.getRowsCount()];
		
		for(int i=0; i<this.getRowsCount(); i++){
			P[i] = i;
		}
		
		for(int i=0; i<this.getRowsCount()-1; i++){
			int pivot = i;
		
			for(int j=i+1; j<this.getRowsCount(); j++){
				if(Math.abs(this.getElement(P[j], i)) > Math.abs(this.getElement(P[pivot], i))){
					pivot = j;
				}
			}
			
			int temp = P[i];
			P[i] = P[pivot];
			P[pivot] = temp;
			
			for(int j=i+1; j<this.getRowsCount(); j++){
				if(this.getElement(P[i], i) == 0){
					System.out.format("At LUP, element (%d, %d) in matrix U is 0!\n", P[i], i);
					System.exit(1);
				}
				this.setElement(P[j], i, (this.getElement(P[j], i) / this.getElement(P[i], i)));
			
				for(int k=i+1; k<this.getRowsCount(); k++){
					double result = this.getElement(P[j], k) - (this.getElement(P[j], i) * this.getElement(P[i], k));
					this.setElement(P[j], k, result);
				}
			
			}
			
		}
		
		return this;
	}
	
	@Override
	public void printMatrix(){
		System.out.println(this.toString());
	}
	

}
