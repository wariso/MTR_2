package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class VistaGen extends AppCompatActivity {
    WebView wv;
    String url = "https://www.google.com/maps/d/u/0/embed?mid=1pjCNv0fuLOHOiEUx5RAaN_Ti18HAFS83";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_gen);

        wv= (WebView) findViewById(R.id.webView);
        wv.setWebViewClient(new WebViewClient());
        //comment
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(url);
    }
}