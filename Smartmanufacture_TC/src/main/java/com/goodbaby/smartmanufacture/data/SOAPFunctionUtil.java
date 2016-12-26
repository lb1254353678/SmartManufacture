package com.goodbaby.smartmanufacture.data;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;

public class SOAPFunctionUtil
{
	/**
	 *  命名空间
	 */
	private static final String nameSpace = "http://tempuri.org/";
	private static final String endPoint = "http://b2b.goodbabygroup.com/SqlExecuteService/SqlExecuteService.SqlExecute.svc";
	private static final String soapActionPrefix = "http://tempuri.org/ISqlExecute/";
	private static final String userName = "android_app1";
	private static final String password = "C8844952853136F61CA8DB8BAC173AFE";
	protected static final String TAG = "***SOAPFunctionUtil***";

	public SOAPFunctionUtil() {

	}

	/**
	 * 执行SQL语句
	 *
	 * @param initialCatalog 数据库名
	 * @param sqlText 执行SQL语句
	 * @param errorMessage 返回错误信息
	 * @return HashMap(Integer, HashMap(Integer, String))
	 * 		如语句执行正确，errorMessage="", 返回HashMap
	 * 		否则errorMessage！=""
	 */
	public HashMap<Integer, HashMap<Integer, String>> ExecuteQuery(String initialCatalog, String sqlText, String[] errorMessage)
	{
		//初始化 返回错误信息
		errorMessage[0] = "";
		// 调用的方法名称
		String methodName = "ExecuteQuery";
		// SOAP Action
		String soapAction = soapActionPrefix + methodName;

		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		// Web Service ExecuteQuery(initialCatalog, sqlText, userName, password)
		rpc.addProperty("initialCatalog", initialCatalog);
		rpc.addProperty("sqlText", sqlText);
		rpc.addProperty("userName", userName);
		rpc.addProperty("password", password);

		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);

		// 获取返回的数据
		SoapObject object;
		try {
			// 调用WebService
			transport.call(soapAction, envelope);


			// 获取返回的数据
			object = (SoapObject) envelope.bodyIn;
			//Log.i(TAG, "object:" + object.toString());

			/*
			 * 得到 执行结果相关信息：有三个参数
			 * Message=anyType{};   信息提示
			 * Result=true;  成功与否：true表示成功;false表示失败
			 * ResultDictionary=anyType{} 需要的结果集
			 */
			object = (SoapObject) object.getProperty(0);
			if (object.getProperty(1).toString().equals("false")) {
				errorMessage[0] =object.getProperty(0).toString();
				//Log.i(TAG, "error Message:" + object.getProperty(0).toString());
				//Log.i(TAG, "object:" + object.toString());
				return null;
			}
		} catch (Exception e) {
			//e.printStackTrace();
			errorMessage[0] = "调用WebService失败。"+e.toString();
			return null;
		}

		//得到具体结果集
		object = (SoapObject) object.getProperty(2);
		return resultTotal(object);
	}



	/**
	 * 执行SQL语句
	 *
	 * @param serverName 服务器名
	 * @param initialCatalog 数据库名
	 * @param sqlText 执行SQL语句
	 * @param errorMessage 返回错误信息
	 * @return HashMap(Integer, HashMap(Integer, String))
	 * 		如语句执行正确，errorMessage="", 返回HashMap
	 * 		否则errorMessage！=""
	 */
	public HashMap<Integer, HashMap<Integer, String>> ExecuteQuery(String serverName, String initialCatalog, String sqlText, String[] errorMessage){
		//初始化 返回错误信息
		errorMessage[0] = "";
		// 调用的方法名称
			String methodName = "ExecuteQueryWithServer";
		// SOAP Action
		String soapAction = soapActionPrefix + methodName;

		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);
		//Log.v("****rpc****","rpc:"+rpc);
		rpc.addProperty("serverName", serverName);
		rpc.addProperty("initialCatalog", initialCatalog);
		rpc.addProperty("sqlText", sqlText);
		rpc.addProperty("userName", userName);
		rpc.addProperty("password", password);

		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
		envelope.bodyOut = rpc;

		//Log.v("++++out++++","out:"+envelope.bodyOut);
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE transport = new HttpTransportSE(endPoint);

		// 获取返回的数据
		SoapObject object;
		try{
			// 调用WebService
			transport.call(soapAction, envelope);
			// 获取返回的数据
			object = (SoapObject) envelope.bodyIn;
			//Log.i(TAG, "*bodyIn*" + object.toString());

			/*object = (SoapObject) object.getProperty(0);
			 * 得到 执行结果相关信息：有三个参数
			 * Message=anyType{};   信息提示
			 * Result=true;  成功与否：true表示成功;false表示失败
			 * ResultDictionary=anyType{} 需要的结果集
			 */
			object = (SoapObject) object.getProperty("ExecuteQueryWithServerResult");


			if (object.getProperty("Result").toString().equals("false")){
				errorMessage[0] =object.getProperty("Message").toString();
				//Log.i(TAG, "Result = false:" + object.getProperty("Message").toString();
				return null;
			}
		} catch (Exception e){
			errorMessage[0] = "调用WebService失败。"+e.toString();
			//Log.i(TAG, "Exception:" + errorMessage[0]);
			return null;
		}

		//得到ResultDictionary具体结果集
		object = (SoapObject) object.getProperty("ResultDictionary");
		//Log.i(TAG, "object:"+object);
		return resultTotal(object);
	}

	/**
	 * 登录方法
	 * @param erpUserName
	 * @param erpPassword
	 * @param appName
	 * @param errorMessage
	 * @return
	 */
	public String[] ERPLoginFromAndroid(String erpUserName, String erpPassword, String appName, String[] errorMessage){

		// 调用的方法名称
		String methodName = "ERPLoginFromAndroid";
		// SOAP Action
		String soapAction = soapActionPrefix + methodName;

		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		// Web Service ERPLoginFromAndroid(string erpUserName, string erpPassword, string userName, string password)， 前两个是用户输入的erp用户名和密码，后两个是我上次发给你的执行ws所需的身份
		rpc.addProperty("erpUserName", erpUserName);
		rpc.addProperty("erpPassword", erpPassword);
		rpc.addProperty("userName", userName);
		rpc.addProperty("password", password);
		rpc.addProperty("appName", appName);

		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);

		// 获取返回的数据
		SoapObject object;
		try{
			// 调用WebService
			transport.call(soapAction, envelope);

			// 获取返回的数据
			object = (SoapObject) envelope.bodyIn;
			//Log.i(TAG, "object:" + object.toString());

			/** 得到 执行结果相关信息：有三个参数
			 * Message=anyType{};   信息提示
			 * Result=true;  成功与否：true表示成功;false表示失败
			 * ResultDictionary=anyType{} 需要的结果集*/

			object = (SoapObject) object.getProperty(0);
			if (object.getProperty(1).toString().equals("false")){
				errorMessage[0] =object.getProperty(0).toString();
				//Log.i(TAG, "error Message:" + object.getProperty(0).toString());
				//Log.i(TAG, "object:" + object.toString());
				return null;
			}
		} catch (Exception e){
			//e.printStackTrace();
			errorMessage[0] = "调用WebService失败。"+e.toString();
			//Log.i(TAG, "errorMessage:" + errorMessage[0]);
			return null;
		}

		Log.i(TAG, "object:" + object.toString());
		//Toast.makeText(context, "object:" + object.toString(), Toast.LENGTH_SHORT).show();
		//得到具体结果集
		String[] resultHashMap = new String[4];
		resultHashMap[0] = object.getProperty(2).toString();
		resultHashMap[1] = object.getProperty(3).toString();
		resultHashMap[2] = object.getProperty(4).toString();
		resultHashMap[3] = object.getProperty(5).toString();

		return resultHashMap;

	}

	/**
	 * 处理结果集
	 * @param rowObject
	 * @return
	 */
	private HashMap<Integer, HashMap<Integer, String>> resultTotal(SoapObject rowObject){
		HashMap<Integer, String> colMap; // = new HashMap<Integer, String>();
		HashMap<Integer, HashMap<Integer, String>> resultHashMap = new HashMap<Integer, HashMap<Integer, String>>();

		SoapObject colObject;
		String lsString;
		//int resultTotalRows; // 返回结果总行数
		//resultTotalRows = rowObject.getPropertyCount();

		for (int i = 0; i < rowObject.getPropertyCount(); i++){
			colMap = new HashMap<Integer, String>();
			colObject = (SoapObject) rowObject.getProperty(i);
			colObject = (SoapObject) colObject.getProperty(1);
			for (int j = 0; j < colObject.getPropertyCount(); j++){
				lsString = ((SoapObject) colObject.getProperty(j)).getProperty(1).toString();
				if(lsString.equals("anyType{}")){
					lsString = "";
				}
				colMap.put(j, lsString);
			}
			resultHashMap.put(i, colMap);
		}
		return resultHashMap;
	}

}
