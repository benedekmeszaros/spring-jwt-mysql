package pmf.awp.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pmf.awp.project.exception.NoSuchElementException;
import pmf.awp.project.exception.UnauthorizedException;
import pmf.awp.project.model.User;
import pmf.awp.project.repository.UserRepository;

@Service
public class AuthServise {
    @Autowired
    private UserRepository userRepository;

    public User getAuthenticatedUser() throws RuntimeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
            return userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new NoSuchElementException("Invalid e-mail address."));
        else
            throw new UnauthorizedException("Bad credentials.");
    }
}
