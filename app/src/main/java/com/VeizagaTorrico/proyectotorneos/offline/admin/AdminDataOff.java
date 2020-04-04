package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.Context;
import android.util.Log;

import com.VeizagaTorrico.proyectotorneos.models.Confrontation;
import com.VeizagaTorrico.proyectotorneos.models.Field;
import com.VeizagaTorrico.proyectotorneos.models.Inscription;
import com.VeizagaTorrico.proyectotorneos.models.Referee;
import com.VeizagaTorrico.proyectotorneos.models.User;
import com.VeizagaTorrico.proyectotorneos.offline.model.CompetitionOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.CompetitorOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.ConfrontationOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.FieldOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.InscriptionOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.JudgeOff;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminDataOff {

    private Context context;
    private ManagerCompetitionOff adminCompetencia;
    private ManagerCompetitorOff adminCompetidor;
    private ManagerFieldOff adminCampos;
    private ManagerInscriptionOff adminInscripcion;
    private ManagerJudgeOff adminJuez;
    private ManagerConfrontationOff adminEncuentro;

    public AdminDataOff(Context context) {
        this.context = context;
    }

    // ##########################################################################################
    // ################################## CARGA DATOS DB ########################################

    public void loadCompetition(CompetitionOff competencia, String[] fases){
        adminCompetencia = new ManagerCompetitionOff(context);

        // vemos si existen datos anteriores de la competencia, si los hay los borramos
        // los campos al ser globales no los borramos solo los actualizamos(a la hora de insertar)
        if(adminCompetencia.existCompetition(competencia.getId())){
            adminInscripcion = new ManagerInscriptionOff(context);
            adminCompetidor = new ManagerCompetitorOff(context);

            adminInscripcion.deleteByCompetition(competencia.getId());
            // los jueces son globales no se deben borrar
            adminCompetidor.deleteByCompetition(competencia.getId());
            adminCompetencia.deleteCompetition(competencia.getId());
        }

        adminCompetencia.addRowCompetitionFromObject(competencia, fases);
    }

    public void loadCompetitors(List<CompetitorOff> competidores){
        adminCompetidor = new ManagerCompetitorOff(context);

        // insertamos los competiodres en la app
        for (CompetitorOff competidor : competidores) {
            adminCompetidor.addRowCompetitorFromObject(competidor);
        }
    }

    public void loadFields(List<FieldOff> campos){
        adminCampos = new ManagerFieldOff(context);

        // insertamos los competiodres en la app
        for (FieldOff campo : campos) {
            adminCampos.addRowFieldFromObject(campo);
        }
    }

    public void loadJudges(List<JudgeOff> jueces){
        adminJuez = new ManagerJudgeOff(context);

        // insertamos los competiodres en la app
        for (JudgeOff juez : jueces) {
            adminJuez.addRowJudgeFromObject(juez);
        }
    }

    public void loadInscription(InscriptionOff inscripcion){
        adminInscripcion = new ManagerInscriptionOff(context);
        // insertamos los competiodres en la app
        adminInscripcion.addRowInscriptionFromObject(inscripcion);
    }

    public void loadConfrontations(List<ConfrontationOff> encuentros){

        if(encuentros.size() == 0){
            Log.d("ENC_LOCAL_DB", "Competencias sin encuentros");
            return;
        }
        adminEncuentro = new ManagerConfrontationOff(context);

        // recuperamos el id de la competencia
        int idCompetencia = encuentros.get(0).getCompetencia();

        // si existian encuentros, los borrramos
        if(adminEncuentro.existByCompetition(idCompetencia)){
            adminEncuentro.deleteByCompetition(idCompetencia);
        }
        // insertamos los encuentros en la db local
        for (ConfrontationOff encuentro : encuentros) {
            adminEncuentro.addRowConfrontationFromObject(encuentro);
        }
    }

    // ##########################################################################################
    // ##########################################################################################

    // ##########################################################################################
    // ################################## DATOS ENCUENTROS ######################################

    // devuelve el juez de un encuentro
    public Referee getRefereeConfrontation(int idEncuentro){
        adminEncuentro = new ManagerConfrontationOff(context);

        return adminEncuentro.getRefereeConfrontation(idEncuentro);
    }

    // devuelve el campo de un encuentro
    public Field getFieldConfrontation(int idEncuentro){
        adminCampos = new ManagerFieldOff(context);

        return adminCampos.getFieldConfrontation(idEncuentro);
    }

    // devuelve el campo de un encuentro
    public String turnConfrontation(int idConfrontation){
        adminEncuentro = new ManagerConfrontationOff(context);

        return adminEncuentro.turnConfrontation(idConfrontation);
    }

    // ##########################################################################################
    // ##########################################################################################

    // ##########################################################################################
    // ################################# DATOS COMPETENCIA ######################################

    // devuelve los encuentros de una competencia por fase, jornada y/o grupo
    public List<Confrontation> getConfrontationByCompetition(int idCompetencia, String tipoOrganizacion, Map<String, String> fechaGrupo){
        adminEncuentro = new ManagerConfrontationOff(context);

        return adminEncuentro.confrontationsByCompetition(idCompetencia, tipoOrganizacion, fechaGrupo);
    }

    // determina si la competencia cuenta con encuentros
    public boolean competitionWtihConfrontation(int idCompetencia){
        adminEncuentro = new ManagerConfrontationOff(context);

        return adminEncuentro.existByCompetition(idCompetencia);
    }

    public List<User> competitorsByCompetition(int idCompetencia){
        adminCompetidor = new ManagerCompetitorOff(context);

        return adminCompetidor.getCompetitorsByCompetition(idCompetencia);
    }

    // devuelve el competidor libre de una jornada, si es que lo hay, sino null
    public String competitorFreeByJornada(int idCompetencia, String typeOrg, Map<String,String> fecha_grupo){
        adminEncuentro = new ManagerConfrontationOff(context);
        adminCompetidor = new ManagerCompetitorOff(context);

        List<Confrontation> encuentros =  adminEncuentro.confrontationsByCompetition(idCompetencia,typeOrg,fecha_grupo);
        List<String> aliasCompEncuentros = new ArrayList<>();
        for (int i=0; i < encuentros.size(); i++){
            aliasCompEncuentros.add(encuentros.get(i).getCompetidor1());
            aliasCompEncuentros.add(encuentros.get(i).getCompetidor2());
        }
        //Log.d("FREE_COMP_ENC", "Cant de alias de los encuentros: "+aliasCompEncuentros.size());

        List<String> aliasCompetidores = new ArrayList<>();

        if(typeOrg.contains("Eliminatoria")){
            return null;
        }
        if(typeOrg.contains("Liga")){
            aliasCompetidores.addAll(adminCompetidor.getAliasCompetitorsByCompetition(idCompetencia));
        }
        if(typeOrg.contains("grupo")){
            aliasCompetidores.addAll(adminCompetidor.getAliasCompetitorsByGroupCompetition(idCompetencia, fecha_grupo.get("grupo")));
        }
        //Log.d("FREE_COMP_ALIAS_GR", "Cant competitores de la competencia: "+aliasCompetidores.size());
        aliasCompetidores.removeAll(aliasCompEncuentros);

        String competidorLibre = null;
        if(aliasCompetidores.size() > 0){
            competidorLibre = aliasCompetidores.get(0);
        }
        //Log.d("FREE_COMP_DIFF", "Cant competitores libres: "+aliasCompetidores.size());

        return competidorLibre;
    }

    // devuelve la cantidad de grupos que tiene una competencia
    public int groupsByCompetition(int idCompetencia){
        adminCompetencia = new ManagerCompetitionOff(context);

        return adminCompetencia.cantGroupByCompetition(idCompetencia);
    }

    // devuelve la cantidad de jornadas que tiene una competencia
    public int jornadaByCompetition(int idCompetencia){
        adminCompetencia = new ManagerCompetitionOff(context);

        return adminCompetencia.cantJornadaByCompetition(idCompetencia);
    }

    // devuelve un array con las fases con las que cuenta una competencia
    public String[] phasesByCompetition(int idCompetencia){
        adminCompetencia = new ManagerCompetitionOff(context);

        return adminCompetencia.phasesByCompetition(idCompetencia);
    }

    // devuelve la cantidad de jornadas que tiene una competencia
    public Inscription inscriptionByCompetition(int idCompetencia){
        adminInscripcion = new ManagerInscriptionOff(context);

        return adminInscripcion.inscriptionByCompetition(idCompetencia);
    }
}
