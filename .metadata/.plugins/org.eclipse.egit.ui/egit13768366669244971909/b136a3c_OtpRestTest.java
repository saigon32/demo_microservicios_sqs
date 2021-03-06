package com.dislicores.api.otp;

import static com.dislicores.api.otp.util.OtpConstants.CONF_OTP_MAX_ATTEMPTS;
import static com.dislicores.api.otp.util.OtpConstants.CONF_OTP_VALIDITY_IN_HOURS;
import static com.dislicores.plugin.utilities.util.UtilConstants.AUTHORIZATION;
import static com.dislicores.plugin.utilities.util.UtilConstants.IP_ADDRESS;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.dislicores.api.otp.business.OtpBusiness;
import com.dislicores.api.otp.domain.ActivationAccountDTO;
import com.dislicores.api.otp.domain.OtpRequestDTO;
import com.dislicores.api.otp.model.Otp;
import com.dislicores.api.otp.model.Usuario;
import com.dislicores.api.otp.provider.NotificationProvider;
import com.dislicores.api.otp.repository.OtpRepository;
import com.dislicores.api.otp.repository.UsuarioRepository;
import com.dislicores.api.otp.rest.OtpRest;
import com.dislicores.api.otp.util.MedioVerificacion;
import com.dislicores.plugin.utilities.util.ConfigurationResource;
import com.google.gson.Gson;

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
	private static final String TEST_AUTHORIZATION = "eyJraWQiOiJqcHpTdXRcL3pmWXhFd1ZZXC8wekhtMldnRStUcDZVa2Z4WjZES2hIR3NLcTg9IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJlN2E5YjYzMS0yYzVjLTQ3OWEtOWQ2MS02MzFiOGY0NmZjZDgiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZ2VuZGVyIjoiTWFzY3VsaW5vIiwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLnVzLWVhc3QtMS5hbWF6b25hd3MuY29tXC91cy1lYXN0LTFfRUtSMGo1WFpTIiwicGhvbmVfbnVtYmVyX3ZlcmlmaWVkIjpmYWxzZSwiY29nbml0bzp1c2VybmFtZSI6ImU3YTliNjMxLTJjNWMtNDc5YS05ZDYxLTYzMWI4ZjQ2ZmNkOCIsImF1ZCI6IjVuYXNxZ3Z2cHBjZTAwNHBqdG9qc2ZodWVhIiwiY3VzdG9tOmNvdW50cnlJZCI6IjEiLCJldmVudF9pZCI6IjUxOGMzMDIzLTBmNjctNDUwYy04OWFhLWM2M2VjNjE1NWMzNyIsInVwZGF0ZWRfYXQiOjE1NzQyMDc4OTQ5NjgsImN1c3RvbTppc0Zyb21CZW5lZml0cyI6ImZhbHNlIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE1NzQ2ODc5OTcsIm5hbWUiOiJEYXZpZCIsInBob25lX251bWJlciI6Iis1NzMwMTM2NTQyNjkiLCJleHAiOjE1NzQ2OTE1OTcsImN1c3RvbTpyb2xlIjoiQ09NUFJBRE9SIiwiaWF0IjoxNTc0Njg3OTk3LCJmYW1pbHlfbmFtZSI6IlJhbW9zIiwiZW1haWwiOiJkYXZpZC5yYW1vc0BwcmFnbWEuY29tLmNvIn0.NmWdtWpJM_9kOTKdXiL309earr5rn9tLdz6DKBMLTKLqhaLOEx6IBhEICn6mPXZ6lghaCotkreDdEFX8P33vinvdKplWldKl9R8U1F6msRnkd8Mzp-x0p2Au3B5mkuvLL58WJ4JbobtBD5JWPzdJ7TPql0mHpuONgKXZwHcfmpJ9GLlBC9Zq40Y0WO-SXadmm6_JZCuIy_JZHrbPEvuVqRaPcD18LqrjsWF6nRSC4IDGQyM33I7-_AkiMcMahsH_hjzU0u56WzWwRWkbpgbk4IhZRsImlLcYRI3HbjzXpiFG7A-2YJanC00W0CzmLYezIV14aizTMrGte0kL_RlnYA";

	/** Objeto para realizar el llamado a las Apis mockeadas */
	private MockMvc restMock;
	private final Gson gson = new Gson();
	@MockBean
	private NotificationProvider notificationProvider;
	@MockBean
	private ConfigurationResource configuration;
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
		this.restMock = MockMvcBuilders.standaloneSetup(new OtpRest(business)).build();
	}

	private void cleanDataBase() {
		this.otpRepository.deleteAll();
		this.usuarioRepository.deleteAll();
	}

	private void createUser() {
		user = new Usuario();
		user.setUsuarioId(1L);
		user.setEmail(TEST_EMAIL);
		user.setCelular(TEST_CELL_PHONE);
		user.setNombre(TEST_NAME);
		user.setMedioVerificacion(MedioVerificacion.EMAIL);
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

	private ActivationAccountDTO createSendActivationCodeRequest() {
		ActivationAccountDTO activationAccountDTO = new ActivationAccountDTO();
		activationAccountDTO.setEmail(TEST_EMAIL);
		return activationAccountDTO;
	}

	@Test
	public void testSendActivationCodeEmail() throws Exception {
		this.createUser();
		when(this.configuration.getConfiguration(CONF_OTP_MAX_ATTEMPTS)).thenReturn("4");
		when(this.configuration.getConfiguration(CONF_OTP_VALIDITY_IN_HOURS)).thenReturn("24");
		restMock.perform(post(GENERAL_PATH + PATH_SEPARATOR + PATH_ACTIVATION_CODE).header(IP_ADDRESS, IP)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(this.gson.toJson(this.createSendActivationCodeRequest()).getBytes())).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath(RESPONSE_STATE).value(HttpStatus.OK.value()));
	}

	@Test
	public void testValidateActivationCode() throws Exception {
		this.createUser();
		this.createOtp();
		restMock.perform(post(GENERAL_PATH + PATH_SEPARATOR + PATH_VALIDATE_KEY + PATH_SEPARATOR + TEST_EMAIL + PATH_SEPARATOR + TEST_KEY).header(IP_ADDRESS, IP)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath(RESPONSE_STATE).value(HttpStatus.OK.value()));
	}

	@Test
	public void testSendOtpCode() throws Exception {
		this.createUser();
		when(this.configuration.getConfiguration(CONF_OTP_MAX_ATTEMPTS)).thenReturn("4");
		when(this.configuration.getConfiguration(CONF_OTP_VALIDITY_IN_HOURS)).thenReturn("24");
		restMock.perform(post(GENERAL_PATH + PATH_SEPARATOR + PATH_OTP).header(IP_ADDRESS, IP).header(AUTHORIZATION, TEST_AUTHORIZATION)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(this.gson.toJson(this.createSendOtpRequest()).getBytes())).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath(RESPONSE_STATE).value(HttpStatus.OK.value()));
	}
	
	   @Test
	    public void testValidteOTP() throws Exception {
	        this.createUser();
	        when(this.configuration.getConfiguration(CONF_OTP_MAX_ATTEMPTS)).thenReturn("4");
	        when(this.configuration.getConfiguration(CONF_OTP_VALIDITY_IN_HOURS)).thenReturn("24");
	        restMock.perform(post(GENERAL_PATH + PATH_SEPARATOR + PATH_OTP).header(IP_ADDRESS, IP).header(AUTHORIZATION, TEST_AUTHORIZATION)
	                .contentType(MediaType.APPLICATION_JSON_VALUE).content(this.gson.toJson(this.createSendOtpRequest()).getBytes())).andDo(print()).andExpect(status().isOk())
	                .andExpect(jsonPath(RESPONSE_STATE).value(HttpStatus.OK.value()));
	    }

	private OtpRequestDTO createSendOtpRequest() {
		OtpRequestDTO otpRequestDTO = new OtpRequestDTO();
		otpRequestDTO.setValue("EMAIL");
		return otpRequestDTO;
	}
}
