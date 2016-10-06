package hr.fer.apr.linear_algebra;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class MatrixFile {
	
	public static IMatrix readMatrix(String path) throws FileNotFoundException{
		BufferedReader br = new BufferedReader(new FileReader(path));
		
		String line = "";
		Matrix matrix = new Matrix();
		try {
			List<String> rows = new ArrayList<>();
			
			while((line = br.readLine()) != null){
				rows.add(line);
			}
			
			int rowCount = rows.size();
			int colCount = rows.get(0).split(" ").length;
			double[][] elements = new double[rowCount][colCount];
			
			int i = 0;
			int j = 0;
			for(String s: rows){
				for(String el: s.split(" ")){
					elements[i][j] = Double.parseDouble(el);
					j++;
				}
				i++;
				j = 0;	
			}
			
			matrix.setColsCount(colCount);
			matrix.setRowsCount(rowCount);
			matrix.setElements(elements);
			
			return matrix;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void writeMatrix(String path, IMatrix matrix) throws IOException{
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<matrix.getRowsCount(); i++){
			for(int j=0; j<matrix.getColsCount(); j++){
				sb.append(matrix.getElement(i, j) + " ");
			}
			sb.replace(sb.lastIndexOf(" "), sb.lastIndexOf(" ")+1, "\n");
		}
		sb.replace(sb.lastIndexOf("\n"), sb.lastIndexOf("\n")+1, "");
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(path)); 
		
		bw.write(sb.toString());
		
		bw.close();
	}
	
	public static void printMatrix(String path) throws FileNotFoundException{
		System.out.println(readMatrix(path).toString());
	}

}
