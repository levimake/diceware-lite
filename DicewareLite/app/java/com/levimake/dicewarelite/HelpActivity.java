package com.levimake.dicewarelite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    private TextView textView;
    private String html;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webView=(WebView) findViewById(R.id.help_webview);
        webView.loadUrl("file:///android_asset/help.html");

    }
}
