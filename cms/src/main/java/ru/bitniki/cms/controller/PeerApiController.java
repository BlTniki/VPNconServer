package ru.bitniki.cms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.bitniki.cms.controller.model.PeerResponse;
import ru.bitniki.cms.controller.model.UpdatePeerRequest;

@RestController
public class PeerApiController implements PeerApi {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public ResponseEntity<PeerResponse> peerIdDelete(Long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<PeerResponse> peerIdPut(Long id, UpdatePeerRequest body) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
