package aston.intensiv.userservice.user.service;

import dto.NotificationMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", path = "/notifications")
public interface NotificationClient {

    @PostMapping("/email")
    void sendNotification(@RequestBody NotificationMessage request);
}
