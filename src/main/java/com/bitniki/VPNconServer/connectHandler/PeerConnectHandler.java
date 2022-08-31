package com.bitniki.VPNconServer.connectHandler;

import com.bitniki.VPNconServer.entity.HostEntity;
import com.bitniki.VPNconServer.entity.PeerEntity;
import com.bitniki.VPNconServer.entity.UserEntity;
import com.bitniki.VPNconServer.model.PeerForRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

// this class serve for hand communication with host
public class PeerConnectHandler {
    final String apiVersion = "1.0"; // sets api version on host
    private HostEntity host;
    private UserEntity user;
    private PeerEntity peer;
    private String hostIpAdress;
    private HttpEntity<PeerForRequest> httpEntity;

    public void createPeerOnHostAndFillEntity() {
        final String uri = this.hostIpAdress + "/peers";
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



//    public void getAllTest() {
//        final String uri = this.hostIpAdress + "/peers";
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<PeerForRequest[]> result = restTemplate.exchange(uri, HttpMethod.GET, this.httpEntity, PeerForRequest[].class);
//            System.out.println(result);
//        } catch (Exception e) {
//
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public void getOneTest() {
//        final String uri = this.hostIpAdress + "/peers/goodtest_21";
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<PeerForRequest> result = restTemplate.exchange(uri, HttpMethod.GET, this.httpEntity, PeerForRequest.class);
//            System.out.println(result);
//        } catch (Exception e) {
//
//            System.out.println(e.getMessage());
//        }
//    }

    public PeerConnectHandler(PeerEntity peer) {
        this.setPeer(peer);
        this.setHost(this.peer.getHost());
        this.setUser(this.peer.getUser());
        this.setHostIpAdress(this.host.getIpadress());
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

    public void setHostIpAdress(String hostIpAdress) {
        //build host url with chosen api version
        this.hostIpAdress = "http://"+ hostIpAdress +"/api/"+this.apiVersion;
    }

    public void setHttpEntity(HttpEntity<PeerForRequest> httpEntity) {
        this.httpEntity = httpEntity;
    }

    private HttpEntity<PeerForRequest> makeHttpEntity() {
        //making HttpEntity for requests
        //set auth header and body for request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Auth", host.getServerPassword());
        return new HttpEntity<>(PeerForRequest.toModel(this.getPeer()), headers);
    }
}
