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
        this.setHostPublicKey(entity.getHostPublicKey());

    }
}
