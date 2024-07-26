package cotato.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GetNotExpiredArticleResponseDTO(List<LocalDateTime> expiredAtList) {
}
