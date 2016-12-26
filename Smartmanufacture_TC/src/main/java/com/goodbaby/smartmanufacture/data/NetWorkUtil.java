package com.goodbaby.smartmanufacture.data;

import java.util.HashMap;

public class NetWorkUtil {
	/**
	 * 获取数据
	 * @param serverName 服务器名
	 * @param initialCatalog 资料库名
	 * @param sqlText sql语句
	 * @param errorMessage
	 * @return
	 */
	public static HashMap<Integer, HashMap<Integer, String>> getData(String serverName, String initialCatalog, String sqlText, String[] errorMessage){
		HashMap<Integer, HashMap<Integer, String>> map = new HashMap<Integer, HashMap<Integer,String>>();
		SOAPFunctionUtil mSOAPFunctionUtil = new SOAPFunctionUtil();
		map = mSOAPFunctionUtil.ExecuteQuery(serverName, initialCatalog, sqlText, errorMessage);
		return map;
	}

	/**
	 * 登录
	 * @param username 用户名
	 * @param password 密码
	 * @param appName app名称
	 * @param errorMessage
	 * @return String[]
	 */
	public static String[] login(String username,String password,String appName,String[] errorMessage){
		SOAPFunctionUtil mSOAPFunctionUtil = new SOAPFunctionUtil();
		String[] result = mSOAPFunctionUtil.ERPLoginFromAndroid(username, password, appName, errorMessage);
		return result;
	}

}
