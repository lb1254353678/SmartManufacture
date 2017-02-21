package com.goodbaby.push.global;

/**
 * Created by goodbaby on 17/2/7.
 */

public class Constant {

    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String REM_PSW = "rem_psw";
    public static final String AUTO_LOGIN = "auto_login";

    public static final String HOSTNAME = "http://10.179.161.10:8080/";
    public static final String HOSTNAME_STAFF = "http://10.179.160.72:8080/";

    public static final String URL_LOGIN = HOSTNAME + "GBPushServer/user/LoginServlet";
    public static final String URL_CONNUSERNAMEANDREGISTRACTIONID = "";
    public static final String URL_PUSHMESSAGE = HOSTNAME + "GBPushServer/message/PushMessageServlet";
    public static final String URL_UNDISPOSED = HOSTNAME + "GBPushServer/message/UnDisposedPushMsgServlet";
    public static final String URL_DISPOSED = HOSTNAME + "GBPushServer/message/DisposedPushMsgServlet";
    public static final String URL_SENDED = HOSTNAME + "GBPushServer/message/SendMsgServlet";

    public static final String URL_SENDTO = HOSTNAME + "GBPushServer/message/SendToMessageServlet";

    public static final int RESPONSE_FAILURE = -1;
    public static final int RESPONSE_SUCCESS = 1;
    public static final int REQUEST_REFRESH = 0;

}
