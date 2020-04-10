package com.VeizagaTorrico.proyectotorneos;

public class Constants {
    // URL BASE del servidor

    public static final String BASE_URL = "http://132.255.7.152:20203/api/";
    // public static final String BASE_URL = "http://192.168.1.47:8000/api/";

    // nombre del archivo donde se guardan los datos del token de firebase de manera local
    public static final String FILE_SHARED_TOKEN_FIREBASE = "TokenFirebase";
    // key del token guardado localmente
    public static final String KEY_TOKEN = "token";

    // nombre del archivo donde se guardan los datos del usuario de manera local
    public static final String FILE_SHARED_DATA_USER = "DataUser";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_SESSION = "session";

    //public static final String KEY_CONFRONTATION = "confrontation";

    // nombre del archivo donde se guardan los encuentros actualizados de manera local, para dsp
    // reflejarlos en el servidor
    public static final String FILE_SHARED_CONFRONTATION_OFF = "EncuentrosOffline";
    public static final String KEY_COUNT = "counter";

    // posibles fases de las competencias
    public static final String FASE_GRUPOS = "0";

    public static final String TIPO_ELIMINATORIAS = "Eliminatorias";
    public static final String TIPO_ELIMINATORIAS_DOBLE = "Eliminatorias Double";
    public static final String TIPO_LIGA = "Liga Single";
    public static final String TIPO_LIGA_DOBLE = "Liga Double";
    public static final String TIPO_GRUPOS = "Fase de grupo";

    // valores para pasar entre activities y fragments
    public static final String EXTRA_KEY_ID_COMPETENCIA = "COMPETENCIA_ID";
    public static final String EXTRA_KEY_VIEW = "VIEW";

    // valores para reconocer que pantalla mostrar
    public static final String NOTIF_VIEW_SOLICITUD = "MIS_SOLICITUDES";
    public static final String NOTIF_VIEW_INVITACION = "MIS_INVITACIONES";
}