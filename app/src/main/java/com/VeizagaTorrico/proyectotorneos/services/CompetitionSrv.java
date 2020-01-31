package com.VeizagaTorrico.proyectotorneos.services;

import com.VeizagaTorrico.proyectotorneos.Constants;
import com.VeizagaTorrico.proyectotorneos.models.Competition;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionMin;
import com.VeizagaTorrico.proyectotorneos.models.CompetitionOrg;
import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.MsgRequest;
import com.VeizagaTorrico.proyectotorneos.models.Phase;
import com.VeizagaTorrico.proyectotorneos.models.Success;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface CompetitionSrv {

    @FormUrlEncoded
    @POST( Constants.BASE_URL + "existcompetition" )
    Call<Success>comprobar(@Field("competencia") String competencia);

    @POST( Constants.BASE_URL + "competition" )
    Call<Competition>createCompetition(@Body Map<String,String> competencia);

    @PUT( Constants.BASE_URL + "competition" )
    Call<MsgRequest>updateCompetition(@Body Map<String,String> dataCompetition);

    @GET(Constants.BASE_URL + "competitions")
    Call<List<Competition>> getCompetitions();

    @GET(Constants.BASE_URL + "competition-participates")
    Call<List<CompetitionMin>> getCompetitionsParticipates(@Query("idUsuario") int idUsuario);

    @GET(Constants.BASE_URL + "competition-follow")
    Call<List<CompetitionMin>> getCompetitionsFollow(@Query("idUsuario") int idUsuario);

    @GET(Constants.BASE_URL + "competition-organize")
    Call<List<CompetitionMin>> getCompetitionsOrganize(@Query("idUsuario") int idUsuario);

    @GET(Constants.BASE_URL + "findCompetitionsByName/{nameCompetition}")
    Call<List<Competition>> findCompetitionsByName(@Path("nameCompetition") String nameCompetition);

    @GET(Constants.BASE_URL + "competitions-roles")
    Call<List<CompetitionMin>> findCompetitionsByFilters(@QueryMap Map<String,String> filters);

    @PUT(Constants.BASE_URL + "usercomp-rolfollow")
    Call<Success> followCompetition(@Body Map<String,Integer> seguir);

    @PUT(Constants.BASE_URL + "usercomp-delfollow")
    Call<Success> noFollowCompetition(@Body Map<String,Integer> noSeguir);

    @POST(Constants.BASE_URL + "competition/org")
    Call<CompetitionOrg> getFaseGrupoCompetition(@Query("idCompetencia") int idCompetencia);

    @POST( Constants.BASE_URL + "usercomp" )
    Call<MsgRequest> solicitudCompetition(@Body Map<String,String> solicitud);

    @GET(Constants.BASE_URL + "generator/matches")
    Call<MsgRequest> generarEncuentros(@Query("idCompetencia") int idCompetencia);

    @POST( Constants.BASE_URL + "competition/new-fase" )
    Call<MsgRequest> generarSiguienteFase(@Body Map<String,Object> generar);

    @GET(Constants.BASE_URL + "competition/phases")
    Call<Phase> getFase(@Query("idCompetencia") int idCompetencia);

}
