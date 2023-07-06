package proj.bbs.user.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpUserDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Min(4)
    @Max(20)
    private String password;

    @NotBlank
    @Min(4)
    @Max(10)
    private String nickname;
}
