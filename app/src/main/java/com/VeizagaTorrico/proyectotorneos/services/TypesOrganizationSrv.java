package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.TypesOrganization;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TypesOrganizationSrv {

    @GET(ConstantURL.BASE_URL + "typesorg")
    Call<List<TypesOrganization>> getTypesOrganization();

}
