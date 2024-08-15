package ru.bitniki.sms.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * UserResponse
 */
@Validated
@Setter
public class UserResponse   {
    @JsonProperty("telegram_id")
    private Long telegramId;

    @JsonProperty("username")
    private String username;

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

    /**
     * Get telegramId
     * @return telegramId
     **/
    @Schema()
    @NotNull
    public Long getTelegramId() {
        return telegramId;
    }

    /**
     * Get username
     * @return username
     **/
    @Schema()
    @NotNull
    public String getUsername() {
        return username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserResponse userResponse = (UserResponse) o;
        return Objects.equals(this.telegramId, userResponse.telegramId)
                && Objects.equals(this.username, userResponse.username)
                && Objects.equals(this.role, userResponse.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telegramId, username, role);
    }

    @Override
    public String toString() {
        return "class UserResponse {\n"
                + "    telegramId: " + toIndentedString(telegramId) + "\n"
                + "    username: " + toIndentedString(username) + "\n"
                + "    role: " + toIndentedString(role) + "\n"
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
