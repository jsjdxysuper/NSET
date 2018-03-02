package com.kedong.nset.main;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import Jama.Matrix;

import com.kedong.nset.base.DBOperate;
import com.kedong.nset.base.Env;
import com.kedong.nset.base.Utilities;
import com.kedong.nset.bean.WindGenParaBean;

public class CalMemoryMatrix {
	private static int MINSAMPLENUM = 100;//最小样本数量
	private String sjidArrayStr;
	private String sjmcArrayStr;
	private Vector<StandardData>standardVect;
	private  int RemoveExtremNum = 0;
	private  int RangeGetAvg = 0;
	private static int index;
	private final int paraNum = 4;
	private String dateTimeStr;
	public CalMemoryMatrix(){
		Env env = Env.getInstance();
		RemoveExtremNum = Integer.parseInt(env.get("RemoveExtremNum").toString());
		RangeGetAvg = Integer.parseInt(env.get("RangeGetAvg").toString());
		
		String standardMinMaxArray[] = env.get("StandardMinMax").toString().split(";");
		standardVect = new Vector<StandardData>();
		for(int i=0;i<standardMinMaxArray.length;i++){
			Double min = Double.parseDouble(standardMinMaxArray[i].split(",")[0].toString());
			Double max = Double.parseDouble(standardMinMaxArray[i].split(",")[1].toString());
			StandardData temp = new StandardData(min,max);
			standardVect.add(temp);
		}
		
		sjidArrayStr = env.getProperty("SJID");
		sjmcArrayStr = env.getProperty("SJMC");

	}
	
	
	public static void main(String[] args) {

		String timeStr = Utilities.getSysTime();
		String dateStr = Utilities.getToday();
		
		CalMemoryMatrix calMemory = new CalMemoryMatrix();
		calMemory.dateTimeStr = dateStr+" "+timeStr;

		String strDate = "2017-11-04";
		String hisStrDate=Utilities.getAddDay(strDate, -30);
		
		Vector<Vector<Object>>plantInfoVec  = new Vector<Vector<Object>>();
		//dcid,dcmc,fjts
		plantInfoVec = getAllPlant();
		
		for(int j=0;j<plantInfoVec.size();j++){
			String plantId = plantInfoVec.get(j).get(0).toString();
		
		
			Vector<Vector<Double>> samplesVector = new Vector<Vector<Double>>();
			
			Vector<WindGenParaBean>onePlantGenBeanVec = calMemory.searchWindGenBaseData(plantId);
			
			for(int i=0;i<onePlantGenBeanVec.size();i++){
				samplesVector = calMemory.getManyDayData(onePlantGenBeanVec.get(i).getIdsStrArr(),hisStrDate,30);
				if(samplesVector.size()<MINSAMPLENUM)continue;
				Vector<Vector<Double>> memoryVect;
				try {
					memoryVect = calMemory.calMemVect(samplesVector);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				calMemory.writeDb(onePlantGenBeanVec.get(i).getDcid(),onePlantGenBeanVec.get(i).getDcmc(),
						onePlantGenBeanVec.get(i).getFjid(),onePlantGenBeanVec.get(i).getFjmc(),memoryVect,calMemory.dateTimeStr );
			}
		}
		System.out.println("avgCenter ok");
		
	}
	
	/**
	 * 获取所有可用风电场信息
	 * @return
	 */
	public static Vector<Vector<Object>> getAllPlant(){
		DBOperate dbo = new DBOperate();
		try {
			dbo.connect("newhisdb");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String fjbmSql = "select dcid,dcmc,fjts from HISDB.XNYZCDB.DCHISBM where is_use='1'";
		Vector<Vector<Object>>plantInfoVec = new Vector<Vector<Object>>();
		try {
			plantInfoVec = dbo.executeQuery(fjbmSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			dbo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return plantInfoVec;
	}
	
	/**
	 * 获取某电厂的所有风机编码信息
	 * @param dcid
	 * @return
	 * @throws EludeException 
	 */
	public Vector<WindGenParaBean> searchWindGenBaseData(String dcid) throws EludeException{
		Vector<WindGenParaBean> windGenParaIdVec = new Vector<WindGenParaBean>();
//		String sqlStr = "select SSDC,KW,FDJZJWDXX_JZID,D5000ID,FSID,DLID,DYID,D5000MC,FSMC,DLMC,DYMC " +
//				"from NEPUBDB.NEPUBDB.FJBM "+
//				"where ssdc='"+dcid+"' order by FDJZJWDXX_JZID";
		String sqlStr = "select SSDC,KW,FDJZJWDXX_JZID,GRAPH.ELEMENT_NAME,D5000ID,FSID,DLID,DYID,D5000MC,FSMC,DLMC,DYMC "+ 
						"from NEPUBDB.NEPUBDB.FJBM FJBM,NEPUBDB.XNYZCDB.WINDGEN2GRAPHELE GRAPH "+
						"where ssdc='"+dcid+"' AND FJBM.SSDC=GRAPH.DCID AND FJBM.FDJZJWDXX_JZID=GRAPH.WINDGEN_ID order by to_number(FDJZJWDXX_JZID)";	
		DBOperate dbo = new DBOperate();
		try {
			dbo.connect("newhisdb");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Vector<Vector<Object>>rst = new Vector<Vector<Object>>();
		
		try {
			rst = dbo.executeQuery(sqlStr);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		for(int i=0;i<rst.size();i++){
			WindGenParaBean oneRow = new WindGenParaBean();
			oneRow.setDcid(dcid);
			oneRow.setDcmc(rst.get(i).get(1).toString());
			oneRow.setFjid(rst.get(i).get(2).toString());
			oneRow.setFjmc(rst.get(i).get(3).toString());
			String idsStrArray[] = new String[paraNum];
			String mcsStrArray[] = new String[paraNum];
			for(int j=0;j<paraNum;j++){
				if(rst==null)throw new EludeException("风机编码id不全");
				
				idsStrArray[j] = rst.get(i).get(j+4).toString();
				mcsStrArray[j] = rst.get(i).get(j+8).toString();
			}
			oneRow.setIdsStrArr(idsStrArray);
			oneRow.setMcsStrArr(mcsStrArray);
			windGenParaIdVec.add(oneRow);
		}
		
		try {
			dbo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return windGenParaIdVec;
	}
	
	
	public void writeDb(String dcid,String dcmc,String fjid,String fjmc,Vector<Vector<Double>> memoryVectVect,String dateTimeStr){
		DBOperate dbo = new DBOperate();
		try {
			dbo.connect("newhisdb");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String deleteSql = "delete from NEPUBDB.XNYZCDB.MEMORY_MATRIX where DCID='"+dcid+"' AND FJID='"+fjid+"'";
		try {
			dbo.executeUpdate(deleteSql);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		String sqlStr = "INSERT INTO NEPUBDB.XNYZCDB.MEMORY_MATRIX(DCID, "+
				"DCMC,FJID,FJMC,MEM_MATRIX,DATA_ID,DATA_NAME,RQ_TIME) VALUES "+
				"(?,?,?,?,?,?,?,?)";
		Vector<Vector<Object>> paraVecVec=new Vector<Vector<Object>>();
		
		Vector<Object> firstRowPara = new Vector<Object>();
		firstRowPara.add(dcid);
		firstRowPara.add(dcmc);
		firstRowPara.add(fjid);
		firstRowPara.add(fjmc);
		String matrixStr = "";
		for(int i=0;i<memoryVectVect.size();i++){
			for(int j=0;j<memoryVectVect.get(0).size();j++){
				matrixStr += memoryVectVect.get(i).get(j);
				if(j==memoryVectVect.get(0).size()-1)
					continue;
				else
					matrixStr += ",";
			}
			matrixStr += ";";
		}
		firstRowPara.add(matrixStr);
		firstRowPara.add(sjidArrayStr);
		firstRowPara.add(sjmcArrayStr);
		firstRowPara.add(dateTimeStr);
		paraVecVec.add(firstRowPara);
		int updateCountArr[] = new int[1];
		try {
			updateCountArr = dbo.executeBatchUpdate(sqlStr, paraVecVec);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			dbo.commit();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			dbo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	
	/**
	 * 计算记忆向量
	 * @param samplesVector
	 * @return
	 * @throws Exception 
	 */
	public Vector<Vector<Double>> calMemVect(Vector<Vector<Double>> samplesVector) throws Exception{
		CalMemoryMatrix main = new CalMemoryMatrix();
		Vector<Vector<Double>>memoryVect = new Vector<Vector<Double>>();
		
		for(double x=0;x<1;x=x+0.1){
			Vector<Vector<Double>> vectorRange = new Vector<Vector<Double>>();
			for(int i=0;i<samplesVector.size();i++){
				if(samplesVector.get(i).get(0)>10)throw new Exception("上下限或者出力单位有问题");
				if(x+0.02<samplesVector.get(i).get(0)&&
						x+0.08>samplesVector.get(i).get(0)){
					vectorRange.add(samplesVector.get(i));
				}
			}
//			System.out.println("ok");
			if(vectorRange==null||vectorRange.size()==0)
				continue;
			int willRemove = (vectorRange.get(0).size()-1)*2*main.getRemoveExtremNum();
			if(vectorRange.size()<=willRemove)
				continue;
			vectorRange = main.filterExtreme(vectorRange);
			Vector<Double>avgVect = main.getAvgVect(vectorRange);
			Vector<Double>avgCenterVect = main.getCenterAvgVect(vectorRange,avgVect);
			
			
			memoryVect.add(avgCenterVect);
		}
		
		Vector<Vector<Double>> minVectors = new Vector<Vector<Double>>();
		for(int i=0;i<samplesVector.size();i++){
			
			if(samplesVector.get(i).get(0)<0.01&&samplesVector.get(i).get(0)>0)
				minVectors.add(samplesVector.get(i));
			
		}
		Vector<Double>minVect = main.getAvgVect(minVectors);
		
		Vector<Vector<Double>> maxVectors = new Vector<Vector<Double>>();
		for(int i=0;i<samplesVector.size();i++){
			
			if(samplesVector.get(i).get(0)<1&&samplesVector.get(i).get(0)>0.99)
				maxVectors.add(samplesVector.get(i));
			
		}
//		System.out.println("计算风电场："+)
		if(maxVectors.size()<1)throw new EludeException("最大值处，向量数量为0");
		Vector<Double>maxVect = main.getAvgVect(maxVectors);
		memoryVect.insertElementAt(minVect, 0);
		memoryVect.insertElementAt(maxVect, memoryVect.size());
		return memoryVect;
	}
	class EludeException extends Exception{

		public EludeException(String string) {
			super(string);
		}
		
	}
	/**
	 * 计算两个矩阵的欧氏距离
	 * @param first
	 * @param second
	 * @return
	 */
	public Matrix euclideanMulti(Matrix first,Matrix second){
		Matrix multiRst = new Matrix(first.getRowDimension(),second.getColumnDimension());
		
		for(int i=0;i<first.getRowDimension();i++){
			for(int j=0;j<second.getColumnDimension();j++){
				Double oneElement = 0d;
				for(int k=0;k<second.getRowDimension();k++){
					oneElement += Math.pow(first.get(i,k)-second.get(k,j),2);
				}
				multiRst.set(i, j, Math.sqrt(oneElement));
			}
		}
		return multiRst;
	}
	
	/**
	 * 计算权重矩阵
	 * @param memoryVect
	 * @param input
	 * @return
	 */
	public Matrix calculateWeight(Vector<Vector<Double>> memoryVect,Vector<Double>input){
		Matrix weightMatrix = new Matrix(memoryVect.size(),1);
		
		Matrix inputMatrix = new Matrix(input.size(),1);
		for(int i=0;i<input.size();i++){
			inputMatrix.set(i, 0, input.get(i));
		}
		Matrix memoryMatrix = this.convert2Matrix(memoryVect);
		Matrix memoryTrans = memoryMatrix.transpose();
		
	
		Matrix partOneMatrix = this.euclideanMulti(memoryTrans, memoryMatrix).inverse();
		Matrix partTwoMatrix = this.euclideanMulti(memoryTrans,inputMatrix);
		System.out.println("partOneMatrix,rows:"+partOneMatrix.getRowDimension()+",column:"+partOneMatrix.getColumnDimension());
		System.out.println("partTwoMatrix,rows:"+partTwoMatrix.getRowDimension()+",column:"+partTwoMatrix.getColumnDimension());
		weightMatrix = partOneMatrix.times(partTwoMatrix);
		return weightMatrix;
	}
	
	/**
	 * 把Vector<Vector<Double>>转化为Matrix类型
	 * @param inVect
	 * @return
	 */
	public Matrix convert2Matrix(Vector<Vector<Double>> inVect){
		Matrix retMatrix = new Matrix(inVect.get(0).size(),inVect.size());
		
		for(int i=0;i<inVect.size();i++){
			for(int j=0;j<inVect.get(0).size();j++){
				retMatrix.set(j, i, inVect.get(i).get(j));
			}
		}
		return retMatrix;
	}
	
	
	public void output(Vector<Vector<Double>> memoryVect,Vector<Double>input){
		Matrix memoryMatrix = new Matrix(memoryVect.get(0).size(),memoryVect.size());
		for(int i=0;i<memoryVect.size();i++){
			for(int j=0;j<memoryVect.get(0).size();j++){
				memoryMatrix.set(j, i, memoryVect.get(i).get(j));
			}
		}
		
		
		
	}
	
	/**
	 * 去掉向量的每个分量的极值
	 * @param original
	 * @return
	 */
	public  Vector<Vector<Double>> filterExtreme(Vector<Vector<Double>>original){
		for(index=1;index<original.get(0).size();index++){
			Collections.sort(original, new Comparator<Vector<Double>>(){
				@Override
				public int compare(Vector<Double> o1, Vector<Double> o2) {
					Vector<Double>v1 = (Vector<Double>)o1;
					Vector<Double>v2 = (Vector<Double>)o2;
					return v2.get(index).compareTo(v1.get(index));
//					if(v1.get(index)==v2.get(index))
//						return 0;
//					else if(v1.get(index)<v2.get(index))
//						return 1;
//					else 
//						return -1;
				}

			});
			
			for(int i=0;i<RemoveExtremNum;i++){
				original.remove(original.size()-1);
				original.remove(0);
			}
		}
		
		return original;
	}
	
	/**
	 * 取离平均向量近的一些向量的平均值
	 * @param original 去掉极大极小值的所有向量
	 * @param avg 均值向量
	 * @return
	 */
	public  Vector<Double>getCenterAvgVect(Vector<Vector<Double>>original,Vector<Double> avg){
		Vector<Double> avgRst = new Vector<Double>();
		//初始化
		for(int i=0;i<original.get(0).size();i++){
			avgRst.add(0d);
		}
		
		int vectCount =0;
		for(int i=0;i<original.size();i++){
			int count = 0;
			for(int j=0;j<original.get(0).size();j++){
				double avgVal = avg.get(j);
				double rangVal = ((double)RangeGetAvg)*1/100;
				if(avgVal+rangVal>original.get(i).get(j)
						&&avgVal-rangVal<original.get(i).get(j))
					count++;
			}
			if(count==original.get(0).size()){
				vectCount++;
				for(int k=0;k<original.get(0).size();k++){
					avgRst.set(k, avgRst.get(k)+original.get(i).get(k));
				}
			}
		}
		
		
		for(int i=0;i<original.get(0).size();i++){
			avgRst.set(i, avgRst.get(i)/vectCount);
		}
		return avgRst;
	}
	
	/**
	 * 求向量的每个分量均值得到的一个向量
	 * @param original
	 * @return
	 */
	public  Vector<Double>getAvgVect(Vector<Vector<Double>>original){
		Vector<Double>avg = new Vector<Double>();
		for(int i=0;i<original.get(0).size();i++){
			avg.add(0d);
		}
		
		for(int i=0;i<original.size();i++){
			for(int j=0;j<original.get(0).size();j++){
				avg.set(j, avg.get(j)+original.get(i).get(j));
			}
		}
		
		for(int i=0;i<avg.size();i++){
			avg.set(i, avg.get(i)/original.size());
		}

		return avg;
	}
	
	
	public  Vector<Double>getGoodVect(Vector<Vector<Double>>original,Vector<Double>avg){
		Vector<Double> ret = new Vector<Double>();
		
		return ret;
	}
	
	/**
	 * 获取数据库中的原始数据
	 * Vector<Double>为包括所有类型数据的一个向量
	 * @param strDateIn
	 * @param count
	 * @return 每个内部Vector为一个时刻的所有观察数据
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public  Vector<Vector<Double>> getManyDayData(String sjidArray[],String strDateIn,int count){
		FileWriter fw = null;
		try {
			fw = new FileWriter("C:\\work\\myeclipseworkspace\\NSET\\log.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Env env = Env.getInstance();
		
		String strDate = strDateIn;
		DBOperate dbo = new DBOperate();
		try {
			dbo.connect("newhisdb");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Vector<Vector<Double>> samplesVector = new Vector<Vector<Double>>();
		for(int j=0;j<count;j++){
			String sqlStr = "select * from HISDB.HISDB.ALGDATA_@ " +
					"where sid=?";
			sqlStr = sqlStr.replaceAll("@", strDate.replaceAll("-", ""));
			
			
			Vector<Vector<Object>>rst = new Vector<Vector<Object>>();
			Vector<Object>paraVect = new Vector<Object>();
			Vector<List<Object>>allDataVect = new Vector<List<Object>>();
			
			for(int i=0;i<sjidArray.length;i++){
				paraVect.add(sjidArray[i]);
				try {
					rst = dbo.executePreparedQuery(sqlStr, paraVect);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				List<Object>oneElementVect = rst.get(0).subList(2, rst.get(0).size());
				paraVect.clear();
				allDataVect.add(oneElementVect);
			}
			

			for(int i=0;i<allDataVect.get(0).size();i++){
				Vector<Double> columnOne = new Vector<Double>();
				
				for(int k=0;k<allDataVect.size();k++){
					Double val = Double.parseDouble(allDataVect.get(k).get(i).toString());
					Double standardVal = standardVect.get(k).standard(val);
					columnOne.add(standardVal);
				}
				samplesVector.add(columnOne);
				//后面的是输出和写文件
//				for(int k=0;k<allDataVect.size();k++){
//					Double val = Double.parseDouble(allDataVect.get(k).get(i).toString());
//					try {
//						fw.write(Utilities.round(val,4)+"      ");
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					System.out.print(val+"       ");
//				}
//				try {
//					fw.write("\n");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				System.out.println();
//				
//				
//				for(int k=0;k<allDataVect.size();k++){
//					Double val = Double.parseDouble(allDataVect.get(k).get(i).toString());
//					Double standardVal = standardVect.get(k).standard(val);
//					try {
//						fw.write(Utilities.round(standardVal,4)+"      ");
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					System.out.print(standardVal+"       ");
//				}
//				try {
//					fw.write("\n\n");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}

				
			}
			strDate = Utilities.getTomorrow(strDate);
		}
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			dbo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return samplesVector;
	}


	public  int getRemoveExtremNum() {
		return RemoveExtremNum;
	}


	public  void setRemoveExtremNum(int removeExtremNum) {
		RemoveExtremNum = removeExtremNum;
	}


	public  int getRangeGetAvg() {
		return RangeGetAvg;
	}


	public  void setRangeGetAvg(int rangeGetAvg) {
		RangeGetAvg = rangeGetAvg;
	}
}
