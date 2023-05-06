package com.bitniki.VPNconServer.modules.peer.connectHandler;

import com.bitniki.VPNconServer.modules.host.entity.HostEntity;
import com.bitniki.VPNconServer.modules.peer.entity.PeerEntity;
import com.bitniki.VPNconServer.modules.peer.model.PeerForRequest;
import com.bitniki.VPNconServer.modules.user.entity.UserEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Objects;

// this class serve for hand communication with host
public class PeerConnectHandler {
    final String apiVersion = "1.0"; // sets api version on host
    private HostEntity host;
    private UserEntity user;
    private PeerEntity peer;
    private PeerForRequest peerForRequest;
    private String hostIpAddress;
    private HttpEntity<PeerForRequest> httpEntity;

    public void createPeerOnHostAndFillEntity() {
        final String uri = this.hostIpAddress + "/peers";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PeerForRequest> response;
        try {
            response = restTemplate.exchange(uri, HttpMethod.POST, this.httpEntity, PeerForRequest.class);
        } catch (Exception e) {
            // somewhere there should be logging
            System.out.println(e.getMessage());//400 BAD REQUEST: "Service answers like: This peerId has already taken"
            throw e;
        }
        Objects.requireNonNull(response.getBody()).fillEntityFromModel(peer);
    }

    public void updatePeerOnHost() {
        final String uri = this.hostIpAddress + "/peers";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PeerForRequest> response;
        try {
            response = restTemplate.exchange(uri, HttpMethod.PUT, this.httpEntity, PeerForRequest.class);
        } catch (Exception e) {
            // somewhere there should be logging
            System.out.println(e.getMessage());//400 BAD REQUEST: "Service answers like: This peerId has already taken"
            throw e;
        }
        Objects.requireNonNull(response.getBody()).fillEntityFromModel(peer);
    }

    public void deletePeerOnHost() {
        //build uri avoiding nullable warning
        final String uri = this.hostIpAddress + "/peers/"+ getPeerForRequest().getPeerId();
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.exchange(uri, HttpMethod.DELETE, this.httpEntity, String.class);
        } catch (Exception e) {
            // somewhere there should be logging
            System.out.println(e.getMessage());//400 BAD REQUEST: "Service answers like: This peerId has already taken"
            throw e;
        }
    }

    public String getDownloadConfToken() {
        final String uri = this.hostIpAddress + "/conf/" + getPeerForRequest().getPeerId();
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(uri, HttpMethod.POST, this.httpEntity, String.class).getBody();
        } catch (Exception e) {
            // somewhere there should be logging
            System.out.println(e.getMessage());//400 BAD REQUEST: "Service answers like: This peerId has already taken"
            throw e;
        }
    }

    public Boolean activateOnHost() {
        final String uri = this.hostIpAddress + "/peers/activate/" + getPeerForRequest().getPeerId();
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.exchange(uri, HttpMethod.POST, this.httpEntity, HashMap.class);
        } catch (Exception e) {
            // somewhere there should be logging
            System.out.println(e.getMessage());//400 BAD REQUEST: "Service answers like: This peerId has already taken"
            throw e;
        }
        return true;
    }

    public Boolean deactivateOnHost() {
        final String uri = this.hostIpAddress + "/peers/deactivate/" + getPeerForRequest().getPeerId();
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.exchange(uri, HttpMethod.POST, this.httpEntity, HashMap.class);
        } catch (Exception e) {
            // somewhere there should be logging
            System.out.println(e.getMessage());//400 BAD REQUEST: "Service answers like: This peerId has already taken"
            throw e;
        }
        return true;
    }

    public PeerConnectHandler(PeerEntity peer) {
        this.setPeer(peer);
        this.setPeerForRequest(PeerForRequest.toModel(peer));
        this.setHost(this.peer.getHost());
        this.setUser(this.peer.getUser());
        this.setHostIpAddress(this.host.getIpaddress());
        this.setHttpEntity(this.makeHttpEntity());
    }

    public HostEntity getHost() {
        return host;
    }

    public void setHost(HostEntity host) {
        this.host = host;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public PeerEntity getPeer() {
        return peer;
    }

    public void setPeer(PeerEntity peer) {
        this.peer = peer;
    }

    public PeerForRequest getPeerForRequest() {
        return peerForRequest;
    }

    public void setPeerForRequest(PeerForRequest peerForRequest) {
        this.peerForRequest = peerForRequest;
    }

    public void setHostIpAddress(String hostIpAddress) {
        //build host url with chosen api version
        this.hostIpAddress = "http://"+ hostIpAddress +"/api/"+this.apiVersion;
    }

    public void setHttpEntity(HttpEntity<PeerForRequest> httpEntity) {
        this.httpEntity = httpEntity;
    }

    private HttpEntity<PeerForRequest> makeHttpEntity() {
        //making HttpEntity for requests
        //set auth header and body for request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Auth", host.getHostPassword());
        return new HttpEntity<>(peerForRequest, headers);
    }
}