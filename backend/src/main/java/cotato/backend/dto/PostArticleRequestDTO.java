package cotato.backend.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostArticleRequestDTO(String receiverName, String title, String detail, LocalDateTime expiredAt) {
}
