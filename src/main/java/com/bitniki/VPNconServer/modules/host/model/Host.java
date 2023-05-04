package com.bitniki.VPNconServer.modules.host.model;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import lombok.*;

/**
 * Модель {@link HostEntity}
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Host {
    private Long id;

    /**
     * Название сервера.
     */
    private String name;

    /**
     * Ip адрес до сервера.
     */
    private String ipaddress;

    /**
     * Порт, который слушает хост.
     */
    private Integer port;

    /**
     * Внутренний адрес сети (например 10.8.0.0)
     */
    private String hostInternalNetworkPrefix;

    /**
     * Публичный ключ Wireguard.
     */
    private String hostPublicKey;


    public static Host toModel (HostEntity entity) {
        return new Host(entity);
    }

    public Host(HostEntity entity) {
        this.setId(entity.getId());
        this.setName(entity.getName());
        this.setIpaddress(entity.getIpaddress());
        this.setPort(entity.getPort());
        this.setHostInternalNetworkPrefix(entity.getHostInternalNetworkPrefix());
        this.setHostPublicKey(entity.getHostPublicKey());
    }
}
