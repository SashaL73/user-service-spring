package aston.intensiv.notificationservice.service;

import dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final RestTemplate template = new RestTemplate();

    @Value("${user-service.url}")
    private String path;

    @Override
    public void sendEmail(String email, String message) {

        EmailRequest emailRequest = EmailRequest.builder()
                .email(email)
                .message(message)
                .build();

        log.info("отправка сообщения на эндпоинт user-service {}", emailRequest);
        template.postForObject(path, emailRequest, Void.class);
    }
}
