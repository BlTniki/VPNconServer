package ru.bitniki.cms.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * UpdatePeerRequest
 */
@Validated
@Setter
public class UpdatePeerRequest   {
  @JsonProperty("name")
  private String name = null;

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
  private PeerStatusEnum peerStatus = null;

  @JsonProperty("owner_id")
  private Long ownerId = null;

  @JsonProperty("host_id")
  private Long hostId = null;

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
   * The id of user who owns this peer
   * @return hostId
   **/
  @Schema(description = "The id of user who owns this peer")
  public Long getHostId() {
    return hostId;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdatePeerRequest updatePeerRequest = (UpdatePeerRequest) o;
    return Objects.equals(this.name, updatePeerRequest.name)
        && Objects.equals(this.peerStatus, updatePeerRequest.peerStatus)
        && Objects.equals(this.ownerId, updatePeerRequest.ownerId)
        && Objects.equals(this.hostId, updatePeerRequest.hostId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, peerStatus, ownerId, hostId);
  }

  @Override
  public String toString() {
      return "class UpdatePeerRequest {\n"
              + "    name: " + toIndentedString(name) + "\n"
              + "    peerStatus: " + toIndentedString(peerStatus) + "\n"
              + "    ownerId: " + toIndentedString(ownerId) + "\n"
              + "    hostId: " + toIndentedString(hostId) + "\n"
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
