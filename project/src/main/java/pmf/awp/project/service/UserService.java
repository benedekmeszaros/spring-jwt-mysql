package pmf.awp.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.PersistenceException;
import pmf.awp.project.model.User;
import pmf.awp.project.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public int save(User user) throws RuntimeException {
        try {
            return userRepository.save(user).getId();
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }
}
