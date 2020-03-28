package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.Context;

import com.VeizagaTorrico.proyectotorneos.offline.model.CompetitionOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.CompetitorOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.ConfrontationOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.FieldOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.InscriptionOff;
import com.VeizagaTorrico.proyectotorneos.offline.model.JudgeOff;

import java.util.List;

public class AdminDataOff {

    //private DbHelper adminDB;
    private ManagerCompetitionOff adminCompetencia;
    private ManagerCompetitorOff adminCompetidor;
    private ManagerFieldOff adminCampos;
    private ManagerInscriptionOff adminInscripcion;
    private ManagerJudgeOff adminJuez;
    private ManagerConfrontationOff adminEncuentro;

    public AdminDataOff() {
    }

    public void loadCompetition(Context context, CompetitionOff competencia, String[] fases){
        adminCompetencia = new ManagerCompetitionOff(context);

        adminCompetencia.addRowCompetitionFromObject(competencia, fases);
    }

    public void loadCompetitors(Context context, List<CompetitorOff> competidores){
        adminCompetidor = new ManagerCompetitorOff(context);

        // insertamos los competiodres en la app
        for (CompetitorOff competidor : competidores) {
            adminCompetidor.addRowCompetitorFromObject(competidor);
        }
    }

    public void loadFields(Context context, List<FieldOff> campos){
        adminCampos = new ManagerFieldOff(context);

        // insertamos los competiodres en la app
        for (FieldOff campo : campos) {
            adminCampos.addRowFieldFromObject(campo);
        }
    }

    public void loadJudges(Context context, List<JudgeOff> jueces){
        adminJuez = new ManagerJudgeOff(context);

        // insertamos los competiodres en la app
        for (JudgeOff juez : jueces) {
            adminJuez.addRowJudgeFromObject(juez);
        }
    }

    public void loadInscription(Context context, InscriptionOff inscripcion){
        adminInscripcion = new ManagerInscriptionOff(context);
        // insertamos los competiodres en la app
        adminInscripcion.addRowInscriptionFromObject(inscripcion);
    }

    public void loadConfrontations(Context context, List<ConfrontationOff> encuentros){
        adminEncuentro = new ManagerConfrontationOff(context);

        // insertamos los competiodres en la app
        for (ConfrontationOff encuentro : encuentros) {
            adminEncuentro.addRowConfrontationFromObject(encuentro);
        }
    }
}
