package pmf.awp.project.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    private int id;
    private String email;
    private String username;
    private Date createdAt;
    private List<String> roles;
}
