package ru.bitniki.cms.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * AddPeerRequest
 */
@Validated
@Setter
public class AddPeerRequest   {
  @JsonProperty("user_id")
  private String name = null;

  @JsonProperty("owner_id")
  private Long ownerId = null;

  @JsonProperty("host_id")
  private Long hostId = null;

  @JsonProperty("user_allowed_active_peers_count")
  private Integer userAllowedActivePeersCount = null;


  /**
   * Get name
   * @return name
   **/
  @Schema()
  public String getName() {
    return name;
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
   * Get hostId
   * @return hostId
   **/
  @Schema()
  public Long getHostId() {
    return hostId;
  }

  /**
   * To check if peer creation is allowed
   * @return userAllowedActivePeersCount
   **/
  @Schema(description = "To check if peer creation is allowed")
  public Integer getUserAllowedActivePeersCount() {
    return userAllowedActivePeersCount;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddPeerRequest addPeerRequest = (AddPeerRequest) o;
    return Objects.equals(this.name, addPeerRequest.name)
        && Objects.equals(this.ownerId, addPeerRequest.ownerId)
        && Objects.equals(this.hostId, addPeerRequest.hostId)
        && Objects.equals(this.userAllowedActivePeersCount, addPeerRequest.userAllowedActivePeersCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, ownerId, hostId, userAllowedActivePeersCount);
  }

  @Override
  public String toString() {
      return "class AddPeerRequest {\n"
              + "    name: " + toIndentedString(name) + "\n"
              + "    ownerId: " + toIndentedString(ownerId) + "\n"
              + "    hostId: " + toIndentedString(hostId) + "\n"
              + "    userAllowedActivePeersCount: " + toIndentedString(userAllowedActivePeersCount) + "\n"
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
