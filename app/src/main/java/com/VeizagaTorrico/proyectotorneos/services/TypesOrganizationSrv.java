package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.TypesOrganization;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TypesOrganizationSrv {

    @GET(Constants.BASE_URL + "typesorg")
    Call<List<TypesOrganization>> getTypesOrganization();

}
