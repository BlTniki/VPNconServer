package ru.bitniki.sms.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * AddSubscriptionToUserRequest
 */
@Validated
@Setter
public class AddSubscriptionToUserRequest   {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("subscription_id")
    private Long subscriptionId;

    @JsonProperty("times")
    private Integer times;

    /**
     * Get userId
     * @return userId
     **/
    @Schema()
    @NotNull
    public Long getUserId() {
        return userId;
    }

    /**
     * Get subscriptionId
     * @return subscriptionId
     **/
    @Schema()
    @NotNull
    public Long getSubscriptionId() {
        return subscriptionId;
    }

    /**
     * Adds a subscription to the user the specified number of times. Used to extend the subscription period
     * @return times
     **/
    @Schema(description = "Adds a subscription to the user the specified number of times. Used to extend the period")
    @NotNull
    public Integer getTimes() {
        return times;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddSubscriptionToUserRequest addSubscriptionToUserRequest = (AddSubscriptionToUserRequest) o;
        return Objects.equals(this.userId, addSubscriptionToUserRequest.userId)
                && Objects.equals(this.subscriptionId, addSubscriptionToUserRequest.subscriptionId)
                && Objects.equals(this.times, addSubscriptionToUserRequest.times);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, subscriptionId, times);
    }

    @Override
    public String toString() {
        return "class AddSubscriptionToUserRequest {\n"
                + "    userId: " + toIndentedString(userId) + "\n"
                + "    subscriptionId: " + toIndentedString(subscriptionId) + "\n"
                + "    times: " + toIndentedString(times) + "\n"
                + "}";
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
