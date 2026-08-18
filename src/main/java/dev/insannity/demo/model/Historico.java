package dev.insannity.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record Historico(
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime data,
    String descricao
) {
}
