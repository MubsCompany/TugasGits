package com.example.newsapi;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class Detail extends AppCompatActivity {
    TextView tvDetailTitle, tvDetailDesc;
    ImageView imgDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detail );

        tvDetailDesc = findViewById( R.id.tv_detail_desc );
        tvDetailTitle = findViewById( R.id.tv_detail_title );

        final int position = getIntent().getIntExtra( "position",1 );
        tvDetailTitle.setText( getIntent().getStringExtra( "title" ));
        tvDetailDesc.setText( getIntent().getStringExtra( "desc" ) );

        // titlenya ngeget dari arrayCompany
        setTitle( getIntent().getStringExtra( "name" ) );

//        untuk web
        class MyWebViewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (Uri.parse( url ).getHost().equals( getIntent().getStringExtra( "url" ) )){
                    view.setTranslationX( android.transition.Fade.MODE_IN );
                    return false;
                }
                else
                    return super.shouldOverrideUrlLoading( view, url );
            }
        }

        android.webkit.WebView webView = findViewById( R.id.web_view );
        webView.getSettings().setJavaScriptEnabled( true );
        webView.loadUrl( getIntent().getStringExtra( "url" ) );
        webView.setWebViewClient( new MyWebViewClient() );



    }
}
