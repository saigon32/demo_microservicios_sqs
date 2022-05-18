package com.dislicores.api.otp;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.dislicores.api.otp.business.OtpBusiness;
import com.dislicores.api.otp.model.Otp;
import com.dislicores.api.otp.model.Usuario;
import com.dislicores.api.otp.provider.NotificationProvider;
import com.dislicores.api.otp.repository.OtpRepository;
import com.dislicores.api.otp.repository.UsuarioRepository;
import com.dislicores.api.otp.util.OtpConstants;
import com.dislicores.api.otp.util.OtpProperties;

@SpringBootTest
@ActiveProfiles("test")
public class OtpRestTest {

	/** Datos de prueba */
	private static final String GENERAL_PATH = "/api/v1";
	private static final String PATH_SEPARATOR = "/";
	private static final String PATH_ACTIVATION_CODE = "activation-code";
	private static final String PATH_VALIDATE_KEY = "/validate-key";
	private static final String PATH_OTP = "/otp";
	private static final String TEST_EMAIL = "david.ramos@pragma.com.co";
	private static final String TEST_CELL_PHONE = "+573013634768";
	private static final String RESPONSE_STATE = "$.status";
	private static final String TEST_NAME = "David";
	private static final String TEST_KEY = "123456";
	private static final String IP = "127.0.0.1";

	/** Objeto para realizar el llamado a las Apis mockeadas */
	private MockMvc restMock;
//	private final Gson gson = new Gson();
	@MockBean
	private NotificationProvider notificationProvider;
	@MockBean
//	private ConfigurationResource configuration;
	/** Objeto que realizar las operaciones de negocio */
	@Autowired
	private OtpBusiness business;
	/** Objeto para el acceso a la capa de datos de la entidad Usuario */
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private OtpRepository otpRepository;
	private Usuario user;
	private Otp otp;

	/**
	 * Permite inicializar los objetos requeridos para la ejecucion de pruebas
	 */
	@BeforeEach
	public void setUp() {
		this.cleanDataBase();
		MockitoAnnotations.openMocks(this);
//		this.restMock = MockMvcBuilders.standaloneSetup(new OtpRest(business)).build();
	}

	private void cleanDataBase() {
		this.otpRepository.deleteAll();
		this.usuarioRepository.deleteAll();
	}

	private void createUser() {
		user = new Usuario();
//		user.setUsuarioId(1L);
		user.setEmail(TEST_EMAIL);
		user.setCelular(TEST_CELL_PHONE);
//		user.setNombre(TEST_NAME);
//		user.setMedioVerificacion(MedioVerificacion.EMAIL);
		user = usuarioRepository.save(user);
	}

	private void createOtp() {
		otp = new Otp();
		otp.setUsuario(user);
		Date dt = new Date();
		otp.setFechaCreacion(new Timestamp(dt.getTime()));
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, 1);
		dt = c.getTime();
		otp.setFechaVencimiento(new Timestamp(dt.getTime()));
		otp.setUsado(false);
		otp.setIntentos(0);
		otp.setClave(TEST_KEY);
		otpRepository.save(otp);
	}

//	private ActivationAccountDTO createSendActivationCodeRequest() {
//		ActivationAccountDTO activationAccountDTO = new ActivationAccountDTO();
//		activationAccountDTO.setEmail(TEST_EMAIL);
//		return activationAccountDTO;
//	}

	@Test
	public void testSendActivationCodeEmail() throws Exception {
		this.createUser();
//		when(this.configuration.getConfiguration(CONF_OTP_MAX_ATTEMPTS)).thenReturn("4");
//		when(this.configuration.getConfiguration(CONF_OTP_VALIDITY_IN_HOURS)).thenReturn("24");
//		restMock.perform(post(GENERAL_PATH + PATH_SEPARATOR + PATH_ACTIVATION_CODE).header(IP_ADDRESS, IP)
//				.contentType(MediaType.APPLICATION_JSON_VALUE)
//				.content(this.gson.toJson(this.createSendActivationCodeRequest()).getBytes())).andDo(print())
//				.andExpect(status().isOk()).andExpect(jsonPath(RESPONSE_STATE).value(HttpStatus.OK.value()));
	}

	@Test
	public void testValidateActivationCode() throws Exception {
		this.createUser();
		this.createOtp();
		restMock.perform(post(GENERAL_PATH + PATH_SEPARATOR + PATH_VALIDATE_KEY + PATH_SEPARATOR + TEST_EMAIL + PATH_SEPARATOR + TEST_KEY).header(OtpConstants.IP_ADDRESS, IP).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath(RESPONSE_STATE).value(HttpStatus.OK.value()));
	}

	@Test
	public void testSendOtpCode() throws Exception {
		this.createUser();
		when(OtpProperties.attempts).thenReturn("4");
		when(this.configuration.getConfiguration(OtpProperties.confOtpValidityInHours)).thenReturn("24");
		restMock.perform(post(GENERAL_PATH + PATH_SEPARATOR + PATH_OTP).header(OtpConstants.IP_ADDRESS, IP).header(AUTHORIZATION, TEST_AUTHORIZATION)
//				.contentType(MediaType.APPLICATION_JSON_VALUE).content(this.gson.toJson(this.createSendOtpRequest()).getBytes())).andDo(print()).andExpect(status().isOk())
//				.andExpect(jsonPath(RESPONSE_STATE).value(HttpStatus.OK.value()));
	}

	@Test
	public void testValidteOTP() throws Exception {
		this.createUser();
//	        when(this.configuration.getConfiguration(CONF_OTP_MAX_ATTEMPTS)).thenReturn("4");
//	        when(this.configuration.getConfiguration(CONF_OTP_VALIDITY_IN_HOURS)).thenReturn("24");
//	        restMock.perform(post(GENERAL_PATH + PATH_SEPARATOR + PATH_OTP).header(IP_ADDRESS, IP).header(AUTHORIZATION, TEST_AUTHORIZATION)
//	                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.gson.toJson(this.createSendOtpRequest()).getBytes())).andDo(print()).andExpect(status().isOk())
//	                .andExpect(jsonPath(RESPONSE_STATE).value(HttpStatus.OK.value()));
	}

//	private OtpRequestDTO createSendOtpRequest() {
//		OtpRequestDTO otpRequestDTO = new OtpRequestDTO();
//		otpRequestDTO.setValue("EMAIL");
//		return otpRequestDTO;
//	}
}
