package pmf.awp.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "${cross.allowed-origin}", allowCredentials = "true")
public class TestController {
    @GetMapping("user/welcome")
    public ResponseEntity<String> userWelcome() {
        return new ResponseEntity<>("Welcome user!", HttpStatus.OK);
    }

    @GetMapping("admin/welcome")
    public ResponseEntity<String> adminWelcome() {
        return new ResponseEntity<>("Welcome admin!", HttpStatus.OK);
    }
}
