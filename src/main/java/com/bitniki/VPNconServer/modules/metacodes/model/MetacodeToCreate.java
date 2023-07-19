package com.bitniki.VPNconServer.modules.metacodes.model;

import com.bitniki.VPNconServer.modules.metacodes.operation.Operation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Тело, получаемое от запроса на создание метакода.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetacodeToCreate {

    /**
     * Действие, для которого следует создать код.
     */
    private Operation operation;
}
