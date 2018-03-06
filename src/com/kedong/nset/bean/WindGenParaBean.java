package com.kedong.nset.bean;

import java.util.Vector;

import com.kedong.nset.main.UnitConversion;

public class WindGenParaBean {

	private String dcid;
	private String dcmc;
	private String fjid;
	private String fjmc;
	
	private String fjxh;//风机型号
	private double ratedCapacity;//额定容量(千瓦)
	private double ratedWindSpeed;//额定风速
	private double ratedVoltage=690;//额定电压(V)
	private double ratedElectric;//额定电流(Amei)
	private double ratedCapacityMult;
	
	private Vector<Vector<Double>>memoryMatrix = new Vector<Vector<Double>>();
	private String idsStrArr[];
	private String mcsStrArr[];
	
	private Vector<Vector<Double>>result = new Vector<Vector<Double>>();//均值，方差
	private Vector<Vector<Double>>amendData = new Vector<Vector<Double>>();//修正后数据
	private Vector<Vector<Double>>observeData = new Vector<Vector<Double>>();//修正后数据
	
	public void calRatedElectric(){
		ratedElectric = ratedCapacity*UnitConversion.KW2W/ratedVoltage;
	}
	
	public String getDcid() {
		return dcid;
	}
	public void setDcid(String dcid) {
		this.dcid = dcid;
	}
	public String getDcmc() {
		return dcmc;
	}
	public void setDcmc(String dcmc) {
		this.dcmc = dcmc;
	}
	public String getFjid() {
		return fjid;
	}
	public void setFjid(String fjid) {
		this.fjid = fjid;
	}
	public String getFjmc() {
		return fjmc;
	}
	public void setFjmc(String fjmc) {
		this.fjmc = fjmc;
	}
	public String[] getIdsStrArr() {
		return idsStrArr;
	}
	public void setIdsStrArr(String[] idsStrArr) {
		this.idsStrArr = idsStrArr;
	}
	public String[] getMcsStrArr() {
		return mcsStrArr;
	}
	public void setMcsStrArr(String[] mcsStrArr) {
		this.mcsStrArr = mcsStrArr;
	}
	public Vector<Vector<Double>> getMemoryMatrix() {
		return memoryMatrix;
	}
	public void setMemoryMatrix(Vector<Vector<Double>> memoryMatrix) {
		this.memoryMatrix = memoryMatrix;
	}
	public Vector<Vector<Double>> getResult() {
		return result;
	}
	public void setResult(Vector<Vector<Double>> result) {
		this.result = result;
	}
	public Vector<Vector<Double>> getAmendData() {
		return amendData;
	}
	public void setAmendData(Vector<Vector<Double>> amendData) {
		this.amendData = amendData;
	}
	public Vector<Vector<Double>> getObserveData() {
		return observeData;
	}
	public void setObserveData(Vector<Vector<Double>> observeData) {
		this.observeData = observeData;
	}
	public String getFjxh() {
		return fjxh;
	}
	public void setFjxh(String fjxh) {
		this.fjxh = fjxh;
	}
	public double getRatedCapacity() {
		return ratedCapacity;
	}
	public void setRatedCapacity(double ratedCapacity) {
		this.ratedCapacity = ratedCapacity;
		this.ratedCapacityMult = this.ratedCapacity*1.1;
	}
	public double getRatedWindSpeed() {
		return ratedWindSpeed;
	}
	public void setRatedWindSpeed(double ratedWindSpeed) {
		this.ratedWindSpeed = ratedWindSpeed;
	}
	public double getRatedVoltage() {
		return ratedVoltage;
	}
	public void setRatedVoltage(double ratedVoltage) {
		this.ratedVoltage = ratedVoltage;
	}
	public double getRatedElectric() {
		return ratedElectric;
	}
	public void setRatedElectric(double ratedElectric) {
		this.ratedElectric = ratedElectric;
	}

	public double getRatedCapacityMult() {
		return ratedCapacityMult;
	}

	public void setRatedCapacityMult(double ratedCapacityMult) {
		this.ratedCapacityMult = ratedCapacityMult;
	}
	
}
