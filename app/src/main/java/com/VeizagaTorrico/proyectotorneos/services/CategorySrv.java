package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CategorySrv {

    @GET(Constants.BASE_URL + "categories")
    Call<List<Category>> getCategorias();

    @GET(Constants.BASE_URL + "categoriesBySport")
    Call<List<Category>> getCategoriasDeporte(@Query("idDeporte") int idDeporte);


}
