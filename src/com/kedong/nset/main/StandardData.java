package com.kedong.nset.main;

public class StandardData {
	private double max;
	private double min;
	private double ratedVal;//额定
	private double val;
	
	public StandardData(double min,double max){
		this.max = max;
		this.min = min;
	}
	
	public double standard(double val){
		return (val-min)/(max-min);
	}

	public double PUValue(double val){
		return val/ratedVal;
	}
	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getVal() {
		return val;
	}

	public void setVal(double val) {
		this.val = val;
	}

	public double getRatedVal() {
		return ratedVal;
	}

	public void setRatedVal(double ratedVal) {
		this.ratedVal = ratedVal;
	}
	
	
}
