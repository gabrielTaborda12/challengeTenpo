package com.tenpo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tenpo.utils.RegexConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatUserRequestDTO {

    @Valid
    @NotNull
    private String userName;

    @Valid
    @JsonProperty("name")
    @Pattern(
            regexp = RegexConstants.REGEX_VALIDATE_ONE_OR_MORE_LETTER,
            message = "{tenpo.validate.word}")
    @NotBlank(message = "{tenpo.validation.parameters.invalid.user.name}")
    @NotEmpty(message = "{tenpo.validation.parameters.invalid.user.name}")
    private String name;

    @Valid
    @JsonProperty("lastName")
    @Pattern(
            regexp = RegexConstants.REGEX_VALIDATE_ONE_OR_MORE_LETTER,
            message = "{tenpo.validate.word}")
    @NotBlank(message = "{tenpo.validation.parameters.invalid.user.name}")
    @NotEmpty(message = "{tenpo.validation.parameters.invalid.user.name}")
    private String lastName;

    @Valid
    @JsonProperty("email")
    @NotBlank(message = "{tenpo.validation.parameters.invalid.email}")
    @NotEmpty(message = "{tenpo.validation.parameters.invalid.email}")
    private String email;

    private String password;

}
