package aston.intensiv.userservice.user.service;

import dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;
    private final NotificationClient notificationClient;

    @Override
    @Async
    public void sendMessage(String topic, NotificationMessage message) {
        try {
            kafkaTemplate.send(topic, message)
                    .orTimeout(5, TimeUnit.SECONDS)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Ошибка отправки сообщения", ex);

                            return;
                        }

                        log.info("Сообщение отправлено message={}", message);
                    });
        } catch (Exception e) {
            log.error("Ошибка отправки kafka e={}", e);
            sendByRest(message);
        }

    }

    private void sendByRest(NotificationMessage message) {
        try {
            notificationClient.sendNotification(message);
            log.info("Уведомление отправлено через rest: {}", message);
        } catch (Exception exception) {
            log.error("Не удалось отправить уведомление", exception);
        }
    }
}
