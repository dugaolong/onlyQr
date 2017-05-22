package com.dgl.www.onlyqr;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * 系统组件
 */
public class MyApplication extends Application {
    private static Context appContext;
    public static MyApplication instance;
    private Bitmap screenShot;
    String TAG = "MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        instance = this;

//        AdSdk.setDebugOn(); // 打开调试，输出调试信息
//        AdSdk.setMockOn();  // 调试时打开，正式发布时关闭
        //配置小米广告的sdk
//        AdSdk.initialize(this, "2882303761517555679");
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    //截屏
    public void setScreenShot(Bitmap screenShot) {
        this.screenShot = screenShot;
    }

    public Bitmap getScreenShot() {
        return screenShot;
    }





}
