package hr.fer.apr.linear_algebra;

public interface IMatrix {
	
	public int getRowsCount();
	public void setRowsCount(int rows);
	public int getColsCount();
	public void setColsCount(int cols);
	public double[][] getElements();
	public void setElements(double[][] elements);
	public double getElement(int row, int col);	
	public void setElement(int row, int col, double element);
	public IMatrix copy();
	public void printMatrix();
	public void setMatrix(IMatrix matrix);
	
	public IMatrix transpose();
	
	public IMatrix sum(IMatrix matrix);
	public IMatrix nSum(IMatrix matrix);
	
	public IMatrix sub(IMatrix matrix);
	public IMatrix nSub(IMatrix matrix);
	
	public IMatrix mul(IMatrix matrix);

	public IMatrix scalarMul(double value);
	public IMatrix nScalarMul(double value);

	public void changeRows(int[] P);

	public boolean LU();
	public IMatrix SF(IMatrix L, int[] P, boolean indexing);
	public IMatrix SB(IMatrix U);
	public int[] LUP();

}
