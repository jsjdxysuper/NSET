package com.kedong.nset.main;

import Jama.Matrix;

public class TestJama {
	public static void main(String[] args) {
		 double [][] array = {  
	              {-1,1,0},  
	              {-4,3,0},  
	              {1 ,0,2}}; 
		double [][]arrayB={
				{1},{2},{3}
		};
		Matrix mn = new Matrix(arrayB);
		Matrix mm = new Matrix(array);
		
		Matrix rst = mm.times(mn);
		printMatrix(mm);
		
		System.out.println();
		Matrix transeMM = mm.transpose();
		printMatrix(transeMM);
		System.out.println();
		Matrix inverseMM = mm.inverse();
		printMatrix(inverseMM);
		
		
	}
	
	
	public static void printMatrix(Matrix mm){
		for(int i=0;i<mm.getRowDimension();i++){
			for(int j=0;j<mm.getColumnDimension();j++){
				System.out.print(mm.get(i, j));
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}
