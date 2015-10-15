package com.whl.hp.work_house.zufang;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.whl.hp.work_house.R;
import com.whl.hp.work_house.tool.Config;

/**
 * Created by hp-whl on 2015/10/5.
 */
public class ZuFangActivity extends Activity{
    WebView webview;
    ImageView waitImageView;
    AnimationDrawable animation;
    String url;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zufang_layout);
        webview= (WebView) findViewById(R.id.webView);
        waitImageView= (ImageView) findViewById(R.id.waitImageViewID);
        url= Config.ZUFANG_URL;
        animation= (AnimationDrawable) waitImageView.getBackground();
        animation.start();


        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webview.loadUrl(url);
        //设置Web视图
        webview.setWebViewClient(new webViewClient ());

    }

    //    //Web视图
    class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            animation.stop();
            waitImageView.setVisibility(View.GONE);
        }
    }


    public void cancel(View view){
        finish();
    }
}
