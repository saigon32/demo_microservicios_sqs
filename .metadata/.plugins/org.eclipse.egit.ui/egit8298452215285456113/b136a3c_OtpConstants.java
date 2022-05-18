package com.dislicores.api.otp.util;

public class OtpConstants {

	/**
	 * Método constructor
	 */
	private OtpConstants() {
		// Empty constructor
	}

	/* Datos para el envio de mensajes por colas */
	public static final String PORTAL = "PORTAL";
	public static final String OTP_API = "OTP";
	public static final String AMPQ_QUEUE_EXCHANGE = "familia.pomys.exchange";
	public static final String AMPQ_ROUTINGKEY_AUDIT = "familia.pomys.routingkey.audit";
	public static final String DEFAULT_IP_ADDRESS = "127.0.0.0";

	/* Constantes relacionadas a la base de datos */
	public static final String CONF_OTP_MAX_ATTEMPTS = "pomys.otp.max.intentos";
	public static final String CONF_OTP_VALIDITY_IN_HOURS = "pomys.otp.vigencia.horas";
	public static final String CONF_BASE_PATH_URL = "portal.url.pais";

	/* datos utilitaros */
	public static final String REGISTER_PATH = "#bienvenida";
	
	/* parametros de pantillas */
	public static final String MC_KEY_PARAM = "key";
	public static final String MC_URL_PARAM = "url";
	public static final String MC_NAME_PARAM = "name";
	public static final String MC_DATE_PARAM = "date";
	public static final String MC_TIME_PARAM = "time";

	/* Constantes para los codigos de acceso a mensajes */
	/* Codigos de mensajes de error de usuario */
	public static final String UEM000 = "UEM000";
	public static final String UEM017 = "UEM017";
	public static final String UEM019 = "UEM019";
	public static final String UEM020 = "UEM020";
	public static final String UEM021 = "UEM021";
	public static final String UEM022 = "UEM022";
	public static final String UEM025 = "UEM025";
	public static final String UEM027 = "UEM027";
	/* Codigo de mensajes informativos del usuario */
	public static final String UIM010 = "UIM010";
	public static final String UIM005 = "UIM005";
	public static final String UIM003 = "UIM003";
	public static final String UIM019 = "UIM019";
	public static final String UIM011 = "UIM011";
	/* Codigos de mensajes de error del desarrollador */
	public static final String DEM000 = "DEM000";
	public static final String DEM029 = "DEM029";
	public static final String DEM031 = "DEM031";
	public static final String DEM038 = "DEM038";
	public static final String DEM044 = "DEM044";
	/* Codigos de mensajes informativos del desarrollador */
	public static final String DIM011 = "DIM011";
	public static final String DIM012 = "DIM012";
	public static final String DIM007 = "DIM007";
	/* Codigos de mensajes enviados a los usuarios por SMS */
}
