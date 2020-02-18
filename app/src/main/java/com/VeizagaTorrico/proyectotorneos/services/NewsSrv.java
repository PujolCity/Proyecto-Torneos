package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.News;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NewsSrv {

    @GET(Constants.BASE_URL + "news/competitions")
    Call<List<News>> getNews(@Query("idUsuario") int idUsuario);

    @POST(Constants.BASE_URL + "news/publish")
    Call<MsgRequest> publishNew(@Body Map<String,String> dataNew);

}
