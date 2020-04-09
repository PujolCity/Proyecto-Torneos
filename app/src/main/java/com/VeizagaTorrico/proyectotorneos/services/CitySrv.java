package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.City;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CitySrv {

    @GET(Constants.BASE_URL + "cities")
    Call<List<City>> getCiudades();

    @GET(Constants.BASE_URL + "findCitiesByName")
    Call<List<City>> buscarCiudad(@Query("nombreCiudad") String nombreCiudad );
}
