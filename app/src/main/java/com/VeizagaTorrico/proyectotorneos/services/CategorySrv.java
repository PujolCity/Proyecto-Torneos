package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategorySrv {

    @GET(ConstantURL.BASE_URL + "categories")
    Call<List<Category>> getCategorias();
}
