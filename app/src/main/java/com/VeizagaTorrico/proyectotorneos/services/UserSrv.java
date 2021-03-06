package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.ConfigNotification;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.RespSrvUser;
import com.VeizagaTorrico.proyectotorneos.models.Classified;
import com.VeizagaTorrico.proyectotorneos.models.Success;
import com.VeizagaTorrico.proyectotorneos.models.User;
import com.VeizagaTorrico.proyectotorneos.offline.model.DataOffline;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface UserSrv {


    @GET(Constants.BASE_URL + "users")
    Call<List<User>> getUsers();

    @GET(Constants.BASE_URL + "usercomp/petitioners")
    Call<List<User>> getPetitionersByCompetition(@Query ("idCompetencia") int idCompetencia);

    @POST(Constants.BASE_URL + "usercomp-del")
    Call<Success> refusePetitionerUser(@Body Map<String,String> userComp);

    @POST(Constants.BASE_URL + "add-participate")
    Call<Success> acceptPetitionUser(@Body Map<String,String> userComp);

    @POST( Constants.BASE_URL + "user" )
    Call<RespSrvUser>register(@Body Map<String,String> user);

    @POST( Constants.BASE_URL + "user/singin" )
    Call<RespSrvUser>initAccount(@Body Map<String,String> userAccount);

//    @POST( Constants.BASE_URL + "user/recovery" )
//    Call<RespSrvUser>resetPass(@Body Map<String,String> user);
    @POST( Constants.BASE_URL + "user/recovery" )
    Call<MsgRequest>resetPass(@Body Map<String,String> user);

    @PUT(Constants.BASE_URL + "user" )
    Call<RespSrvUser>updateData(@Body Map<String,String> dataNewUser);

    @PUT(Constants.BASE_URL + "user/uppass" )
    Call<MsgRequest>changePass(@Body Map<String,String> dataUser);

    @GET(Constants.BASE_URL + "competitors-competition")
    Call<List<User>> getCompetidoresByCompetencia(@Query ("idCompetencia") int idCompetencia);

    @GET(Constants.BASE_URL + "users/getUsersByUsername")
    Call<List<User>> getUsuariosByUsername(@Query ("username") String username);

    @GET(Constants.BASE_URL + "competition/classified")
    Call<List<Classified>> getClasificados(@QueryMap Map<String,Integer> datos);

    @POST(Constants.BASE_URL + "user-notif")
    Call<MsgRequest> updateConfigNotification(@Body Map<String, String> dataConfig);

    @GET(Constants.BASE_URL + "user-notif/config")
    Call<ConfigNotification> getConfigNotification(@Query ("idUsuario") int idUsuario);

    @POST(Constants.BASE_URL + "user/off")
    Call<DataOffline> getDataOffline(@Body Map<String,String> userComp);
}
