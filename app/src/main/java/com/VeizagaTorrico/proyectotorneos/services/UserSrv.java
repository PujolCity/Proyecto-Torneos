package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.ConstantURL;
import com.VeizagaTorrico.proyectotorneos.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserSrv {


    @GET(ConstantURL.BASE_URL + "users")
    Call<List<User>> getUsers();


}
