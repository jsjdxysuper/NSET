package com.kedong.nset.base;

import java.util.Properties;
import java.io.*;

import org.apache.log4j.*;

/**
 * 读取配置文件config.properties的内容
 *
 */
public class Env extends Properties {

	private static final long serialVersionUID = 1L;
	
	Logger logger = Logger.getLogger(Env.class);
	
	private static Env instance;

	/**
	 * 获取Env对象instance
	 * @return
	 * Env对象
	 */
	public static Env getInstance() {
		if (instance != null) {
			return instance;
		} else {
			makeInstance();
			return instance;
		}
	}

	/**
	 * 设置Env对象instance
	 * @param instance
	 * Env对象
	 */
	public static void setInstance(Env instance) {
		Env.instance = instance;
	}

	/**
	 * 实例化Env对象instance
	 */
	private static synchronized void makeInstance() {
		if (instance == null) {
			instance = new Env();
		}
	}

	/**
	 * 读取配置文件config.properties
	 */
	private Env() {
		
		InputStream is = getClass().getResourceAsStream("/config.ini");
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(is, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			load(reader);
		} catch (Exception e) {
			logger.error("FILE 'config.properties' READ ERROR !");
		}
	}
}
