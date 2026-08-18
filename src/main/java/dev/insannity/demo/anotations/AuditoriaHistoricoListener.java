package dev.insannity.demo.anotations;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import dev.insannity.demo.Historico;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuditoriaHistoricoListener extends AbstractMongoEventListener<Object> {

    private final MongoTemplate mongoTemplate;

    @Override
    @SuppressWarnings("unchecked")
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object entidadeNova = event.getSource();
        Class<?> clazz = entidadeNova.getClass();

        if (!clazz.isAnnotationPresent(AuditarHistorico.class)) {
            return;
        }

        try {
            Field idField = getIdField(clazz);
            if (idField == null) return;

            idField.setAccessible(true);
            Object id = idField.get(entidadeNova);

            if (id == null) return;

            Object entidadeAntiga = mongoTemplate.findById(id, clazz);
            if (entidadeAntiga == null) return;

            Field historicoField = getHistoricoField(clazz);
            if (historicoField == null) return;

            historicoField.setAccessible(true);
            List<Historico> historicos = (List<Historico>) historicoField.get(entidadeNova);
            List<Historico> historicosAntigos = (List<Historico>) historicoField.get(entidadeAntiga);

            if (historicosAntigos != null && !historicosAntigos.isEmpty()) {
                if (historicos == null) {
                    historicos = new java.util.ArrayList<>(historicosAntigos);
                    historicoField.set(entidadeNova, historicos);
                } else if (historicos.isEmpty()) {
                    historicos.addAll(historicosAntigos);
                }
            } else if (historicos == null) {
                historicos = new java.util.ArrayList<>();
                historicoField.set(entidadeNova, historicos);
            }
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                if (field.equals(idField) || field.equals(historicoField) ||
                    field.isAnnotationPresent(CreatedDate.class) ||
                    field.isAnnotationPresent(LastModifiedDate.class)) {
                    continue;
                }

                Object valorAntigo = field.get(entidadeAntiga);
                Object valorNovo = field.get(entidadeNova);

                if (!Objects.equals(valorAntigo, valorNovo)) {
                    String strAntigo = valorAntigo != null ? valorAntigo.toString() : "";
                    String strNovo = valorNovo != null ? valorNovo.toString() : "";

                    String descricao = String.format("campo %s alterado de \"%s\" para \"%s\"", 
                            field.getName(), strAntigo, strNovo);

                    historicos.add(new Historico(LocalDateTime.now(), descricao));
                }
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Erro ao processar auditoria de histórico para " + clazz.getSimpleName(), e);
        }
    }

    private Field getIdField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class) || field.getName().equalsIgnoreCase("id")) {
                return field;
            }
        }
        return null;
    }

    private Field getHistoricoField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equalsIgnoreCase("historico") && List.class.isAssignableFrom(field.getType())) {
                return field;
            }
        }
        return null;
    }
}
