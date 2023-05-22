package com.bitniki.VPNconServer.modules.metacodes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Тело, получаемое от запроса на применение метакода.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetacodeFromRequest {

    /**
     * Метакод.
     */
    private String code;
}
