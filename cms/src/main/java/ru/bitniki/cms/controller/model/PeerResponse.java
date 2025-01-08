package ru.bitniki.cms.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.Objects;
import lombok.Builder;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * PeerResponse
 */
@Validated
@Setter
@Builder
public class PeerResponse   {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("name")
  private String name;

  /**
   * Gets or Sets peerStatus
   */
  public enum PeerStatusEnum {
    PENDING("PENDING"),
    ACTIVE("ACTIVE"),
    INACTIVE_MANUAL("INACTIVE_MANUAL"),
    INACTIVE_BURNED("INACTIVE_BURNED");

    private final String value;

    PeerStatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PeerStatusEnum fromValue(String text) {
      for (PeerStatusEnum b : PeerStatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("peer_status")
  private PeerStatusEnum peerStatus;

  @JsonProperty("owner_id")
  private Long ownerId;

  @JsonProperty("host")
  private HostResponse host;

  /**
   * Get id
   * @return id
   **/
  @Schema()
  public Long getId() {
    return id;
  }

  /**
   * Get name
   * @return name
   **/
  @Schema()
  public String getName() {
    return name;
  }

  /**
   * Get peerStatus
   * @return peerStatus
   **/
  @Schema()
  public PeerStatusEnum getPeerStatus() {
    return peerStatus;
  }

  /**
   * The id of user who owns this peer
   * @return ownerId
   **/
  @Schema(description = "The id of user who owns this peer")
  public Long getOwnerId() {
    return ownerId;
  }

  /**
   * Get host
   * @return host
   **/
  @Schema()
  @Valid
  public HostResponse getHost() {
    return host;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PeerResponse peerResponse = (PeerResponse) o;
    return Objects.equals(this.id, peerResponse.id)
        && Objects.equals(this.name, peerResponse.name)
        && Objects.equals(this.peerStatus, peerResponse.peerStatus)
        && Objects.equals(this.ownerId, peerResponse.ownerId)
        && Objects.equals(this.host, peerResponse.host);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, peerStatus, ownerId, host);
  }

  @Override
  public String toString() {
      return "class PeerResponse {\n"
              + "    id: " + toIndentedString(id) + "\n"
              + "    name: " + toIndentedString(name) + "\n"
              + "    peerStatus: " + toIndentedString(peerStatus) + "\n"
              + "    ownerId: " + toIndentedString(ownerId) + "\n"
              + "    host: " + toIndentedString(host) + "\n"
              + "}";
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
