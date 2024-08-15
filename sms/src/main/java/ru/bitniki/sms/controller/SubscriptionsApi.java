package ru.bitniki.sms.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bitniki.sms.controller.model.ErrorResponse;
import ru.bitniki.sms.controller.model.SubscriptionResponse;

@SuppressWarnings("checkstyle:LineLength")
@Validated
public interface SubscriptionsApi {
    @Operation(summary = "Get subscriptions", description = "", tags = { "subscriptions" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "If a specific role is given, list all existing subscriptions that match that role. Otherwise, list all existing subscriptions", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SubscriptionResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestMapping(value = "/subscriptions", produces = { "application/json" }, method = RequestMethod.GET)
    ResponseEntity<List<SubscriptionResponse>> subscriptionsGet(@Parameter(in = ParameterIn.QUERY, description = "Role filter for subscriptions. May be null", schema = @Schema()) @Valid @RequestParam(value = "role", required = false) String role);

    @Operation(summary = "Get subscription", description = "", tags = { "subscriptions" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subscription with given id", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubscriptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestMapping(value = "/subscriptions/{id}", produces = { "application/json" }, method = RequestMethod.GET)
    ResponseEntity<SubscriptionResponse> subscriptionsIdGet(@Parameter(in = ParameterIn.PATH, description = "subscription id", required = true, schema = @Schema()) @PathVariable("id") Long id);

}

