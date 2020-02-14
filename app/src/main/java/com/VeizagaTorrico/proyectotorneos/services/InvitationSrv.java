package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.Invitation;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface InvitationSrv {

    @GET(Constants.BASE_URL + "invitations")
    Call<List<Invitation>> getInvitaciones(@Query("idUsuario") int idUsuario);

    @POST(Constants.BASE_URL + "invitation-coorg")
    Call<MsgRequest> invitarUsuario(@Body Map<String,String> body);

    @PUT(Constants.BASE_URL + "invitations/upstatus")
    Call<MsgRequest> aceptarInvitacion(@Query("idInvitacion") int idInvitacion);

    @PUT(Constants.BASE_URL + "invitations/dwnstatus")
    Call<MsgRequest> rechazarInvitacion(@Query("idInvitacion") int idInvitacion);

}
