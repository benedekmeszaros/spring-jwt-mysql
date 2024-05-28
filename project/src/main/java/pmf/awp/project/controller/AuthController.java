package pmf.awp.project.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import pmf.awp.project.dto.AuthorityDTO;
import pmf.awp.project.dto.LoginDTO;
import pmf.awp.project.dto.RegisterDTO;
import pmf.awp.project.exception.DuplicateEntryException;
import pmf.awp.project.exception.ExpiredTokenException;
import pmf.awp.project.exception.NoSuchElementException;
import pmf.awp.project.model.RefreshToken;
import pmf.awp.project.model.User;
import pmf.awp.project.security.CustomUserDetail;
import pmf.awp.project.security.CustomUserDetailService;
import pmf.awp.project.security.JWTUtil;
import pmf.awp.project.service.RefreshTokenService;
import pmf.awp.project.service.RoleService;
import pmf.awp.project.service.UserService;

@RestController
@RequestMapping("auth")
@CrossOrigin(origins = "${cross.allowed-origin}", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtil jwt;
    @Autowired
    private CustomUserDetailService userservice;
    @Autowired
    private AuthenticationManager authmgr;

    @PostMapping("register")
    public ResponseEntity<String> registration(@RequestBody @Valid RegisterDTO register) throws RuntimeException {
        if (userService.existsByEmail(register.getEmail()))
            throw new DuplicateEntryException("E-mail address is already taken.");
        else {
            User user = User.builder()
                    .email(register.getEmail())
                    .username(register.getUsername())
                    .password(passwordEncoder.encode(register.getPassword()))
                    .createdAt(new Date())
                    .build();
            user.setRoles(new ArrayList<>());
            user.getRoles().add(roleService.findByName("USER"));
            userService.save(user);
            return new ResponseEntity<>("Registration success!", HttpStatus.OK);
        }
    }

    @PostMapping("login")
    public ResponseEntity<?> login(HttpServletResponse response, @RequestBody @Valid LoginDTO login)
            throws RuntimeException {
        Authentication authentication = null;
        CustomUserDetail userdetails = null;
        try {
            authentication = authmgr
                    .authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
            userdetails = (CustomUserDetail) userservice.loadUserByUsername(login.getEmail());
        } catch (Exception e) {
            throw new NoSuchElementException(e.getMessage());
        }
        String token = jwt.generateToken(userdetails.getUsername());
        List<String> roles = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", "")).toList();
        User user = userdetails.getRawUser();
        refreshTokenService.deleteAllExpired(user.getId());
        String value = user.getId() + new Date().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .value(Base64.getEncoder().encodeToString(value.getBytes()))
                .expireAt(new Date(System.currentTimeMillis() + 24000 * 3600))
                .owner(user)
                .build();
        refreshTokenService.save(refreshToken);
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", refreshToken.getValue())
                .secure(true)
                .httpOnly(true)
                .path("/auth").maxAge(86400)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return new ResponseEntity<>(new AuthorityDTO(token, roles), HttpStatus.OK);
    }

    @GetMapping("logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws RuntimeException {
        if (request.getCookies() == null)
            throw new ExpiredTokenException("Token is already expired.");
        String value = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("refresh-token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new ExpiredTokenException("Token is already expired."));
        RefreshToken refreshToken = refreshTokenService.findByValue(value);
        refreshTokenService.delete(refreshToken);
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                .secure(true)
                .httpOnly(true)
                .path("/auth")
                .maxAge(0)
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return ResponseEntity.ok("Logged out.");
    }

    @GetMapping("refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) throws RuntimeException {
        if (request.getCookies() == null)
            throw new ExpiredTokenException("Token is already expired.");
        String value = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("refresh-token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new ExpiredTokenException("Token is already expired."));
        RefreshToken refreshToken = refreshTokenService.findByValue(value);
        if (refreshToken.getExpireAt().compareTo(new Date()) > 0) {
            User user = refreshToken.getOwner();
            String token = jwt.generateToken(user.getEmail());
            List<String> roles = user.getRoles().stream()
                    .map(r -> r.getName()).toList();
            return new ResponseEntity<AuthorityDTO>(new AuthorityDTO(token, roles), HttpStatus.OK);
        } else {
            refreshTokenService.delete(refreshToken);
            throw new ExpiredTokenException("Token is already expired.");
        }
    }
}
