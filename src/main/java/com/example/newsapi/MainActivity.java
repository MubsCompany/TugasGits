package com.example.newsapi;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.newsapi.api.ArticlesItem;
import com.example.newsapi.api.ResponseApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.newsapi.R.anim.slide_in_left;
import static com.example.newsapi.R.anim.slide_out_left;

public class MainActivity extends AppCompatActivity{
    RecyclerView rvNews;
    NewsAdapter newsAdapter;
    List<ArticlesItem> data;
    CustomTabsClient myCustomTabsClient;
    ProgressBar progressBar;

    FloatingActionButton floatingActionButton;
    boolean isDark = false;

    ConstraintLayout mainLayout;
    ConstraintLayout errorLayout;
    TextView tvErrorTitle, tvErrorMessage, tvDetailTitle, tvDetailDesc;
    ImageView imgError, imgDetail;
    Button btnError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

//        inisialisasi view
        initView();

//        progressBar
        progressBar.setOutlineSpotShadowColor( getResources().getColor( android.R.color.white ) );
        progressBar.setAnimation(  android.view.animation.AnimationUtils.loadAnimation( this,R.anim.slide_in_left ) );

//        actionBar
        getSupportActionBar().setTitle( "National Geographic" );

        newsAdapter = new NewsAdapter( this, data );

//        floatingActionButton
        floatingActionButton.setBackgroundTintList( ColorStateList.valueOf( getResources().getColor( R.color.colorPrimary ) ) );

        floatingActionButton.setOnClickListener( new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                isDark = !isDark;
                if (isDark) {
                    mainLayout.setBackgroundResource( R.color.electromagnetic );
                    floatingActionButton.setImageResource( R.drawable.sun_icon );
                    floatingActionButton.setBackgroundTintList( ColorStateList.valueOf( getResources().getColor( R.color.colorPrimary ) ) );

                } else {
                    mainLayout.setBackgroundResource( android.R.color.white );
                    floatingActionButton.setImageResource( R.drawable.moon_icon );
                }

                newsAdapter = new NewsAdapter( getApplicationContext(), data, isDark );
                rvNews.setAdapter( newsAdapter );


            }
        } );

//        customTab Service
        CustomTabsServiceConnection tabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                myCustomTabsClient = customTabsClient;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                myCustomTabsClient = null;
            }
        };
        String packageName = "com.android.chrome";
        CustomTabsClient.bindCustomTabsService( this, packageName, tabsServiceConnection );

        //get data
        getDataNationalGeographic();
    }

//    Inisialisasi View
    private void initView() {

        rvNews = findViewById( R.id.rv_news );
        floatingActionButton = findViewById( R.id.fab );
        mainLayout = findViewById( R.id.main_layout );
        imgError = findViewById( R.id.img_error );
        tvErrorMessage = findViewById( R.id.tv_error_message );
        tvErrorTitle = findViewById( R.id.tv_error_title );
        errorLayout = findViewById( R.id.error_layout );
        btnError = findViewById( R.id.btn_error );
        progressBar = findViewById( R.id.progress_bar );
    }

    public void customService(View view) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder( getSession() );

        builder.setToolbarColor( ContextCompat.getColor( this, R.color.browser_actions_title_color ) );

        builder.setStartAnimations( this, slide_in_left, slide_in_left );
        builder.setExitAnimations( this, slide_out_left, slide_out_left );

        CustomTabsIntent tabsIntent = builder.build();
        tabsIntent.launchUrl( this, Uri.parse( getIntent().getStringExtra( "url" ) ) );


    }

//    get data
    private void getDataNationalGeographic() {
        progressBar.setVisibility( View.VISIBLE );
        errorLayout.setVisibility( View.GONE );
        ConfigRetrofit.getInstance().getDataNational().enqueue( new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    data = response.body().getArticles();
                    newsAdapter = new NewsAdapter( MainActivity.this, data );

                    rvNews.setLayoutManager( new LinearLayoutManager( MainActivity.this ) );
                    rvNews.setAdapter( newsAdapter );
                    progressBar.setVisibility( View.INVISIBLE );
                } else {
                    progressBar.setVisibility( View.INVISIBLE );
                    String errorCode;
                    switch (response.code()) {
                        case 404 :
                            errorCode = "404 not found";
                            break;

                        case 500 :
                            errorCode = "500 server broken";
                            break;

                            default :
                                errorCode = "unknown error";
                                break;
                    }
                    showError( R.drawable.failure_icon,"No Result","Please Try Again\n"+ errorCode );
                }
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                progressBar.setVisibility( View.INVISIBLE );
                errorLayout.setVisibility( View.VISIBLE );
                showError( R.drawable.failure_icon,"Oh no...","Network error, Please Try Again\n"+ t.toString() );
            }
        } );

    }

//    getSession untuk customTab
    public CustomTabsSession getSession() {
        return myCustomTabsClient.newSession( new CustomTabsCallback() {
            @Override
            public void onNavigationEvent(int navigationEvent, Bundle extras) {
                super.onNavigationEvent( navigationEvent, extras );
            }
        } );


    }

//    tampilan error/failure
    private void showError(int imageView, String title, String message){
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        imgError.setImageResource( imageView );
        tvErrorTitle.setText( title );
        tvErrorMessage.setText( message );

        btnError.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataNationalGeographic();
            }
        } );
    }

}
