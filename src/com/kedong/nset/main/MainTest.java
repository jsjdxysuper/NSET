package com.kedong.nset.main;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import org.junit.Test;

import Jama.Matrix;

public class MainTest {

	@Test
	public void testMain() {
		fail("Not yet implemented");
	}

	@Test
	public void testFilterExtreme() {
		Vector<Vector<Double>>original = new Vector<Vector<Double>>();
		Vector<Vector<Double>>removedOriginal = new Vector<Vector<Double>>();
		Vector<Vector<Double>>filteredVect = new Vector<Vector<Double>>();
		
		Main main = new Main();
		original = main.getManyDayData("2017-11-11", 1);
		int originalSize = original.size();
		filteredVect = main.filterExtreme(original);
		
		assertTrue(filteredVect.size()==(originalSize-(original.get(0).size()-1)*main.getRemoveExtremNum()*2));
	}

	@Test
	public void testGetCenterAvgVect() {
		Vector<Vector<Double>>original = new Vector<Vector<Double>>();
		Vector<Double> one = new Vector<Double>();
		one.add(5d);
		one.add(6d);
		one.add(7d);
		one.add(8d);
		Vector<Double> two = new Vector<Double>();
		two.add(7d);
		two.add(6d);
		two.add(5d);
		two.add(4d);		
		Vector<Double> three = new Vector<Double>();
		three.add(15d);
		three.add(16d);
		three.add(17d);
		three.add(18d);
		
		original.add(one);
		original.add(two);
		original.add(three);
		Vector<Double> avg = new Vector<Double>();
		Vector<Double> centerAvg = new Vector<Double>();
		Main main = new Main();
		avg = main.getAvgVect(original);
		centerAvg = main.getCenterAvgVect(original, avg);
	}

	@Test
	public void testGetAvgVect() {
		Vector<Vector<Double>>original = new Vector<Vector<Double>>();
		Vector<Double> one = new Vector<Double>();
		one.add(5d);
		one.add(6d);
		one.add(7d);
		one.add(8d);
		Vector<Double> two = new Vector<Double>();
		two.add(7d);
		two.add(6d);
		two.add(5d);
		two.add(4d);		
		Vector<Double> three = new Vector<Double>();
		three.add(15d);
		three.add(16d);
		three.add(17d);
		three.add(18d);
		
		original.add(one);
		original.add(two);
		original.add(three);
		Vector<Double> rst = new Vector<Double>();
		Main main = new Main();
		rst = main.getAvgVect(original);
		assertTrue(rst.get(0)==9);
		assertTrue(rst.get(1)>9.3&&rst.get(1)<9.4);
		assertTrue(rst.get(2)>9.6&&rst.get(2)<9.7);
		assertTrue(rst.get(3)==10);
	}

	@Test
	public void testGetGoodVect() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetManyDayData() {
		Main main = new Main();
		Vector<Vector<Double>>original = new Vector<Vector<Double>>();
		original = main.getManyDayData("2017-11-10", 1);
		assertTrue(original.size()==1440);
		for(int i=0;i<original.size();i++){
			assertTrue(original.get(i).size()==4);
			for(int j=0;j<original.get(i).size();j++){
				System.out.println("i="+i+",j="+j+",val="+original.get(i).get(j));
				assertTrue(original.get(i).get(j)<=1&&original.get(i).get(j)>=0);
			}
		}

	}
	
	@Test
	public void testEuclideanMulti(){
		 double [][] arrayA = {  
	              {-1,1,0},  
	              {-4,3,0},  
	              {1 ,0,2},
	              {2,1,5}}; 
		 double [][]arrayB = {
				 {5,4,2,4},
				 {1,2,5,3},
				 {2,5,6,2}
		 };
		Matrix matrixA= new Matrix(arrayA);
		Matrix matrixB= new Matrix(arrayB);
		Main main = new Main();
		Matrix rst = main.euclideanMulti(matrixA, matrixB);
		
		for(int i=0;i<rst.getRowDimension();i++){
			System.out.println();
			for(int j=0;j<rst.getColumnDimension();j++){
				System.out.print(rst.get(i, j));
				System.out.print(",");
			}
		}
	}
	
	@Test
	public void testConvert2Matrix(){
		Main main = new Main();
		
		Vector<Vector<Double>>inVect = new Vector<Vector<Double>>();
		Vector<Double>oneColumn = new Vector<Double>();
		oneColumn.add(5d);oneColumn.add(4d);oneColumn.add(2d);oneColumn.add(4d);
		Vector<Double>twoColumn = new Vector<Double>();
		twoColumn.add(1d);twoColumn.add(2d);twoColumn.add(5d);twoColumn.add(3d);
		Vector<Double>threeColumn = new Vector<Double>();
		threeColumn.add(2d);threeColumn.add(5d);threeColumn.add(6d);threeColumn.add(2d);
		inVect.add(oneColumn);inVect.add(twoColumn);inVect.add(threeColumn);
		
		Matrix  m = main.convert2Matrix(inVect);
		
		for(int i=0;i<m.getRowDimension();i++){
			System.out.println();
			for(int j=0;j<m.getColumnDimension();j++){
				System.out.print(m.get(i, j));
				System.out.print(",");
			}
		}
		
		Matrix  transM = m.transpose();
		
		for(int i=0;i<transM.getRowDimension();i++){
			System.out.println();
			for(int j=0;j<transM.getColumnDimension();j++){
				System.out.print(transM.get(i, j));
				System.out.print(",");
			}
		}
	}

}
