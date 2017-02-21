package com.goodbaby.smartmanufacture.global;

/**
 * Created by XXZX_LB on 2016/6/15.
 */
public class Constant {
	/**
	 * 服务器名
	 */
	public static final String SERVERNAME = "server48";
	/**
	 * 数据数据库名
	 */
	public static final String INITIALCATALOG_ADX = "ADX";
	/**
	 * 登录账号数据库名
	 */
	public static final String INITIALCATALOG_SYSLOG = "SysLog";

	public static final String TEL = "tel";
	public static final String PASSWORD = "password";
	public static final String APPNAME = "appname";
	public static final String USERID = "userid";
	public static final String ISVALID = "isvalid";
	public static final String REMARK = "remark";
	public static final String AUTOLOGIN = "autologin";
	public static final String REMEMBERPSW = "rememberpsw";
	public static final String LOGINTIME = "logintime";

	public static final long DELAYTIME = 60000;

	//广播接收filter字符串
	public static final String LAST_NEWS_RECIVER = "com.goodbaby.last_news_RECIVER";
	public static final String BAR_CHART_RECIVER = "com.goodbaby.bar_chart_RECIVER";
	public static final String SITE_STATUS_RECIVER = "com.goodbaby.site_status_RECIVER";
	public static final String WARNING_COUNT_RECIVER = "com.goodbaby.warning_count_RECIVER";
	public static final String MODEL_RECIVER = "com.goodbaby.model_RECIVER";



	/**
	 * 下载安装包路径
	 */
	public static final String saveFileName = "/GBFile/apk";
	/**
	 * 测试flag
	 */
	public static final int DEBUG = -2;
	/**
	 * 获取当前时间
	 */
	public static final int CURRENT_TIME = 0;
	/**
	 * 返回数据失败
	 */
	public static final  int FAILED = -1;
	/**
	 * 返回数据成功
	 */
	public static final int SUCCESS = 1;
	/**
	 * 出现异常
	 */
	public static final int EXCEPTION = 2;
	/**
	 * 无网络
	 */
	public static final int NO_NETWORK = 3;
	/**
	 * update
	 */
	public static final int UPDATE = 4;


	/**
	 * 获取sys版本号sql
	 */
	public static String sql_sysVersion = "SELECT * FROM dbo.AppVersion WHERE AppName = '推车智能制造'";

	/**
	 * 获取所有groupName sql
	 */
	public static String sql_getGroupName = "SELECT DISTINCT GroupName,B.Gzzxmch  FROM [172.16.42.18].ADX.dbo.DSXML A INNER JOIN [172.16.42.18].ADX.dbo.MRP_gzzxb B ON A.GroupName = b.Gzzxbh";

	/**
	 * 获取最新资讯sql
	 * @param xb
	 * @return
	 */
	public static String sql_lastnews(String xb){
		String sql= "SELECT * FROM dbo.DSXML WHERE ValueName = 'LastNews' AND GroupName = '"+xb+"' " ;
		return sql;
	}

	/**
	 * 获取产量统计数据sql --> BarChartProductionStatistics
	 * @param xb
	 * @return
	 */
	public static String sql_ProductionStatistics(String xb){
		String sql = "SELECT * FROM dbo.DSXML WHERE FatherStyle = 'ProductionStatistics' AND Style IN ('JHSL','Real','Standard') AND GroupName = '"+xb+"' "
				+ "ORDER BY Style,ValueName";
		return sql;
	}

	/**
	 * 获取效率数据sql --> BarChartAchieveRate
	 * @param xb
	 * @return
	 */
	public static String sql_EfficiencyStatistics(String xb){
		String sql = "SELECT * FROM dbo.DSXML WHERE FatherStyle = 'EfficiencyStatistics'  AND Style IN ('JHDCL','Effciency') AND GroupName = '"+xb+"' ORDER BY Style,ValueName";
		return sql;
	}

	/**
	 * 获取翻线数据sql --> BarChartChangeLine
	 * @param xb
	 * @return
	 */
	public static String sql_ChangeLine(String xb){
		String sql = "SELECT * FROM dbo.DSXML WHERE FatherStyle = 'ProductionStatistics' AND Style = 'ChangeLine' AND GroupName = '"+xb+"' ORDER BY Style,ValueName";
		return sql;
	}

	/**
	 * 获取pph数据sql --> BarChartPPH
	 * @param xb
	 * @return
	 */
	public static String sql_PPH(String xb){
		String sql = "SELECT * FROM dbo.DSXML WHERE FatherStyle = 'EfficiencyStatistics' AND Style = 'PPH' AND GroupName = '"+xb+"' ORDER BY Style,ValueName";
		return sql;
	}

	/**
	 * 获取警报次数统计sql
	 * @param xb
	 * @return
	 */
	public static String sql_warning_count(String xb){
		String  sql = "SELECT * FROM dbo.DSXML WHERE FatherStyle = 'WarningCount' AND GroupName = '"+xb+"' ORDER BY ValueName";
		return sql;
	}

	/**
	 * 获取站点状态sql
	 * @param xb
	 * @return
	 */
	public static String sql_site_status(String xb){
		String sql = "SELECT * FROM dbo.DSXML WHERE FatherStyle = 'DeviceStatus' AND GroupName = '"+xb+"' ORDER BY ValueName";
		return sql;
	}

	/**
	 * 获取型号sql
	 * @param xb
	 * @return
	 */
	public static String sql_devicemodel(String xb){
		String sql = "SELECT * FROM dbo.DSXML WHERE ValueName = 'DeviceModel' AND GroupName = '"+xb+"'";
		return sql;
	}










	/**
	 * 产线效率统计
	 */
	public static String sql_chart_detail(String xb){
		String sql = "SELECT  b.* ,SUBSTRING(b.rq,12,5) AS time,\r\n" +
				//"        CONVERT(DECIMAL,(ISNULL(SumPerHour,0)*(datediff(Minute,CONVERT(VARCHAR(14),GETDATE(),120) +' 00:00',GETDATE() )*1.00/60))) AS SumPerHour\r\n" +
				"CONVERT(INT,a.SumPerHour) AS SumPerHour\r\n" +
				"FROM    [172.16.42.18].ADX.dbo.ADX_DocMain a\r\n" +
				"        INNER JOIN ( SELECT  COUNT(1) AS zs ,\r\n" +
				"                            Gzzxbh ,\r\n" +
				"                            A.rq\r\n" +
				"                    FROM    ( SELECT    * ,\r\n" +
				"                                        CONVERT(VARCHAR(14), Jsrq, 120)\r\n" +
				"                                        + '00:00' AS rq\r\n" +
				"                              FROM      [172.16.8.36].WebMisT1.dbo.v_Dzc_Weight\r\n" +
				"                              WHERE     Gzzxbh = '"+xb+"'\r\n" +
				"                                        AND Jsrq > = CONVERT(VARCHAR(10), GETDATE(), 120)\r\n" +
				"                                        + +' 00:00:00'\r\n" +
				"                            ) A\r\n" +
				"                    GROUP BY Gzzxbh ,\r\n" +
				"                            rq\r\n" +
				"                  ) b ON a.cjmc = b.Gzzxbh\r\n" +
				"WHERE   a.zt = 'Y'\r\n" +
				"        AND cjmc = '"+xb+"'";
		return sql;
	}

	/**
	 * 当日累计异常次数明细
	 */
	public static String  sql_warning_count_detail_inday(String xb){
		String sql = "SELECT SUM(A.ZS) AS ZS,A.DeviceName,A.Style "
				+ "FROM (SELECT COUNT(1) ZS,DeviceName,AlarmStyle ,"
				+ "CASE A.AlarmStyle "
				+ "WHEN '0A' THEN '1' "
				+ "WHEN '0B' THEN '1' "
				+ "WHEN '0C' THEN '1' "
				+ "WHEN '0D' THEN '2' "
				+ "WHEN '0E' THEN '3' "
				+ "WHEN '0F' THEN '3' "
				+ "END "
				+ "AS Style "
				+ "FROM [172.16.42.18].ADX.dbo.AlarmLog A "
				+ "INNER JOIN  [172.16.42.18].ADX.dbo.DeviceBasicData B ON A.DeviceID = B.DeviceID "
				+ "WHERE GroupID= '"+xb+"' "
				+ "AND CONVERT(varchar(10) , DoTime, 120 ) = CONVERT(varchar(10) , GETDATE(), 120 ) "
				+ "GROUP BY DeviceName,AlarmStyle) A "
				+ "GROUP BY DeviceName,STYLE " ;
		return sql;
	}

	/**
	 * 最近小时异常次数明细
	 */
	public static String sql_warning_count_detail_lasthour(String xb){
		String sql = "SELECT COUNT(1) AS zs , DeviceName ,AlarmStyle "
				+ "FROM [172.16.42.18].ADX.dbo.AlarmLog A "
				+ "INNER JOIN [172.16.42.18].ADX.dbo.DeviceBasicData B ON A.SendDevID = B.DeviceID "
				+ "WHERE GroupID = '"+xb+"' "
				+ "AND DoTime > CONVERT(VARCHAR(14), GETDATE(), 120) + ' 00:00' "
				+ "AND DeviceName IN ( 'U1', 'U2', 'U3', 'U4', 'U5', 'U6', 'U7', 'U8', 'Z1', 'Z2', 'Z3' ) "
				+ "GROUP BY DeviceName , AlarmStyle";
		return sql;
	}

	/**
	 * 质量异常次数明细
	 */
	public static String sql_warning_count_detail_quantity(String xb){
		String sql  = "SELECT COUNT(1) AS zs ,DeviceName "+
				"FROM [172.16.42.18].ADX.dbo.AlarmLog A "+
				"INNER JOIN  [172.16.42.18].ADX.dbo.DeviceBasicData B ON A.SendDevID = B.DeviceID "+
				"WHERE AlarmStyle IN('0A','0B','0C') "+
				"AND GroupID = '"+xb+"' "+
				"AND CONVERT(varchar(10) , DoTime, 120 ) = CONVERT(varchar(10) , GETDATE(), 120 ) "+
				"GROUP BY DeviceName";
		return sql;
	}

	/**
	 * 物料异常次数明细
	 */
	public static String sql_warning_count_detail_material(String xb){
		String sql  = "SELECT COUNT(1) AS zs ,DeviceName "+
				"FROM [172.16.42.18].ADX.dbo.AlarmLog A "+
				"INNER JOIN  [172.16.42.18].ADX.dbo.DeviceBasicData B ON A.SendDevID = B.DeviceID "+
				"WHERE AlarmStyle IN('0D') "+
				"AND GroupID = '"+xb+"' "+
				"AND CONVERT(varchar(10) , DoTime, 120 ) = CONVERT(varchar(10) , GETDATE(), 120 ) "+
				"GROUP BY DeviceName";
		return sql;
	}

	/**
	 * 节拍异常次数明细
	 */
	public static String sql_warning_count_detail_beatwarning(String xb){
		String sql  = "SELECT COUNT(1) AS zs ,DeviceName "+
				"FROM [172.16.42.18].ADX.dbo.AlarmLog A "+
				"INNER JOIN  [172.16.42.18].ADX.dbo.DeviceBasicData B ON A.SendDevID = B.DeviceID "+
				"WHERE AlarmStyle IN('0E','0F') "+
				"AND GroupID = '"+xb+"' "+
				"AND CONVERT(varchar(10) , DoTime, 120 ) = CONVERT(varchar(10) , GETDATE(), 120 ) "+
				"GROUP BY DeviceName";
		return sql;
	}


	/**
	 * 各站点（U1~Z3）实际数量,标准数量sql
	 * @param xb
	 * @param siteName
	 * @return
	 */
	public static String sql_site_real_standard_detail(String xb,String siteName){
		String sql = "SELECT  D.DeviceName ,\r\n" +
				"        COUNT(1) AS sl ,\r\n" +
				"        SUBSTRING(rq, 12, 5) AS time,\r\n" +
				"					 CONVERT(INT,SumPerHour) AS SumPerHour \r\n" +
				"FROM    ( SELECT    GroupID ,\r\n" +
				"                    DeviceName ,\r\n" +
				"                    COUNT(1) AS sl ,\r\n" +
				"                    CONVERT(VARCHAR(14), A.DoTime, 120) + '00:00' AS rq,\r\n" +
				"					 SumPerHour \r\n" +
				"          FROM      [172.16.42.18].ADX.dbo.ADX_Detail_Do A\r\n" +
				"                    INNER JOIN [172.16.42.18].ADX.dbo.DeviceBasicData B ON A.DevID = B.SCMID\r\n" +
				"                    INNER JOIN [172.16.42.18].ADX.dbo.ADX_DocMain C ON B.GroupID = C.cjmc\r\n" +
				"          WHERE     CONVERT(VARCHAR(10), A.DoTime, 120) = CONVERT(VARCHAR(10), GETDATE(), 120)\r\n" +
				"                    AND B.GroupID = '"+xb+"'\r\n" +
				"                    AND C.zt = 'Y'\r\n" +
				"					AND DeviceName = '"+siteName+"'\r\n" +
				"          GROUP BY  GroupID ,\r\n" +
				"                    DeviceName ,\r\n" +
				"                    A.DoTime,SumPerHour\r\n" +
				"        ) D\r\n" +
				"GROUP BY DeviceName ,\r\n" +
				"        rq,SumPerHour";
		return sql;

	}

	/**
	 * 各站点（U1~Z3）目标数量sql
	 * @param xb
	 * @return
	 */
	public static String sql_site_standard_detail(String xb){
		String sql = "SELECT  CONVERT(INT,a.SumPerHour) AS SumPerHour ,\r\n" +
				"        SUBSTRING(time, 12, 5) AS time\r\n" +
				"FROM    ( SELECT    SumPerHour ,\r\n" +
				"                    CONVERT(VARCHAR(14), DoTime, 120) + '00:00' time\r\n" +
				"          FROM      [172.16.42.18].ADX.dbo.ADX_DocMain\r\n" +
				"          WHERE     CONVERT(VARCHAR(10), DoTime, 120) = CONVERT(VARCHAR(10), GETDATE(), 120)\r\n" +
				"AND cjmc = '"+xb+"'\r\n" +
				"AND zt = 'Y' \r\n" +
				"        ) a ";
		return sql;
	}

	/**
	 * 各站点（U1~Z3）异常数量sql
	 * @param xb
	 * @param siteName
	 * @return
	 */
	public static String sql_site_exception_detail(String xb,String siteName){
		String sql = "SELECT COUNT(1), SUBSTRING(TIME, 12, 5) AS TIME ,DeviceName \r\n" +
				"FROM (SELECT CONVERT(varchar(14) , DoTime, 120 )  + '00' AS TIME,DeviceName\r\n" +
				"FROM [172.16.42.18].ADX.dbo.AlarmLog A \r\n" +
				"INNER JOIN [172.16.42.18].ADX.dbo.DeviceBasicData B ON A.DeviceID = B.DeviceID \r\n" +
				"WHERE GroupID= '"+xb+"' AND DeviceName = '"+siteName+"'\r\n" +
				"AND CONVERT(varchar(10) , DoTime, 120 ) = CONVERT(varchar(10) , GETDATE(), 120 )  ) A  \r\n" +
				"GROUP BY TIME ,DeviceName";
		return sql;
	}

}
