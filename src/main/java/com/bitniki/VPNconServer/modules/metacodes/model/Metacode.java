package com.bitniki.VPNconServer.modules.metacodes.model;

import com.bitniki.VPNconServer.modules.metacodes.entity.MetacodeEntity;
import com.bitniki.VPNconServer.modules.metacodes.operation.Operation;
import lombok.*;
import org.jetbrains.annotations.NotNull;

/**
 * Модель MetacodeEntity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Metacode {

    /**
     * Код, по которому проверяется разрешение на действие.
     */
    private String code;

    /**
     * Действие, которое следует сделать.
     */
    private Operation operation;

    /**
     * Создание модели на основе сущности.
     * @param entity Сущность, из которой следует создать модель.
     * @return Новая модель.
     */
    public static Metacode toModel(@NotNull MetacodeEntity entity) {
        return Metacode.builder()
                .code(entity.getCode())
                .operation(entity.getOperation())
                .build();
    }
}
