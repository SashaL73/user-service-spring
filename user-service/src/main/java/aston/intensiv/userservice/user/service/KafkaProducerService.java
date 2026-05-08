package aston.intensiv.userservice.user.service;

import dto.NotificationMessage;

public interface KafkaProducerService {
    void sendMessage(String topic, NotificationMessage message);
}
