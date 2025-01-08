package ru.bitniki.cms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bitniki.cms.controller.model.AddPeerRequest;
import ru.bitniki.cms.controller.model.ErrorResponse;
import ru.bitniki.cms.controller.model.PeerResponse;

@SuppressWarnings("checkstyle:LineLength")
@Validated
public interface PeersApi {

    @Operation(summary = "Get peers", description = "Returns all peers, filtering by the passed user_id and host_id. If some of parameters is not specified, then it is perceived as `any`", tags = { "peers" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns list of peers filtered by user_id and host_id", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PeerResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestMapping(
            value = "/peers",
            produces = { "application/json" },
            method = RequestMethod.GET
    )
    ResponseEntity<List<PeerResponse>> peersGet(
            @Parameter(in = ParameterIn.QUERY, description = "User id. May be null", schema = @Schema()) @Valid @RequestParam(value = "user_id", required = false) Long userId,
            @Parameter(in = ParameterIn.QUERY, description = "Host id. May be null", schema = @Schema()) @Valid @RequestParam(value = "host_id", required = false) Long hostId
    );


    @Operation(summary = "Create peer", tags = { "peers" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created peer", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PeerResponse.class))),
        @ApiResponse(responseCode = "404", description = "Host not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestMapping(
            value = "/peers",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST
    )
    ResponseEntity<PeerResponse> peersPost(@Parameter(in = ParameterIn.DEFAULT, schema = @Schema()) @Valid @RequestBody AddPeerRequest body);

}

