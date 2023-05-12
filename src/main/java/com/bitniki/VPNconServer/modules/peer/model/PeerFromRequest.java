package com.bitniki.VPNconServer.modules.peer.model;

import lombok.*;

/**
 * Тело реквеста на создание нового пира.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeerFromRequest {

    /**
     * Название конфигурационного файла. Должен состоять только из латиницы и/или цифр.
     */
    private String peerConfName;

    /**
     * Внутренний адрес в сети. Формат Х.Х.Х.Х. Может быть null, тогда peerIp сгенерирует самостоятельно.
     * Последний октет должен быть в пределах [2, 254].
     */
    private String peerIp;

    /**
     * Id юзера, для которого создаётся конфиг.
     */
    private Long userId;

    /**
     * Id хоста, на котором создаётся пир.
     */
    private Long hostId;
}
