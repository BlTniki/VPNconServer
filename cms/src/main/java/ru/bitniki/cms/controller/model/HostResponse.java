package ru.bitniki.cms.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.Builder;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * HostResponse
 */
@Validated
@Setter
@Builder
public class HostResponse   {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("name")
  private String name;

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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HostResponse hostResponse = (HostResponse) o;
    return Objects.equals(this.id, hostResponse.id)
            && Objects.equals(this.name, hostResponse.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
      return "class HostResponse {\n"
              + "    id: " + toIndentedString(id) + "\n"
              + "    name: " + toIndentedString(name) + "\n"
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
