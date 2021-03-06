package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface NotificationSrv {

        @PUT(Constants.BASE_URL + "user-token")
        Call<MsgRequest> reportChangeTokenToServer(@Body Map<String,String> dataUpdateToken);

}
