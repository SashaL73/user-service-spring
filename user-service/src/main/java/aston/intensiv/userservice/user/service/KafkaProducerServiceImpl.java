package aston.intensiv.userservice.user.service;

import dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    @Override
    public void sendMessage(String topic, NotificationMessage message) {
        kafkaTemplate.send(topic, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Ошибка отправки сообщения", ex);
                        return;
                    }

                    log.info("Сообщение отправлено message={}", message);
                });
    }
}
