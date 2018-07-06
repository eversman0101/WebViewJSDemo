package com.jingyun.wisdom.webviewjsdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;


import com.jingyun.wisdom.Model.SharePreferenceUtil;
import com.jingyun.wisdom.service.TemperatureService;
import com.jingyun.wisdom.service.Web;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class MainActivity extends FragmentActivity {

    WebView tbsContent;
    private ProgressDialog dialog;
    private String url = Web.root;
    Alert alert;

    //private String url="file:///android_asset/js.html";
    //WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }
    private void initView() {

        alert=new Alert(MainActivity.this);
        tbsContent = (WebView)findViewById(R.id.webView);
        tbsContent.loadUrl(url);
        WebSettings webSettings = tbsContent.getSettings();
        webSettings.setJavaScriptEnabled(true);

        tbsContent.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                Uri uri=Uri.parse(url);
                if(uri.getScheme().equals("js"))
                {
                    if(uri.getAuthority().equals("alert"))
                    {
                        Log.e("Alert","js调用了android方法");
                        int arg=Integer.valueOf(uri.getQueryParameter("arg"));
                        alert.BeepAndShake(arg);
                    }
                    else if(uri.getAuthority().equals("routing"))
                    {
                        //document.location = "js://routing?arg=-1";
                        int arg=Integer.valueOf(uri.getQueryParameter("arg"));
                        SharePreferenceUtil.setRoutingType(getApplicationContext(),arg);
                    }
                }
                return true;
            }
        });
        tbsContent.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //newProgress   1-100之间的整数
                if (newProgress == 100) {
                    //页面加载完成，关闭ProgressDialog
                    closeDialog();
                } else {
                    //网页正在加载，打开ProgressDialog
                    openDialog(newProgress);
                }
            }

            private void openDialog(int newProgress) {
                if (dialog == null) {
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setTitle("正在加载");
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setProgress(newProgress);
                    dialog.setCancelable(true);
                    dialog.show();
                } else {
                    dialog.setProgress(newProgress);
                }
            }

            private void closeDialog() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
    }

    private void initData()
    {
        //初始化温度轮训服务
        Intent intent=new Intent(this,TemperatureService.class);
        startService(intent);
    }
    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (tbsContent.canGoBack()) {
                tbsContent.goBack();   //返回上一页面
                return true;
            } else {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                new MyDialogFragment().show(ft,"df");
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent=new Intent(this,TemperatureService.class);
        stopService(intent);
    }
    /*
    private void initView1()
    {
        webView=(WebView) findViewById(R.id.webView);
        webView.loadUrl("http://218.201.129.20:8104/Basic");
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings=webView.getSettings();

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);

        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        webView.addJavascriptInterface(new Alert(MainActivity.this), "alert");//AndroidtoJS类对象映射到js的test对象
        webView.setInitialScale(100);
        // 加载JS代码
        // 格式规定为:file:///android_asset/文件名.html
        //http://172.20.10.5:8080/TemperaturePages/

    }
    */
}
