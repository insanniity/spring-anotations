package dev.insannity.demo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;

import dev.insannity.demo.anotations.AuditarHistorico;


@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Document(collection = "usuarios")
@AuditarHistorico
public class Usuario {

    @Id
    String id;
    String nome;
    String email;
    @Setter(value = AccessLevel.PRIVATE)
    List<Historico> historico = new ArrayList<>();
    
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dataCriacao;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dataUltimaAlteracao;

    

}
