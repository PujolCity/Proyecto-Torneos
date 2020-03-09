package com.VeizagaTorrico.proyectotorneos.offline.admin;

import android.content.Context;

import com.VeizagaTorrico.proyectotorneos.offline.model.Competition;
import com.VeizagaTorrico.proyectotorneos.offline.model.Competitor;
import com.VeizagaTorrico.proyectotorneos.offline.model.Confrontation;
import com.VeizagaTorrico.proyectotorneos.offline.model.Field;
import com.VeizagaTorrico.proyectotorneos.offline.model.Inscription;
import com.VeizagaTorrico.proyectotorneos.offline.model.Judge;

import java.util.List;

public class AdminDataOff {

    //private DbHelper adminDB;
    private ManagerCompetition adminCompetencia;
    private ManagerCompetitor adminCompetidor;
    private ManagerField adminCampos;
    private ManagerInscription adminInscripcion;
    private ManagerJudge adminJuez;
    private ManagerConfrontation adminEncuentro;

    public AdminDataOff() {
    }

    public void loadCompetition(Context context, Competition competencia){
        adminCompetencia = new ManagerCompetition(context);

        adminCompetencia.addRowCompetitionFromObject(competencia);
    }

    public void loadCompetitors(Context context, List<Competitor> competidores){
        adminCompetidor = new ManagerCompetitor(context);

        // insertamos los competiodres en la app
        for (Competitor competidor : competidores) {
            adminCompetidor.addRowCompetitorFromObject(competidor);
        }
    }

    public void loadFields(Context context, List<Field> campos){
        adminCampos = new ManagerField(context);

        // insertamos los competiodres en la app
        for (Field campo : campos) {
            adminCampos.addRowFieldFromObject(campo);
        }
    }

    public void loadJudges(Context context, List<Judge> jueces){
        adminJuez = new ManagerJudge(context);

        // insertamos los competiodres en la app
        for (Judge juez : jueces) {
            adminJuez.addRowJudgeFromObject(juez);
        }
    }

    public void loadInscription(Context context, Inscription inscripcion){
        adminInscripcion = new ManagerInscription(context);
        // insertamos los competiodres en la app
        adminInscripcion.addRowInscriptionFromObject(inscripcion);
    }

    public void loadConfrontations(Context context, List<Confrontation> encuentros){
        adminEncuentro = new ManagerConfrontation(context);

        // insertamos los competiodres en la app
        for (Confrontation encuentro : encuentros) {
            adminEncuentro.addRowConfrontationFromObject(encuentro);
        }
    }
}
