package aston.intensiv.userservice.user.service;

import dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    @Override
    public void sendMessage(String topic, NotificationMessage message) {
        kafkaTemplate.send(topic, message);
    }
}
