package dev.insannity.demo.model;

import dev.insannity.demo.core.audit.AuditarHistorico;
import dev.insannity.demo.core.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "usuarios")
@AuditarHistorico
public class Usuario extends BaseEntity {

    String nome;
    String email;
    
    @Setter(value = AccessLevel.PRIVATE)
    List<Historico> historico = new ArrayList<>();

}
