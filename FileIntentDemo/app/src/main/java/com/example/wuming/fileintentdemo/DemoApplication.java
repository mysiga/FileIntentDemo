package com.example.wuming.fileintentdemo;

import android.app.Application;
import android.os.StrictMode;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by wuming on 16/6/16.
 */

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initStrictMode();
        /**
         * 仅仅是缓存Application的Context，不耗时,在下载进程启动的时候，初始化OkHttpClient
         */
        FileDownloader.init(getApplicationContext(), new FileDownloadHelper.OkHttpClientCustomMaker() { // is not has to provide.
            @Override
            public OkHttpClient customMake() {
                //download OkHttpClient
                final OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS);
                return builder.build();
            }
        });
    }

    /**
     * 开启严格模式
     */
    private void initStrictMode() {
        StrictMode.ThreadPolicy.Builder builderThread = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().penaltyLog();
        StrictMode.setThreadPolicy(builderThread.build());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().penaltyLog();
        StrictMode.setVmPolicy(builder.build());
    }
}
