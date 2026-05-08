package aston.intensiv.notificationservice.service;

import aston.intensiv.notificationservice.exception.NotFoundException;
import dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerServiceImpl implements KafkaConsumerService{

    private final EmailService emailService;

    @Override
    @KafkaListener(topics = "user_notification")
    public void consume(NotificationMessage message) {

        Operation operation = Operation.operation(message.getOperation()).orElseThrow(
                () -> new NotFoundException("неизвестная операция")
        );

        if (operation.equals(Operation.CREATE)) {
            String sendMessage = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
            emailService.sendEmail(message.getEmail(), sendMessage);
        }

        if (operation.equals(Operation.DELETE)) {
            String sendMessage = "Здравствуйте! Ваш аккаунт был удалён.";
            emailService.sendEmail(message.getEmail(), sendMessage);
        }
    }
}
