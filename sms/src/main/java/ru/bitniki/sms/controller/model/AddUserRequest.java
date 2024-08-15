package ru.bitniki.sms.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * AddUserRequest
 */
@Validated
@Setter
public class AddUserRequest   {
    @JsonProperty("telegram_id")
    private Long telegramId;

    @JsonProperty("username")
    private String username;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddUserRequest addUserRequest = (AddUserRequest) o;
        return Objects.equals(this.telegramId, addUserRequest.telegramId)
                && Objects.equals(this.username, addUserRequest.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telegramId, username);
    }

    @Override
    public String toString() {
        return "class AddUserRequest {\n"
                + "    telegramId: " + toIndentedString(telegramId) + "\n"
                + "    username: " + toIndentedString(username) + "\n"
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
