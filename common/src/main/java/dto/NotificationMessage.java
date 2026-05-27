package dto;

import lombok.Builder;

@Builder
public record NotificationMessage (
    String operation,
    String email) {
}
