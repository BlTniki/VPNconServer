package ru.bitniki.cms.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * ErrorResponse
 */
@Validated
@Setter
@AllArgsConstructor
public class ErrorResponse   {
  @JsonProperty("code")
  private Integer code;

  @JsonProperty("description")
  private String description;

  @JsonProperty("exceptionName")
  private String exceptionName;

  @JsonProperty("exceptionMessage")
  private String exceptionMessage;

  @JsonProperty("stacktrace")
  @Valid
  private List<String> stacktrace;

  /**
   * Get code
   * @return code
   **/
  @Schema()
  @NotNull
  public Integer getCode() {
    return code;
  }

  /**
   * Get description
   * @return description
   **/
  @Schema()
  @NotNull
  public String getDescription() {
    return description;
  }

  /**
   * Get exceptionName
   * @return exceptionName
   **/
  @Schema()
  @NotNull
  public String getExceptionName() {
    return exceptionName;
  }

  /**
   * Get exceptionMessage
   * @return exceptionMessage
   **/
  @Schema()
  @NotNull
  public String getExceptionMessage() {
    return exceptionMessage;
  }

  public ErrorResponse addStacktraceItem(String stacktraceItem) {
    if (this.stacktrace == null) {
      this.stacktrace = new ArrayList<>();
    }
    this.stacktrace.add(stacktraceItem);
    return this;
  }

  /**
   * Get stacktrace
   * @return stacktrace
   **/
  @Schema()
  @NotNull
  public List<String> getStacktrace() {
    return stacktrace;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorResponse errorResponse = (ErrorResponse) o;
    return Objects.equals(this.code, errorResponse.code)
            && Objects.equals(this.description, errorResponse.description)
            && Objects.equals(this.exceptionName, errorResponse.exceptionName)
            && Objects.equals(this.exceptionMessage, errorResponse.exceptionMessage)
            && Objects.equals(this.stacktrace, errorResponse.stacktrace);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, description, exceptionName, exceptionMessage, stacktrace);
  }

  @Override
  public String toString() {
    return "class ErrorResponse {\n"
            + "    code: " + toIndentedString(code) + "\n"
            + "    description: " + toIndentedString(description) + "\n"
            + "    exceptionName: " + toIndentedString(exceptionName) + "\n"
            + "    exceptionMessage: " + toIndentedString(exceptionMessage) + "\n"
            + "    stacktrace: " + toIndentedString(stacktrace) + "\n"
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
