package pmf.awp.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterDTO {
    @NotBlank
    @Pattern(regexp = "^[a-z](?!.*\\.\\.)[a-z0-9\\.]{2,}@[a-z0-9\\.]{2,}\\.[a-z]{2,3}$")
    private String email;
    @NotBlank
    @Pattern(regexp = "^[\\w\\d\\.]{3,24}$")
    private String username;
    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9]+)(?=.*[A-Z]+).{8,64}$")
    private String password;
}
