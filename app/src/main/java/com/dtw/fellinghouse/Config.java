package com.dtw.fellinghouse;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class Config {
    public static final String Share_Link ="http://dtwdtw.tk";

    //<editor-fold desc="腾讯位置处理">
    //腾讯api文档   http://lbs.qq.com/webservice_v1/guide-convert.html
    public static final String LocationTranslateQQAPI="http://apis.map.qq.com/ws/coord/v1/translate?type=1&key=2NMBZ-BQIW3-M5C3V-3DWQI-PR3EJ-5SFHN&locations=";
    public static final String LocationDescripeQQAPI="http://apis.map.qq.com/ws/geocoder/v1/?key=2NMBZ-BQIW3-M5C3V-3DWQI-PR3EJ-5SFHN&location=";
    //</editor-fold>

    public static final String MasterName="13011300265";

    public static final String Name_SimpleProductJson="main.json";

    public static final String MobChina="86";
    public static final int Code_Success=0;
    public static final int Code_Fail=-1;
    public static final int Code_CodeSend=1110;
    public static final int Code_CodeVerify=1111;

    public static final int Code_JMRegist=20;
    public static final int Code_JMLogin=21;

    public static final int Request_Code_Regist =100;
    public static final int Request_Code_SmsCodeLogin =101;
    public static final int Request_Code_Request_Image=103;
    public static final int Request_Code_Login=104;
    public static final int Request_Code_Permission_Location=105;

    public static final String Key_Admin="admin";
    public static final String Key_Is_Admin="isadmin";
    public static final String Key_PhnoeNum="phoneNum";
    public static final String Key_Password="password";
    public static final String Key_SP_KeyBoardHeight ="keyboardheight";
    public static final String Key_SP_UserName="username";
    public static final String Key_SP_UserPassworde="password";
    public static final String Key_SP_Data_Update_Time="updatetime";
    public static final String Key_Main_Product="mainproduct";
    public static final String Key_Product="product";
    public static final String Key_SP_LastChartUserName="lastChartUserName";

    public static final int NotifyDisEnable =30;
    public static final int NotifySilence=31;
    public static final int NotifyDefault=32;

    public static final String WXSceneSession="session";
    public static final String WXSceneTimeline="timeline";
    public static final String WXSceneFavorite="favorite";

    public static final String TYPE_DAY="day";
    public static final String TYPE_WEEK="week";
    public static final String TYPE_MONTH="month";
}
