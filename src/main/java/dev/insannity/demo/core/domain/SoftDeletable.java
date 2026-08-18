package dev.insannity.demo.core.domain;

import java.time.LocalDateTime;

/**
 * Interface que adiciona o comportamento de exclusão lógica (soft delete) a uma entidade.
 * Qualquer entidade que implementar essa interface ganha o método deletar().
 */
public interface SoftDeletable {

    void setDataExclusao(LocalDateTime data);

    /**
     * Marca a entidade como excluída preenchendo a data atual.
     */
    default void deletar() {
        this.setDataExclusao(LocalDateTime.now());
    }

}
