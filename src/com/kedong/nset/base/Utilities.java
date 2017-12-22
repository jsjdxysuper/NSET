package com.kedong.nset.base;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公用方法类，包含一般常用的方法
 *
 */
public class Utilities {

	/**
	 * 判断参数date是否在参数startdate至参数enddate日期之间
	 * @param startdate
	 * 起始日期，String型日期描述，格式：yyyy-MM-dd
	 * @param enddate
	 * 终止日期，String型日期描述，格式：yyyy-MM-dd
	 * @param date
	 * 需要判断的日期，String型日期描述，格式：yyyy-MM-dd
	 * @return
	 * 当date在startdate和enddate之间时返回true，否则返回false
	 */
	public static boolean IsStartAndEndTime(String startdate, String enddate, String date) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt1 = new Date();
		Date dt2 = new Date();
		Date dt3 = new Date();
		try {
			dt1 = sf.parse(startdate);
			dt2 = sf.parse(enddate);
			dt3 = sf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (dt3.before(dt1) || dt3.after(dt2)) {
			return false;
		}

		return true;
	}

	public static boolean IsStartAndEndTime(String startdate, String enddate) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt1 = new Date();
		Date dt2 = new Date();
		try {
			dt1 = sf.parse(startdate);
			dt2 = sf.parse(enddate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if ( dt1.after(dt2)||dt1.equals(dt2)) {
			return false;
		}

		return true;
	}
	/**
	 * 获取系统当前时间
	 * @return
	 * String型时间描述，格式：HH:mm:ss
	 */
	public static String getSysTime() {
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
		return sf.format(new Date());
	}

	/**
	 * 获取给定日期之后或者之前的第 num天
	 * @param strDate
	 * @param num
	 * @return
	 */
	public static String getAddDay(String strDate,int num){
		String rstDate = strDate;
		if(num>0){
			for(int i=0;i<num;i++)
				rstDate = Utilities.getTomorrow(rstDate);
		}else
			for(int i=0;i<Math.abs(num);i++)
				rstDate = Utilities.getYesterday(rstDate);
		
		return rstDate;
	}
	/**
	 * 获取系统当前日期
	 * @return
	 * String型日期描述，格式： yyyy-MM-dd
	 */
	public static String getToday() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(new Date());
	}

	/**
	 * 获取昨日日期，参数格式yyyy-MM-dd
	 * @param today
	 * 当前日期，String型日期描述，格式：yyyy-MM-dd
	 * @return
	 * 昨日日期，String型日期描述，格式：yyyy-MM-dd
	 */
	public static String getYesterday(String today) {
		int tyear, tmonth, tday;
		StringTokenizer st = new StringTokenizer(today, "-");
		int year = Integer.parseInt(st.nextToken().trim());
		int month = Integer.parseInt(st.nextToken().trim());
		int day = Integer.parseInt(st.nextToken().trim());

		if (day == 1) {
			if (month == 1) {
				tyear = year - 1;
				tmonth = 12;
			} else {
				tyear = year;
				tmonth = month - 1;
			}
			tday = dayInmonth(tyear, tmonth);
		} else {
			tyear = year;
			tmonth = month;
			tday = day - 1;
		}
		String tmonthString = "";
		if (tmonth < 10)
			tmonthString = "0" + tmonth;
		else
			tmonthString = "" + tmonth;
		String tdayString = "";
		if (tday < 10)
			tdayString = "0" + tday;
		else
			tdayString = "" + tday;
		return tyear + "-" + tmonthString + "-" + tdayString;
	}

	/**
	 * 获取明日日期，参数格式yyyy-MM-dd
	 * @param today
	 * 当前日期，String型日期描述，格式：yyyy-MM-dd
	 * @return
	 * 明日日期，String型日期描述，格式：yyyy-MM-dd
	 */
	public static String getTomorrow(String today) {
		int tyear, tmonth, tday;
		StringTokenizer st = new StringTokenizer(today, "-");
		int year = Integer.parseInt(st.nextToken().trim());
		int month = Integer.parseInt(st.nextToken().trim());
		int day = Integer.parseInt(st.nextToken().trim());

		if (day == dayInmonth(year, month)) {
			if (month == 12) {
				tyear = year + 1;
				tmonth = 1;
			} else {
				tyear = year;
				tmonth = month + 1;
			}
			tday = 1;
		} else {
			tyear = year;
			tmonth = month;
			tday = day + 1;
		}
		String tmonthString = "";
		if (tmonth < 10)
			tmonthString = "0" + tmonth;
		else
			tmonthString = "" + tmonth;
		String tdayString = "";
		if (tday < 10)
			tdayString = "0" + tday;
		else
			tdayString = "" + tday;
		return tyear + "-" + tmonthString + "-" + tdayString;
	}

	/**
	 * 判断参数年月包含的天数
	 * @param Year
	 * int型年描述
	 * @param Month
	 * int型月描述
	 * @return
	 * int型天数
	 */
	public static int dayInmonth(int Year, int Month) {
		switch (Month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return (31);
		case 4:
		case 6:
		case 9:
		case 11:
			return (30);
		default:
			if (Year % 4 != 0) {
				return (28);
			} else if (Year % 100 != 0) {
				return (29);
			} else if (Year % 400 != 0) {
				return (28);
			} else {
				return (29);
			}
		}
	}

	/**
	 * 获取指定位数的随机字符串
	 * @param size
	 * int型字符串位数
	 * @return
	 * 参数位数的随机字符串
	 */
	public static String getRandomString(int size) {
		char[] c = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q',
				'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd',
				'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n',
				'm', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A',
				'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V',
				'B', 'N', 'M' };
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			sb.append(c[Math.abs(random.nextInt()) % c.length]);
		}
		return sb.toString();
	}

	/**
	 * 获取指定位数的随机数字字符串
	 * @param size
	 * int型字符串位数
	 * @return
	 * 参数位数的随机数字字符串
	 */
	public static String getRandomNumber(int size) {
		char[] c = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			sb.append(c[Math.abs(random.nextInt()) % c.length]);
		}
		return sb.toString();
	}

	/**
	 * 字符流转换成字符串
	 * @param in
	 * 输入字符流
	 * @param charset
	 * 返回的字符集
	 * @return
	 * 按照参数字符集转换参数字符流产生的字符串
	 */
	public static String stream2String(InputStream in, String charset) {
		StringBuffer sb = new StringBuffer();
		try {
			Reader r = new InputStreamReader(in, charset);
			int length = 0;
			for (char[] c = new char[1024]; (length = r.read(c)) != -1;) {
				sb.append(c, 0, length);
			}
			r.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取参数startdate至参数enddate日期之间的天数
	 * @param startDay
	 * 起始日期，String型日期描述，格式：yyyy-MM-dd
	 * @param endDay
	 * 终止日期，String型日期描述，格式：yyyy-MM-dd
	 * @return
	 * long型天数
	 */
	public static long getDayCount(String startDay, String endDay) {
		long dayCount = 0;

		String day = startDay;
		while (day.equals(endDay) == false) {
			day = getTomorrow(day);
			dayCount++;
		}

		return dayCount;
	}

	/**
	 * 96点时段转换为时间
	 * @param sd
	 * int型时段描述
	 * @return
	 * String型时间描述，格式HH:mm
	 */
	public static String sd96tosj(int sd) {
		int h = ((sd - 1) * 15) / 60;
		int m = ((sd - 1) * 15) % 60;
		String sh = "";
		String sm = "";

		if (h < 10) {
			sh = "0" + h;
		} else {
			sh = "" + h;
		}
		if (m < 10) {
			sm = "0" + m;
		} else {
			sm = "" + m;
		}


		return sh + ":" + sm;
	}

	/**
	 * 288点时段转换为时间
	 * @param sd
	 * int型时段描述
	 * @return
	 * String型时间描述，格式HH:mm
	 */
	public static String sd288tosj(int sd) {
		int h = ((sd - 1) * 5) / 60;
		int m = ((sd - 1) * 5) % 60;
		String sh = "";
		String sm = "";

		if (h < 10) {
			sh = "0" + h;
		} else {
			sh = "" + h;
		}
		if (m < 10) {
			sm = "0" + m;
		} else {
			sm = "" + m;
		}


		return sh + ":" + sm;
	}

	/**
	 * 1440点时段转换为时间
	 * @param sd
	 * int型时段描述
	 * @return
	 * String型时间描述，格式HH:mm
	 */
	public static String sd1440tosj(int sd) {
		int h = ((sd - 1) * 1) / 60;
		int m = ((sd - 1) * 1) % 60;
		String sh = "";
		String sm = "";

		if (h < 10) {
			sh = "0" + h;
		} else {
			sh = "" + h;
		}
		if (m < 10) {
			sm = "0" + m;
		} else {
			sm = "" + m;
		}


		return sh + ":" + sm + ":00";
	}
	
	/**
	 * 288点时间转换为时段
	 * @param sj
	 * String型时间描述，格式HH:mm
	 * @return
	 * int型时段描述
	 */
	public static int sjtosd288(String sj) {
		return (Integer.parseInt(sj.split(":")[0]) * 60 + Integer.parseInt(sj.split(":")[1])) / 5 + 1;
	}

	/**
	 * 96点时间转换为时段
	 * @param sj
	 * String型时间描述，格式HH:mm
	 * @return
	 * int型时段描述
	 */
	public static int sjtosd96(String sj) {
		return (Integer.parseInt(sj.split(":")[0]) * 60 + Integer.parseInt(sj.split(":")[1])) / 15 + 1;
	}

	/**
	 * 字符串字符集转换
	 * @param str
	 * 输入字符串
	 * @param fromcharset
	 * 原始字符串字符集
	 * @param tocharset
	 * 结果字符串字符集
	 * @return
	 * 经字符集转换后的字符串
	 */
	public static String strCharsetChange(String str, String fromcharset, String tocharset) {
		System.out.println("System Charset : " + Charset.defaultCharset().toString());

		String fromchar = fromcharset;
		String tochar = tocharset;
		String resstr = "";
		try {
			if (fromchar.indexOf("auto_system") != -1) {
				fromchar = "GBK";
			}
			if (tochar.indexOf("auto_system") != -1) {
				tochar = Charset.defaultCharset().toString();
			}
			resstr = new String(str.getBytes(fromchar), tochar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resstr;
	}

	/**
	 * 保留指定位数的小数
	 * @param v
	 * 原始数字
	 * @param scale
	 * 需要保留的位数
	 * @return
	 * 结果数字
	 */
	public static double round(double v, int scale) {
		try {
			scale = Math.abs(scale);
			BigDecimal b = new BigDecimal(Double.toString(v));
			BigDecimal one = new BigDecimal("1");
			return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		catch (Exception ex) {
			return 0.0;
		}
	}
	
	
	public static Vector<Vector<Object>> roundSQLResult(Vector<Vector<Object>> sqlVec) {
		Vector<Vector<Object>> resVec = new Vector<Vector<Object>>();
		Vector<Object> tmpResVec = new Vector<Object>();
		Object tmpResObj = null;
		
		for (int i = 0; i < sqlVec.size(); i++) {
			Vector<Object> tmpVec = sqlVec.get(i);
			tmpResVec = new Vector<Object>();
			
			for (int j = 0; j < tmpVec.size(); j++) {
				Object tmpObj = tmpVec.get(j);

				
				
				if (tmpObj == null || tmpObj.equals(null) || tmpObj.equals("null") || tmpObj == "null") {
					tmpResObj = " ";
				} else {
					if (isNumber(tmpObj.toString()) == true) {
						String [] tmpStrArray = tmpObj.toString().split("\\.");

						if (tmpStrArray.length == 1) {
							tmpResObj = tmpStrArray[0];
						} else {
							String tmpStr = "";
							
							char [] tmpCharArray = tmpStrArray[1].toCharArray();
							int m = tmpCharArray.length;
							int n = tmpCharArray.length;
							for (int k = tmpCharArray.length - 1; k >= 0; k--) {
								String tmpChar = String.valueOf(tmpCharArray[k]);
								if (Integer.parseInt(tmpChar) != 0) {
									m = k;
									break;
								}
								n = k;
							}
							
							if (n == 0) {
								tmpStr = tmpStrArray[0];
							} else {
								tmpStr = tmpStrArray[0] + ".";

								for (int k = 0; k <= m; k++) {
									tmpStr += String.valueOf(tmpCharArray[k]);
								}
							}
							
							tmpResObj = tmpStr;
						}
					} else {
						tmpResObj = tmpObj.toString();
					}
				}
				
				tmpResVec.add(tmpResObj);
			}
			
			resVec.add(tmpResVec);
		}
		
		
		return resVec;
	}
	

	public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^-?[0-9]+(.[0-9]*)?$");
        Matcher match=pattern.matcher(str);
        if(match.matches()) {
        	return true;
        } else {
        	return false;
        }
    }
	
	//////阿拉伯数字转换成罗马数字
	public static String a2r(int aNumber){
        if(aNumber < 1 || aNumber > 3999){
            return "-1";
        }
        int[] aArray = {90,80,70,60,50,40,30,20,10,9,8,7,6,5,4,3,2,1};
        String[] rArray = {"九十","八十","七十","六十","五十","四十","三十","二十","十","九","八","七","六","五","四","三","二","一"};
        String rNumber = "";
        for(int i=0; i<aArray.length; i++){
            while(aNumber >= aArray[i]){
                rNumber += rArray[i];
                aNumber -= aArray[i];
            }
        }
        return rNumber;
    }
}