package com.kedong.nset.bean;

import java.util.Vector;

public class WindGenParaBean {

	private String dcid;
	private String dcmc;
	private String fjid;
	private String fjmc;
	
	private Vector<Vector<Double>>memoryMatrix = new Vector<Vector<Double>>();
	private String idsStrArr[];
	private String mcsStrArr[];
	
	private Vector<Vector<Double>>result = new Vector<Vector<Double>>();//均值，方差
	private Vector<Vector<Double>>amendData = new Vector<Vector<Double>>();//修正后数据
	private Vector<Vector<Double>>observeData = new Vector<Vector<Double>>();//修正后数据
	
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
	
	
}
