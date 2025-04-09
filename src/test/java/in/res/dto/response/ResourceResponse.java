package in.res.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResourceResponse(Integer id,
                               String name,
                               Integer year,
                               String color,
                               @JsonProperty(value = "pantone_value")
                               String pantoneValue
) {
}
