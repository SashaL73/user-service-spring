package dto;

import lombok.Builder;

@Builder
public record EmailRequest(String email, String message) {
}
