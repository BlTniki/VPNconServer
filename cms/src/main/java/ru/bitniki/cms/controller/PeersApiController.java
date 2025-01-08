package ru.bitniki.cms.controller;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.bitniki.cms.controller.model.AddPeerRequest;
import ru.bitniki.cms.controller.model.PeerResponse;

@RestController
public class PeersApiController implements PeersApi {
    private static final Logger LOGGER = LogManager.getLogger();

    public ResponseEntity<List<PeerResponse>> peersGet(Long userId, Long hostId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<PeerResponse> peersPost(AddPeerRequest body) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
