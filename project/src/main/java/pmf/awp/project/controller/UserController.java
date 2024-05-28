package pmf.awp.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pmf.awp.project.dto.UserDTO;
import pmf.awp.project.model.User;
import pmf.awp.project.service.AuthServise;

@RestController
@RequestMapping("user")
@CrossOrigin(origins = "${cross.allowed-origin}", allowCredentials = "true")
public class UserController {

    @Autowired
    private AuthServise authServise;

    @GetMapping("self")
    public ResponseEntity<?> self() throws RuntimeException {
        User user = authServise.getAuthenticatedUser();
        UserDTO details = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .roles(user.getRoles().stream()
                        .map(r -> r.getName())
                        .toList())
                .build();
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
}
