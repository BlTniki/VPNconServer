package ru.bitniki.sms.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * SubscriptionResponse
 */
@Validated
@Setter
public class SubscriptionResponse   {
    @JsonProperty("id")
    private Long id;

    /**
     * Gets or Sets role
     */
    public enum RoleEnum {
        ACTIVATED_USER("ACTIVATED_USER"),
        ACTIVATED_CLOSE_USER("ACTIVATED_CLOSE_USER"),
        ADMIN("ADMIN"),
        DEACTIVATED_USER("DEACTIVATED_USER");

        private final String value;

        RoleEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static RoleEnum fromValue(String text) {
            for (RoleEnum b : RoleEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("role")
    private RoleEnum role;

    @JsonProperty("price_in_rubles")
    private BigDecimal priceInRubles;

    @JsonProperty("allowed_active_peers_count")
    private Integer allowedActivePeersCount;

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
     * Get role
     * @return role
     **/
    @Schema()
    @NotNull
    public RoleEnum getRole() {
        return role;
    }

    /**
     * Get priceInRubles
     * @return priceInRubles
     **/
    @Schema()
    @NotNull
    @Valid
    public BigDecimal getPriceInRubles() {
        return priceInRubles;
    }

    /**
     * Get allowedActivePeersCount
     * @return allowedActivePeersCount
     **/
    @Schema()
    @NotNull
    public Integer getAllowedActivePeersCount() {
        return allowedActivePeersCount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubscriptionResponse subscriptionResponse = (SubscriptionResponse) o;
        return Objects.equals(this.id, subscriptionResponse.id)
                && Objects.equals(this.role, subscriptionResponse.role)
                && Objects.equals(this.priceInRubles, subscriptionResponse.priceInRubles)
                && Objects.equals(this.allowedActivePeersCount, subscriptionResponse.allowedActivePeersCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, priceInRubles, allowedActivePeersCount);
    }

    @Override
    public String toString() {
        return "class SubscriptionResponse {\n"
                + "    id: " + toIndentedString(id) + "\n"
                + "    role: " + toIndentedString(role) + "\n"
                + "    priceInRubles: " + toIndentedString(priceInRubles) + "\n"
                + "    allowedActivePeersCount: " + toIndentedString(allowedActivePeersCount) + "\n"
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
