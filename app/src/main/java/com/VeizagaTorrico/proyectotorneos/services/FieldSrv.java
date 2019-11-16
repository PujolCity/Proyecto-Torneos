package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.Field;
import com.VeizagaTorrico.proyectotorneos.models.Success;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface FieldSrv {
    @GET(ConstantURL.BASE_URL + "grounds/getFieldsByGrounds")
    Call<List<Field>> getFieldsByGround(@Query("idPredio") int idPredio);

    @POST( ConstantURL.BASE_URL + "grounds/field" )
    Call<Success>createField(@Body Map<String,String> campo);

    @DELETE( ConstantURL.BASE_URL + "grounds/fields/del" )
    Call<Success>deleteField(@QueryMap  Map<String,Integer> datos);

}
