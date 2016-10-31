package hr.fer.apr.linear_algebra;

/**
 * Sucelje metoda za operacije sa matricama te lu, lup dekompozicije i substitucije unaprijed i unatrag
 */

public interface IMatrix {
	
	int getRowsCount();
	void setRowsCount(int rows);
	int getColsCount();
	void setColsCount(int cols);
	double[][] getElements();
	void setElements(double[][] elements);
	double getElement(int row, int col);
	void setElement(int row, int col, double element);
	IMatrix copy();
	void printMatrix();
	void setMatrix(IMatrix matrix);
	
	IMatrix transpose();
	
	IMatrix sum(IMatrix matrix);
	IMatrix nSum(IMatrix matrix);
	
	IMatrix sub(IMatrix matrix);
	IMatrix nSub(IMatrix matrix);
	
	IMatrix mul(IMatrix matrix);

	IMatrix scalarMul(double value);
	IMatrix nScalarMul(double value);

	void changeRows(int[] P);

	boolean LU();
	IMatrix SF(IMatrix L, int[] P, boolean indexing);
	IMatrix SB(IMatrix U);
	int[] LUP();
	IMatrix inv();

}
