package com.kedong.nset.main;

import java.sql.SQLException;
import java.util.Vector;

import com.kedong.nset.base.DBOperate;
import com.kedong.nset.base.Env;
import com.kedong.nset.bean.WindGenParaBean;

public class CalNSETResult {
	
	private static int index;
	private double avgBound;
	private double sqtBound;
	
	public CalNSETResult(){
		Env env = Env.getInstance();
		
		String writeOrWrongBound = env.getProperty("RightOrWrongBound");
		avgBound = Double.parseDouble(writeOrWrongBound.split(",")[0]);
		sqtBound = Double.parseDouble(writeOrWrongBound.split(",")[1]);
	}
	
	public Vector<WindGenParaBean>getFjBm(String dcid){
		
		Vector<String>fjVect = new Vector<String>();
		
		DBOperate dbo = new DBOperate();
		try {
			dbo.connect("newhisdb");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String sqlStr = "select dcid,dcmc,fjid,fjmc,mem_matrix,data_id,data_name from NEPUBDB.XNYZCDB.MEMORY_MATRIX "+
				"where dcid='hrlg'";
		Vector<Vector<Object>>rst = new Vector<Vector<Object>>();
		
		try {
			rst = dbo.executeQuery(sqlStr);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		Vector<WindGenParaBean>ret = new Vector<WindGenParaBean>();
		for(int i=0;i<rst.size();i++){
			WindGenParaBean oneRow = new WindGenParaBean();
			oneRow.setDcid(dcid);
			oneRow.setDcmc(rst.get(i).get(1).toString());
			oneRow.setFjid(rst.get(i).get(2).toString());
			oneRow.setFjmc(rst.get(i).get(3).toString());
			String memMatrixStr = rst.get(i).get(4).toString();
			String dataIdStr = rst.get(i).get(5).toString();
			String dataNameStr = rst.get(i).get(6).toString();
			String vectStr[] = memMatrixStr.split(";");
			for(int j=0;j<vectStr.length;j++){
				String elements[] = vectStr[j].split(",");
				Vector<Double>oneColumn = new Vector<Double>();
				for(int k=0;k<elements.length;k++)
					oneColumn.add(Double.parseDouble(elements[k].toString()));
				oneRow.getMemoryMatrix().add(oneColumn);
			}
			oneRow.setIdsStrArr(dataIdStr.split(","));
			oneRow.setMcsStrArr(dataNameStr.split(","));
			ret.add(oneRow);
		}
		try {
			dbo.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	public void writeResidual2Db(Vector<WindGenParaBean>windGenVect,String strDate){
		String sqlStr = "INSERT INTO NEPUBDB.XNYZCDB.NSETDATA"+
		"(DCID,DCMC,FJID,FJMC,SID,SNAME,RQ,SD1,SD2,SD3,SD4,SD5,SD6,SD7,SD8,SD9,SD10,SD11,SD12,SD13,SD14,SD15,SD16,SD17,SD18,SD19,SD20, "+
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
		String valueQuery = "?,?,?,?,?,?,?";
		for(int i=0;i<windGenVect.get(0).getResult().size();i++){
			valueQuery+=",?";
		}
		valueQuery+=")";
		sqlStr+=valueQuery;
		Vector<Vector<Object>> paraVecVec=new Vector<Vector<Object>>();
		for(int i=0;i<windGenVect.size();i++){
			Vector<Object> firstRow = new Vector<Object>();
			firstRow.add(windGenVect.get(i).getDcid());
			firstRow.add(windGenVect.get(i).getDcmc());
			firstRow.add(windGenVect.get(i).getFjid());
			firstRow.add(windGenVect.get(i).getFjmc());
			firstRow.add("ResidualAvg");
			firstRow.add("残差均值");
			firstRow.add(strDate);
			for(int j=0;j<windGenVect.get(i).getResult().size();j++){
				firstRow.add(windGenVect.get(i).getResult().get(j).get(0));
			}
			
//			oneRow.add("ResidualSqt");
			paraVecVec.add(firstRow);
			
			Vector<Object> secondRow = new Vector<Object>();
			secondRow.add(windGenVect.get(i).getDcid());
			secondRow.add(windGenVect.get(i).getDcmc());
			secondRow.add(windGenVect.get(i).getFjid());
			secondRow.add(windGenVect.get(i).getFjmc());
			secondRow.add("ResidualSqt");
			secondRow.add("残差均方根");
			secondRow.add(strDate);
			for(int j=0;j<windGenVect.get(i).getResult().size();j++){
				secondRow.add(windGenVect.get(i).getResult().get(j).get(1));
			}
			paraVecVec.add(secondRow);
		}
		DBOperate dbo = new DBOperate();
		try {
			dbo.connect("newhisdb");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			dbo.connect("newhisdb");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			dbo.executeBatchUpdate(sqlStr, paraVecVec);
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
	public void calForcast(Vector<WindGenParaBean>windGenVect,String strDate){
		CalMemoryMatrix calMM = new CalMemoryMatrix();
		Main main = new Main();
		for(int i=0;i<windGenVect.size();i++){
			Vector<Vector<Double>>observeData = new Vector<Vector<Double>>();
			observeData = calMM.getManyDayData(windGenVect.get(i).getIdsStrArr(), strDate, 1);
			Vector<Vector<Double>>oneGenADayResult = new Vector<Vector<Double>>();
			
			for(int j=0;j<observeData.size();j++){
				System.out.println("j="+j+",i="+i);
				Vector<Double>onePointResult = main.calAvgSqt(observeData.get(j),windGenVect.get(i).getMemoryMatrix());
				oneGenADayResult.add(onePointResult);
			}
			windGenVect.get(i).setResult(oneGenADayResult);
		}
		
	}
	public static void main(String[] args) {
		String dcbm="hrlg";
		CalNSETResult calNset = new CalNSETResult();
		Vector<WindGenParaBean>windGenVect = calNset.getFjBm(dcbm);
		calNset.calForcast(windGenVect,"2017-12-01");
		calNset.writeResidual2Db(windGenVect,"2017-12-01");
		System.out.println("down");
	}
}
