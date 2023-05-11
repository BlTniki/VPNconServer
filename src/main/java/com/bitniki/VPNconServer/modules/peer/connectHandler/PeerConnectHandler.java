package com.bitniki.VPNconServer.modules.peer.connectHandler;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.peer.connectHandler.exception.PeerConnectHandlerException;
import com.bitniki.VPNconServer.modules.peer.connectHandler.model.PeerForHost;
import com.bitniki.VPNconServer.modules.peer.connectHandler.model.PeerFromHost;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Этот класс является предназначен для коммуникации с хостом.
 */
@Slf4j
public class PeerConnectHandler {
    final String apiVersion = "1.0"; // sets api version on host
    private final PeerForHost peerForRequest;
    private final String hostIpAddress;
    private final HttpEntity<PeerForHost> httpEntity;

    public PeerConnectHandler(PeerEntity peer) {
        this.peerForRequest = PeerForHost.builder()
                .peerId(peer.getPeerConfName() + "_" + peer.getUser().getId())
                .peerIp(peer.getPeerIp())
                .peerPrivateKey(peer.getPeerPrivateKey())
                .peerPublicKey(peer.getPeerPublicKey())
                .build();

        this.hostIpAddress = "http://%s:%d/api/%s".formatted(
                peer.getHost().getIpaddress(),
                peer.getHost().getPort(),
                apiVersion
        );
        this.httpEntity = this.makeHttpEntity(peer.getHost().getHostPassword());
    }

    /**
     * Создание {@link HostEntity} с заголовком Auth и телом {@link PeerForHost}.
     * @param hostPassword Пароль хоста.
     * @return {@link HostEntity} с заголовком Auth и телом {@link PeerForHost}.
     */
    private HttpEntity<PeerForHost> makeHttpEntity(String hostPassword) {
        //making HttpEntity for requests
        //set auth header and body for request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Auth", hostPassword);
        return new HttpEntity<>(peerForRequest, headers);
    }

    /**
     * Создать пира на хосте.
     * @return Ответ хоста в виде {@link PeerFromHost} с заполненными private и public ключами.
     * @throws PeerConnectHandlerException В случае проблем на стороне хоста.
     */
    public PeerFromHost createPeerOnHost() throws PeerConnectHandlerException {
        final String uri = this.hostIpAddress + "/peers";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PeerFromHost> response;
        try {
            response = restTemplate.exchange(uri, HttpMethod.POST, this.httpEntity, PeerFromHost.class);
        } catch (Exception e) {
            log.error("Error creating peer on host", e);
            throw new PeerConnectHandlerException("Error creating peer on host");
        }

        return response.getBody();
    }

    /**
     * Удаляет пира на хосте.
     * @throws PeerConnectHandlerException В случае проблем на стороне хоста.
     */
    public void deletePeerOnHost() throws PeerConnectHandlerException {
        //build uri avoiding nullable warning
        final String uri = this.hostIpAddress + "/peers/"+ peerForRequest.getPeerId();
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.exchange(uri, HttpMethod.DELETE, this.httpEntity, String.class);
        } catch (Exception e) {
            log.error("Error deleting peer on host", e);
            throw new PeerConnectHandlerException("Error deleting peer on host");
        }
    }

    /**
     * Создаёт токен для скачивания конфига для туннеля с хоста.
     * @return Токен в виде url-safe строки.
     * @throws PeerConnectHandlerException В случае проблем на стороне хоста.
     */
    public String getDownloadConfToken() throws PeerConnectHandlerException {
        final String uri = this.hostIpAddress + "/conf/" + peerForRequest.getPeerId();
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(uri, HttpMethod.POST, this.httpEntity, String.class).getBody();
        } catch (Exception e) {
            log.error("Error creating token on host", e);
            throw new PeerConnectHandlerException("Error creating token on host");
        }
    }

    /**
     * Активирует пира на хосте.
     * @throws PeerConnectHandlerException В случае проблем на стороне хоста.
     */
    public void activateOnHost() throws PeerConnectHandlerException {
        final String uri = this.hostIpAddress + "/peers/activate/" + peerForRequest.getPeerId();
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.exchange(uri, HttpMethod.POST, this.httpEntity, HashMap.class);
        } catch (Exception e) {
            log.error("Error activating peer on host", e);
            throw new PeerConnectHandlerException("Error activating peer on host");
        }
    }

    /**
     * Деактивирует пира на хосте.
     * @throws PeerConnectHandlerException В случае проблем на стороне хоста.
     */
    public void deactivateOnHost() throws PeerConnectHandlerException {
        final String uri = this.hostIpAddress + "/peers/deactivate/" + peerForRequest.getPeerId();
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.exchange(uri, HttpMethod.POST, this.httpEntity, HashMap.class);
        } catch (Exception e) {
            log.error("Error deactivating peer on host", e);
            throw new PeerConnectHandlerException("Error deactivating peer on host");
        }
    }
}
