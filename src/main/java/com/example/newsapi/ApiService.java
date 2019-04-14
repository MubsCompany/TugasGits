package com.example.newsapi;

import com.example.newsapi.api.ResponseApi;

import retrofit2.Call;
import retrofit2.http.GET;

interface ApiService {

    @GET("everything?q=national-geographic&apiKey=0af9e39d1c0f41fb9709e8de8af33ff3")
    Call<ResponseApi> getDataNational();

    @GET("everything?q=techcrunch&apiKey=0af9e39d1c0f41fb9709e8de8af33ff3")
    Call<ResponseApi> getDataTech();

    @GET("everything?q=mtv-news&apiKey=0af9e39d1c0f41fb9709e8de8af33ff3")
    Call<ResponseApi> getDataM();

    @GET("everything?q=bitcoin&apiKey=0af9e39d1c0f41fb9709e8de8af33ff3")
    Call<ResponseApi> getDataGoogleNews();
}
