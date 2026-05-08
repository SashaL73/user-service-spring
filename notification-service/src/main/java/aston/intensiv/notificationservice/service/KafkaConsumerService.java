package aston.intensiv.notificationservice.service;

import dto.NotificationMessage;

public interface KafkaConsumerService {

    public void consume(NotificationMessage message);
}
