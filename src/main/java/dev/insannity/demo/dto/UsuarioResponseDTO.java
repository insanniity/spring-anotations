package dev.insannity.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.insannity.demo.model.Historico;

import java.time.LocalDateTime;
import java.util.List;

public record UsuarioResponseDTO(
    String id,
    String nome,
    String email,
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dataCriacao,
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dataUltimaAlteracao,
    
    List<Historico> historico
) {
}
