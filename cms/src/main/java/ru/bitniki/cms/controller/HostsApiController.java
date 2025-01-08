package ru.bitniki.cms.controller;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.bitniki.cms.controller.model.HostResponse;

@RestController
public class HostsApiController implements HostsApi {
    private static final Logger LOGGER = LogManager.getLogger();

    public ResponseEntity<List<HostResponse>> hostsGet() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<HostResponse>> hostsIdGet(Long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
