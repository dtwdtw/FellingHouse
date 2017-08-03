package com.dtw.fellinghouse.Model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.dtw.fellinghouse.Config;
import com.dtw.fellinghouse.R;
import com.dtw.fellinghouse.Utils.UriUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class WXShareModel {
    private static WXShareModel WXShareModel;
    private Context context;
    private IWXAPI api;
    private WXShareModel(Context context){
        this.context=context;
        String weixinID =null;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            weixinID=info.metaData.getString("WEIXIN_ID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        api=WXAPIFactory.createWXAPI(context,weixinID,true);
        api.registerApp(weixinID);
    }
    public static WXShareModel getInstance(Context context){
        if(WXShareModel ==null){
            WXShareModel =new WXShareModel(context);
        }
        return WXShareModel;
    }
    public void sendTextMessage(String scene,String textMsg,String description){
        WXTextObject textObject=new WXTextObject();
        textObject.text=textMsg;

        WXMediaMessage msg=new WXMediaMessage();
        msg.mediaObject=textObject;
        msg.description=description;

        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction="text"+System.currentTimeMillis();
        req.message=msg;
        switch(scene){
            case Config.WXSceneSession:
                req.scene=SendMessageToWX.Req.WXSceneSession;
                break;
            case Config.WXSceneTimeline:
                req.scene=SendMessageToWX.Req.WXSceneTimeline;
        }
        api.sendReq(req);
    }
    public void sendImgMessage(String scene, Uri uri){
        try {
            Bitmap bitmap= UriUtil.getBitmapFormUri(context,uri);
            WXImageObject imageObject=new WXImageObject(bitmap);
            WXMediaMessage msg=new WXMediaMessage();
            msg.mediaObject=imageObject;
//            msg.description="love";

            SendMessageToWX.Req req=new SendMessageToWX.Req();
            req.transaction="img"+System.currentTimeMillis();
            req.message=msg;
            switch(scene){
                case Config.WXSceneSession:
                    req.scene=SendMessageToWX.Req.WXSceneSession;
                    break;
                case Config.WXSceneTimeline:
                    req.scene=SendMessageToWX.Req.WXSceneTimeline;
            }
            api.sendReq(req);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendWebPageMessage(String scene,String url){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "资克";
        msg.description = "简单，时尚的短租服务！欢迎入住";

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,baos);
        msg.thumbData = baos.toByteArray();
        bmp.recycle();

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage"+System.currentTimeMillis();
        req.message = msg;
        switch(scene){
            case Config.WXSceneSession:
                req.scene=SendMessageToWX.Req.WXSceneSession;
                break;
            case Config.WXSceneTimeline:
                req.scene=SendMessageToWX.Req.WXSceneTimeline;
        }
        api.sendReq(req);
    }
}
