package ru.bitniki.sms.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * UserSubscriptionResponse
 */
@Validated
@Setter
public class UserSubscriptionResponse   {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("user")
    private UserResponse user;

    @JsonProperty("subscription")
    private SubscriptionResponse subscription;

    @JsonProperty("expiration_date")
    private LocalDate expirationDate;

    /**
     * Get id
     * @return id
     **/
    @Schema()
    @NotNull
    public Long getId() {
        return id;
    }

    /**
     * Get user
     * @return user
     **/
    @Schema()
    @NotNull
    @Valid
    public UserResponse getUser() {
        return user;
    }

    /**
     * Get subscription
     * @return subscription
     **/
    @Schema()
    @NotNull
    @Valid
    public SubscriptionResponse getSubscription() {
        return subscription;
    }

    /**
     * Get expirationDate
     * @return expirationDate
     **/
    @Schema()
    @NotNull
    @Valid
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserSubscriptionResponse userSubscriptionResponse = (UserSubscriptionResponse) o;
        return Objects.equals(this.id, userSubscriptionResponse.id)
                && Objects.equals(this.user, userSubscriptionResponse.user)
                && Objects.equals(this.subscription, userSubscriptionResponse.subscription)
                && Objects.equals(this.expirationDate, userSubscriptionResponse.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, subscription, expirationDate);
    }

    @Override
    public String toString() {
        return "class UserSubscriptionResponse {\n"
                + "    id: " + toIndentedString(id) + "\n"
                + "    user: " + toIndentedString(user) + "\n"
                + "    subscription: " + toIndentedString(subscription) + "\n"
                + "    expirationDate: " + toIndentedString(expirationDate) + "\n"
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
