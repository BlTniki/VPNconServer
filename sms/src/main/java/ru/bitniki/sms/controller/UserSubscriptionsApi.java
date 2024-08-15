
package ru.bitniki.sms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bitniki.sms.controller.model.AddSubscriptionToUserRequest;
import ru.bitniki.sms.controller.model.ErrorResponse;
import ru.bitniki.sms.controller.model.UserSubscriptionResponse;

@SuppressWarnings("checkstyle:LineLength")
@Validated
public interface UserSubscriptionsApi {
    @Operation(summary = "Get user subscription", description = "", tags = { "user_subscriptions" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns specific user subscription", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserSubscriptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "User subscription not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @RequestMapping(value = "/user_subscriptions", produces = { "application/json" }, method = RequestMethod.GET)
    ResponseEntity<UserSubscriptionResponse> userSubscriptionsGet(@NotNull @Parameter(in = ParameterIn.QUERY, description = "user telegram id", required = true, schema = @Schema()) @Valid @RequestParam(value = "user_id", required = true) Long userId);


    @Operation(summary = "Add subscription to user", description = "", tags = { "user_subscriptions" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Adds subscription to user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserSubscriptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "User or subscription not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))) })
    @RequestMapping(value = "/user_subscriptions", produces = { "application/json" }, consumes = { "application/json" }, method = RequestMethod.POST)
    ResponseEntity<UserSubscriptionResponse> userSubscriptionsPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody AddSubscriptionToUserRequest body);
}

