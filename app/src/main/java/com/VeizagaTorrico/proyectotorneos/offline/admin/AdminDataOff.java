package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.Context;
import android.util.Log;

import com.VeizagaTorrico.proyectotorneos.models.Field;
import com.VeizagaTorrico.proyectotorneos.models.Referee;
import com.VeizagaTorrico.proyectotorneos.offline.model.CompetitionOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.CompetitorOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.ConfrontationOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.FieldOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.InscriptionOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.JudgeOff;

import java.util.List;

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

    public void loadCompetition(CompetitionOff competencia, String[] fases){
        adminCompetencia = new ManagerCompetitionOff(context);

        // vemos si existen datos anteriores de la competencia, si los hay los borramos
        // los campos al ser globales no los borramos solo los actualizamos(a la hora de insertar)
        if(adminCompetencia.existCompetition(competencia.getId())){
            adminInscripcion = new ManagerInscriptionOff(context);
//            adminJuez = new ManagerJudgeOff(context);
            adminCompetidor = new ManagerCompetitorOff(context);

            adminInscripcion.deleteByCompetition(competencia.getId());
            // los jueces son globales tmpoco se deberian borrar
//            adminJuez.deleteByCompetition(competencia.getId());
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
}
