package aston.intensiv.userservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic userNotificationTopic() {
        return new NewTopic("user_notification", 1, (short) 1);
    }

}
