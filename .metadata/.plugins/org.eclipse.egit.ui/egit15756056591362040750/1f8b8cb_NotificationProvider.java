package com.dislicores.api.otp.provider;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
//import com.dislicores.plugin.utilities.domain.Response;
//import com.dislicores.plugin.utilities.util.MessageResource;
import org.springframework.web.client.RestTemplate;

import com.dislicores.api.otp.domain.EmailRequestDto;
import com.dislicores.api.otp.domain.ResponseDto;
import com.dislicores.api.otp.util.OtpProperties;

/**
 * Clase que define un componente para el envio de notificaciones
 *
 * @author ricardo.ayala@pragma.com.co
 *
 */
@Component
public class NotificationProvider {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationProvider.class);
    /** Objeto para la carga de propiedades */
    private final OtpProperties properties;
    private final RestTemplate restTemplate;

    @Autowired
    public NotificationProvider(OtpProperties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    public boolean sendEmail(String email, String otp, String ip) {
        try {
            var emailRequest = new EmailRequestDto();
            emailRequest.setTo(email);
            emailRequest.setEmailTemplate(this.properties.getNotification().getOtpEmailTemplate());
            emailRequest.setBodyParams(List.of(otp));

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.set("Ip-Address", ip);

            HttpEntity<EmailRequestDto> request = new HttpEntity<>(emailRequest, headers);
            ResponseDto<?> response = this.restTemplate
                    .postForObject(URI.create(this.properties.getNotification().getEndpointUrl() + "/email"), request, ResponseDto.class);
            if (Objects.nonNull(response) && 200 == response.getStatus()) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Error in sendEmail", e);
        }
        return false;
    }

}
