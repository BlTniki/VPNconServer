package com.bitniki.VPNconServer.modules.host.entity;

import lombok.*;

import javax.persistence.*;
import java.lang.reflect.Field;

/**
 * Сущность хоста. Содержит информацию о модуле <a href="https://github.com/BlTniki/vpnconhost">vpnconhost</a>,
 * до которого будет строиться тоннель.
 */
@Entity
@Table (name = "hosts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class HostEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название сервера. Уникальный.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Ip адрес до сервера. Уникальный.
     */
    @Column(nullable = false, unique = true)
    private String ipaddress;

    /**
     * Порт, который слушает хост.
     */
    @Column(nullable = false)
    private Integer port = 80;

    /**
     * Внутренний адрес сети (например 10.8.0.0)
     */
    @Column(nullable = false)
    private String hostInternalNetworkPrefix;

    /**
     * Пароль хоста.
     */
    @Column(nullable = false)
    private String hostPassword;

    /**
     * Публичный ключ Wireguard.
     */
    @Column(nullable = false)
    private String hostPublicKey;

    /**
     * Обновляет посредством рефлексии поля этого объекта HostEntity ненулевыми полями данного объекта HostEntity.
     * @param newHost объект HostEntity, ненулевые поля которого будут использоваться для обновления полей этого объекта.
     * @return обновленный объект UserEntity.
     * @throws RuntimeException если произошла ошибка при доступе к полям через рефлексию.
     */
    public HostEntity updateWith(HostEntity newHost) {
        //get all fields
        Field[] fields = HostEntity.class.getDeclaredFields();

        //replace in this entity all fields that not null in new entity
        for (Field field: fields) {
            try {
                if (field.get(newHost) != null) {
                    field.set(this, field.get(newHost));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }
}
