package hr.fer.apr.linear_algebra;

import javax.swing.*;

/**
 * Abstraktna klasa implementira operacije racunanja sa matricama
 * lu i lup dekompozicija te substitucija unaprije i unazad
 */

public abstract class MathMat implements IMatrix{

	public MathMat() {
	}

	/**
	 * Abstraktne metode implementirane u razredu Matrix
	 */
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

	/**
	 * Transponiranje matrice
	 * @return Matrix - novu transponiranu matricu
	 */
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

	/**
	 * Zbrajanje dviju matrica
	 * @param matrix - 2. pribrojnik
	 * @return Matrix - vraca ovu matricu zbrojenu sa parametrom, ne stvara kopiju!
	 */
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

	/**
	 * Zbrajanje dviju matrica
	 * @param matrix - 2. pribrojnik
	 * @return Matrix - vraca kopiju rezultata zbrajanja matrica
	 */
	@Override
	public IMatrix nSum(IMatrix matrix) {
		return this.copy().sum(matrix);
	}

	/**
	 * Oduzimanje dviju matrica
	 * @param matrix - umanjitelj
	 * @return Matrix - vraca ovu matricu umanjenu sa umanjiteljem, ne stvara kopiju!
	 */
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

	/**
	 * Oduzimanje dviju matrica
	 * @param matrix - umanjitelj
	 * @return Matrix - vraca kopiju rezultata oduzimanja matrica
	 */
	@Override
	public IMatrix nSub(IMatrix matrix) {
		return this.copy().sub(matrix);
	}

	/**
	 * Umnozak dviju matrica
	 * @param matrix - Imatrix - matrica sa kojom se mnozi trenutna
	 * @return Matrix - vraca NOVU pomnozenu matricu
	 */
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

	/**
	 * Mnozenje matrice skalarom
	 * @param value - double skalar
	 * @return IMatrix - trenutnu matricu pomnozenu skalarom
	 */
	@Override
	public IMatrix scalarMul(double value) {
		for(int i=0; i<this.getRowsCount(); i++){
			for(int j=0; j<this.getColsCount(); j++){
				this.setElement(i, j, value*this.getElement(i, j));
			}
		}
		return this;
		
	}

	/**
	 * Mnozenje matrice skalarom
	 * @param value - double skalar
	 * @return Matrix - vraca kopiju pomnozene matrice skalarom
	 */
	@Override
	public IMatrix nScalarMul(double value) {
		return this.copy().scalarMul(value);
	}

	/**
	 * Metoda za racunanje LU dekompozicije
	 * U matricu A nad kojom je pozbana metoda, zapisuje L i U matrice
	 * NE radi novu matricu!
	 * @return Matrix - L i U u trenutnoj matrici
	 */
	@Override
	public boolean LU(){
		IMatrix backUp = this.copy();
		for(int i=0; i<this.getRowsCount()-1; i++){
			for(int j=i+1; j<this.getColsCount(); j++){
				if(Math.abs(this.getElement(i, i)) < 0.000001){
					System.out.format("At LU, element (%d, %d) in matrix U is 0! : %f\n", i, i, this.getElement(i, i));
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

	/**
	 * Substitucija unaprijed
	 * Yi = Bi - (Ej=1 do i-1 Lij * yj)
	 * @param L - Matrix - matrica A koja sadrzi L i U
	 * @param P - polje integera koje sadrzi indexe poretka redaka
	 * @param indexing - boolean ako je potrebno mjenjati retke
	 * @return - matricu stupac - NE stvara novu matricu!
	 */
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

	/**
	 * Substitucija unazad
	 * Xi = 1/Uii * (Yi - Ej=i+1 do n Uij*Xj)
	 * @param U - matrica A koja sadrzi L i U
	 * @return - metricu stupac - NE stvara novu matricu!
	 */
	@Override
	public IMatrix SB(IMatrix U){
		U.printMatrix();
		IMatrix backup = this.copy();
		for(int i=this.getRowsCount()-1; i>-1; i--){
			if(Math.abs(U.getElement(i, i)) < 0.000001){
				System.out.format("At SB, element (%d, %d) in matrix U is 0! : %f\n", i, i, U.getElement(i, i));
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

	/**
	 * LUP dekompozicija
	 * Nad trenutnom matricom racuna lup dekompoziciju ne stvara novu matricu
	 * @return - polje integera u koje je zapisan poredak redaka
	 */
	@Override
	public int[] LUP(){
		int P[] = new int[this.getRowsCount()]; // stvori polje indexa redaka
		IMatrix backup = this.copy(); // kopija ove matrice za slucaj da nesto nije u redu

		// Vector indexes - inicijalizacija indexa
		for(int i=0; i<this.getRowsCount(); i++){
			P[i] = i;
		}
		
		for(int i=0; i<this.getRowsCount()-1; i++){
			// Choose pivot
			int pivot = i;

			// Trazi redak sa najvecim pivotom
			for(int j=i+1; j<this.getRowsCount(); j++){
				if(Math.abs(this.getElement(P[j], i)) > Math.abs(this.getElement(P[pivot], i))){
					pivot = j;
				}
			}

			// Ako je pivot blizu 0, prekini jer nema smisla racunanja
			// u tom slucaju prekini racunanje postavi backup u ovu matricu te vrati polje indexa
			if(Math.abs(this.getElement(P[pivot], i)) < 0.000001){
				System.out.println("Element : (" + P[pivot] + ", " + i  + ") is 0! : " + this.getElement(P[pivot], i));
				this.setMatrix(backup);
				return P;
			}


			// Change row indexes - najveci pivot na vrh
			int temp = P[i];
			P[i] = P[pivot];
			P[pivot] = temp;

			// Racunaj normalnu LU dekompoziciju sad dalje
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

		// na kraju zamjeni poredak redaka prema polju indexa, pri racunanju ih nismo mjenjali nego samo koristili
		// indexe redaka sa najvecim pivotom i vrati polje indexa
		this.changeRows(P);
		return P;
	}

	/**
	 * Metoda mjenja retke matrice
	 * Mjenja retke trenutnoj matrici, NE stvara novu!
	 * @param P - polje integera u kojem je zapisan pravi poredak redaka
	 */
	public void changeRows(int[] P){
		double[][] change = new double[this.getRowsCount()][this.getColsCount()];
		for(int i=0; i<this.getRowsCount(); i++){
			for(int j=0; j<this.getColsCount(); j++){
				change[i][j] = this.getElement(P[i], j);
			}
		}
		this.setElements(change);
	}

	/**
	 * Inverz matrice pomocu lup dekompozicije
	 * Ne mijenja trenutnu matricu
	 * usage: x = C.inv()
	 * @return Matrix - inverznu novu matricu
	 */
	public IMatrix inv(){

		IMatrix A = this.copy();
		int rows = A.getRowsCount();
		int cols = A.getColsCount();

		int[] P = A.LUP();

		Matrix Ainv = new Matrix(rows, cols);

		for(int n=0; n<cols; n++){
			IMatrix e = colE(rows, n);
			e.SF(A, P, true);
			e.SB(A);

			Ainv.addCol(n, e);
		}

		return Ainv;

	}

	/**
	 * Metoda za dodavanje vektora u odrededenu matricu pod odredenim indexom stupca
	 * @param index - int index stupca za umetanje
	 * @param e - vektor za dodavanje
	 */
	public void addCol(int index, IMatrix e){
		for(int i=0; i<this.getRowsCount(); i++){
			this.setElement(i, index, e.getElement(i, 0));
		}
	}

	/**
	 * Metoda stvara vektor sa jedinicom na zadanom indexu, ostali elementi su 0
	 * @param rows - broj redaka vektora
	 * @param oneIndex - index jedinice
	 * @return Matrix - vektor sa jedinicom na zadanom indexu
	 */
	public IMatrix colE(int rows, int oneIndex){
		double[][] els = new double[rows][1];

		for(int i=0; i<rows; i++){
			els[i][0] = 0.0;
		}

		els[oneIndex][0] = 1.0;

		return new Matrix(rows, 1, els);
	}

	/**
	 * Ispisi matricu na ekran
	 */
	@Override
	public void printMatrix(){
		System.out.println(this.toString());
	}
	

}
