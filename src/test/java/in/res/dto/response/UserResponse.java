package in.res.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(Integer id,
                           String email,
                           @JsonProperty(value = "first_name")
                           String firstName,
                           @JsonProperty(value = "last_name")
                           String lastName,
                           String avatar
) {
}
