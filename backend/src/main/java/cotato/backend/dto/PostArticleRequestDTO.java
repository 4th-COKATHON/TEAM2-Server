package cotato.backend.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PostArticleRequestDTO(String receiverName, String title, String detail, LocalDate expiredAt) {
}
