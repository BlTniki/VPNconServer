package ru.bitniki.cms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.bitniki.cms.controller.model.ErrorResponse;
import ru.bitniki.cms.controller.model.PeerResponse;
import ru.bitniki.cms.controller.model.UpdatePeerRequest;

@SuppressWarnings("checkstyle:LineLength")
@Validated
public interface PeerApi {

    @Operation(summary = "Delete peer", tags = { "peers" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deleted peer", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PeerResponse.class))),
        @ApiResponse(responseCode = "404", description = "Peer or host not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestMapping(
            value = "/peer/{id}",
            produces = { "application/json" },
            method = RequestMethod.DELETE
    )
    ResponseEntity<PeerResponse> peerIdDelete(@Parameter(in = ParameterIn.PATH, description = "peer id", required = true, schema = @Schema()) @PathVariable("id") Long id);


    @Operation(summary = "Update peer", tags = { "peers" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated peer", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PeerResponse.class))),
        @ApiResponse(responseCode = "404", description = "Peer or host not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestMapping(
            value = "/peer/{id}",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.PUT
    )
    ResponseEntity<PeerResponse> peerIdPut(
            @Parameter(in = ParameterIn.PATH, description = "peer id", required = true, schema = @Schema()) @PathVariable("id") Long id,
            @Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @Valid @RequestBody UpdatePeerRequest body
    );
}

