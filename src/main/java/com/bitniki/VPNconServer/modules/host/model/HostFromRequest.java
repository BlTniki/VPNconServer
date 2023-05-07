package com.bitniki.VPNconServer.modules.host.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model for request body
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostFromRequest {
    /**
     * Название сервера.
     */
    private String name;

    /**
     * Ip адрес до сервера. Комбинация айпи и порта должна быть уникальна.
     */
    private String ipaddress;

    /**
     * Порт, который слушает хост. Комбинация айпи и порта должна быть уникальна.
     */
    private Integer port;

    /**
     * Внутренний адрес сети (например 10.8.0.0)
     */
    private String hostInternalNetworkPrefix;

    /**
     * Пароль хоста.
     */
    private String hostPassword;

    /**
     * Публичный ключ Wireguard.
     */
    private String hostPublicKey;
}
