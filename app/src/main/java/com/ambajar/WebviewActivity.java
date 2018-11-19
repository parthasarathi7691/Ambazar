package com.ambajar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebviewActivity extends AppCompatActivity {
    private WebView webview;
    private ProgressDialog dialog;
    private String url = "https://ambajar.com";
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview);

        webview = findViewById(R.id.webview);
        if (checkNetworkConnection()) {
           /* webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.getSettings().setBuiltInZoomControls(true);
            webview.getSettings().setDisplayZoomControls(true);*/
            dialog = new ProgressDialog(WebviewActivity.this);
            dialog.setMessage("Please wait Loading...");
            dialog.show();
            webview.setWebViewClient(new MyWebViewClient());
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setDomStorageEnabled(true);
            webview.loadUrl(url);
        } else {
            Intent intent = new Intent(WebviewActivity.this, NetworkCheckActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) WebviewActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            view.getSettings().setLoadWithOverviewMode(true);
            view.getSettings().setDomStorageEnabled(true);
            view.loadUrl(url);

            if (!dialog.isShowing()) {
                dialog.show();
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
            return;
        }else if (doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
