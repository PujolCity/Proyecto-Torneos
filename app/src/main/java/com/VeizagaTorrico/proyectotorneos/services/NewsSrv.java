package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsSrv {


    @GET(Constants.BASE_URL + "news")
    Call<List<News>> getNews(@Query("idUsuario") int idUsuario);

}
