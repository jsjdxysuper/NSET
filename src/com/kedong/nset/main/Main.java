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


public class Main {

	private String sjidArray[];
	private String sjmcArray[];
	private Vector<StandardData>standardVect;
	private  int RemoveExtremNum = 0;
	private  int RangeGetAvg = 0;
	private static int index;
	private double avgBound;
	private double sqtBound;
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {


		Main main = new Main();
		System.out.println("ok");
		String strDate = "2017-11-04";
		String hisStrDate=Utilities.getAddDay(strDate, -30);
		Vector<Vector<Double>> samplesVector = new Vector<Vector<Double>>();
		samplesVector = main.getManyDayData(hisStrDate,30);
		
		Vector<Vector<Double>> memoryVect = main.calMemVect(samplesVector);
		System.out.println("avgCenter ok");

		Vector<Vector<Double>> oneDaySeeVect = main.getManyDayData(strDate, 1);
		Vector<Vector<Double>> oneDayAvgSqtVect = new Vector<Vector<Double>>();
		for(int i=0;i<oneDaySeeVect.size();i++){
			Vector<Double>tempAvgSqtVect = main.calAvgSqt(oneDaySeeVect.get(i), memoryVect);
			oneDayAvgSqtVect.add(tempAvgSqtVect);
		}
		
		main.writeDb(oneDayAvgSqtVect, strDate);
		
	}
	public Main(){
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
		
		String sjidArrayStr = env.getProperty("SJID");
		String sjmcArrayStr = env.getProperty("SJMC");
		sjidArray = sjidArrayStr.split(",");
		sjmcArray = sjmcArrayStr.split(",");
		
		String writeOrWrongBound = env.getProperty("RightOrWrongBound");
		avgBound = Double.parseDouble(writeOrWrongBound.split(",")[0]);
		sqtBound = Double.parseDouble(writeOrWrongBound.split(",")[1]);
	}
	/**
	 * 获取所有风机编码信息
	 */
	public static void getAllFjbm(){
		DBOperate dbo = new DBOperate();
		try {
			dbo.connect("newhisdb");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String fjbmSql = "select ssdc,kw,fdjzjwdxx_jzid,d5000id,fsid,dlid,dyid from NEPUBDB.NEPUBDB.FJBM";
		Vector<Vector<Object>>fjbmVec = new Vector<Vector<Object>>();
		try {
			fjbmVec = dbo.executeQuery(fjbmSql);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			dbo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public void writeDb(Vector<Vector<Double>> oneDayAvgSqtVect,String strDate){
		DBOperate dbo = new DBOperate();
		try {
			dbo.connect("newhisdb");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String deleteSql = "delete from NEPUBDB.XNYZCDB.NSETDATA where rq='"+strDate+"'";
		try {
			dbo.executeUpdate(deleteSql);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		String sqlStr = "INSERT INTO NEPUBDB.XNYZCDB.NSETDATA"+
		"(SID,SNAME,RQ,SD1,SD2,SD3,SD4,SD5,SD6,SD7,SD8,SD9,SD10,SD11,SD12,SD13,SD14,SD15,SD16,SD17,SD18,SD19,SD20, "+
		"SD21,SD22,SD23,SD24,SD25,SD26,SD27,SD28,SD29,SD30,SD31,SD32,SD33,SD34,SD35,SD36,SD37,SD38,SD39,SD40, "+
		"SD41,SD42,SD43,SD44,SD45,SD46,SD47,SD48,SD49,SD50,SD51,SD52,SD53,SD54,SD55,SD56,SD57,SD58,SD59,SD60, "+
		"SD61,SD62,SD63,SD64,SD65,SD66,SD67,SD68,SD69,SD70,SD71,SD72,SD73,SD74,SD75,SD76,SD77,SD78,SD79,SD80, "+
		"SD81,SD82,SD83,SD84,SD85,SD86,SD87,SD88,SD89,SD90,SD91,SD92,SD93,SD94,SD95,SD96,SD97,SD98,SD99,SD100, "+
		"SD101,SD102,SD103,SD104,SD105,SD106,SD107,SD108,SD109,SD110,SD111,SD112,SD113,SD114,SD115,SD116,SD117,SD118,SD119,SD120, "+
		"SD121,SD122,SD123,SD124,SD125,SD126,SD127,SD128,SD129,SD130,SD131,SD132,SD133,SD134,SD135,SD136,SD137,SD138,SD139,SD140, "+
		"SD141,SD142,SD143,SD144,SD145,SD146,SD147,SD148,SD149,SD150,SD151,SD152,SD153,SD154,SD155,SD156,SD157,SD158,SD159,SD160, "+
		"SD161,SD162,SD163,SD164,SD165,SD166,SD167,SD168,SD169,SD170,SD171,SD172,SD173,SD174,SD175,SD176,SD177,SD178,SD179,SD180, "+
		"SD181,SD182,SD183,SD184,SD185,SD186,SD187,SD188,SD189,SD190,SD191,SD192,SD193,SD194,SD195,SD196,SD197,SD198,SD199,SD200, "+
		"SD201,SD202,SD203,SD204,SD205,SD206,SD207,SD208,SD209,SD210,SD211,SD212,SD213,SD214,SD215,SD216,SD217,SD218,SD219,SD220, "+
		"SD221,SD222,SD223,SD224,SD225,SD226,SD227,SD228,SD229,SD230,SD231,SD232,SD233,SD234,SD235,SD236,SD237,SD238,SD239,SD240, "+
		"SD241,SD242,SD243,SD244,SD245,SD246,SD247,SD248,SD249,SD250,SD251,SD252,SD253,SD254,SD255,SD256,SD257,SD258,SD259,SD260, "+
		"SD261,SD262,SD263,SD264,SD265,SD266,SD267,SD268,SD269,SD270,SD271,SD272,SD273,SD274,SD275,SD276,SD277,SD278,SD279,SD280, "+
		"SD281,SD282,SD283,SD284,SD285,SD286,SD287,SD288) VALUES(";
		String valueQuery = "?,?,?";
		for(int i=0;i<oneDayAvgSqtVect.size();i++){
			valueQuery+=",?";
		}
		valueQuery+=")";
		sqlStr+=valueQuery;
		Vector<Vector<Object>> paraVecVec=new Vector<Vector<Object>>();
		
		Vector<Object> firstRowPara = new Vector<Object>();
		firstRowPara.add("avg");
		firstRowPara.add("残差均值");
		firstRowPara.add(strDate);
		for(int j=0;j<oneDayAvgSqtVect.size();j++){
			firstRowPara.add(oneDayAvgSqtVect.get(j).get(0));
		}
		paraVecVec.add(firstRowPara);
		
		Vector<Object> secondRowPara = new Vector<Object>();
		secondRowPara.add("sqt");
		secondRowPara.add("残差均方根");
		secondRowPara.add(strDate);
		for(int j=0;j<oneDayAvgSqtVect.size();j++){
			secondRowPara.add(oneDayAvgSqtVect.get(j).get(0));
		}
		paraVecVec.add(secondRowPara);
			
		
		Vector<Object> thirdRowPara = new Vector<Object>();
		thirdRowPara.add("right_or_wrong");
		thirdRowPara.add("数据是否有问题");
		thirdRowPara.add(strDate);
		for(int j=0;j<oneDayAvgSqtVect.size();j++){
			if(avgBound<oneDayAvgSqtVect.get(j).get(0)||sqtBound<oneDayAvgSqtVect.get(j).get(1))
				thirdRowPara.add(1);
			else
				thirdRowPara.add(0);
		}
		paraVecVec.add(thirdRowPara);
		
		int updateCountArr[] = new int[3];
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
	public Vector<Double>calAvgSqt(Vector<Double>seeVect,Vector<Vector<Double>> memoryVect){
//		Vector<Double>seeVect = new Vector<Double>();
//		seeVect.add(0.346);seeVect.add(0.2584);
//		seeVect.add(0.5805);seeVect.add(0.7985);
		
		Main main = new Main();
		Matrix memoryMatrix = main.convert2Matrix(memoryVect);
		Matrix weightMatrix = main.calculateWeight(memoryVect,seeVect);
		
		Matrix outputMatrix = memoryMatrix.times(weightMatrix);
		
		//残差向量
		Matrix residualMatrix = new Matrix(outputMatrix.getRowDimension(),1);

		for(int i=0;i<outputMatrix.getRowDimension();i++){
			residualMatrix.set(i, 0, outputMatrix.get(i, 0)-seeVect.get(i));
		}
		
		Double sqt = 0d;
		Double avg = 0d;
		for(int i=0;i<residualMatrix.getRowDimension();i++){
			avg +=residualMatrix.get(i, 0);
		}
		avg = avg/residualMatrix.getRowDimension();
		
		for(int i=0;i<residualMatrix.getRowDimension();i++){
			sqt += Math.pow(residualMatrix.get(i, 0)-avg, 2);
		}
		sqt = Math.sqrt(sqt/residualMatrix.getRowDimension());
		System.out.println("output ok,avg:"+avg+",sqt:"+sqt);
		Vector<Double>retVect = new Vector<Double>();
		retVect.add(avg);
		retVect.add(sqt);
		return retVect;
	}
	
	/**
	 * 计算记忆向量
	 * @param samplesVector
	 * @return
	 */
	public Vector<Vector<Double>> calMemVect(Vector<Vector<Double>> samplesVector){
		Main main = new Main();
		Vector<Vector<Double>>memoryVect = new Vector<Vector<Double>>();
		
		for(double x=0;x<1;x=x+0.1){
			Vector<Vector<Double>> vectorRange = new Vector<Vector<Double>>();
			for(int i=0;i<samplesVector.size();i++){
				if(x+0.02<samplesVector.get(i).get(0)&&
						x+0.08>samplesVector.get(i).get(0)){
					vectorRange.add(samplesVector.get(i));
				}
			}
			System.out.println("ok");
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
		return memoryVect;
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
					if(v1.get(index)==v2.get(index))
						return 0;
					else if(v1.get(index)<v2.get(index))
						return 1;
					else 
						return -1;
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
	 * 取中间一些向量的平均值
	 * @param original
	 * @param avg
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
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public  Vector<Vector<Double>> getManyDayData(String strDateIn,int count){
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
				for(int k=0;k<allDataVect.size();k++){
					Double val = Double.parseDouble(allDataVect.get(k).get(i).toString());
					try {
						fw.write(Utilities.round(val,4)+"      ");
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.print(val+"       ");
				}
				try {
					fw.write("\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println();
				
				
				for(int k=0;k<allDataVect.size();k++){
					Double val = Double.parseDouble(allDataVect.get(k).get(i).toString());
					Double standardVal = standardVect.get(k).standard(val);
					try {
						fw.write(Utilities.round(standardVal,4)+"      ");
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.print(standardVal+"       ");
				}
				try {
					fw.write("\n\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println();
				System.out.println();
				
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
