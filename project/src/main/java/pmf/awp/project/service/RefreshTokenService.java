package pmf.awp.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pmf.awp.project.exception.NoSuchElementException;
import pmf.awp.project.exception.PersistenceException;
import pmf.awp.project.model.RefreshToken;
import pmf.awp.project.model.User;
import pmf.awp.project.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByValue(String value) throws RuntimeException {
        return refreshTokenRepository.findByValue(value)
                .orElseThrow(() -> new NoSuchElementException("Invalid refresh token."));
    }

    public boolean existsByOwner(User owner) {
        return refreshTokenRepository.existsByOwner(owner);
    }

    public boolean existsByValue(String value) {
        return refreshTokenRepository.existsByValue(value);
    }

    public int save(RefreshToken refreshToken) throws RuntimeException {
        try {
            return refreshTokenRepository.save(refreshToken).getId();
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    public boolean delete(RefreshToken refreshToken) throws RuntimeException {
        try {
            refreshTokenRepository.delete(refreshToken);
            return true;
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    public void deleteAllExpired(int userId) throws RuntimeException {
        try {
            refreshTokenRepository.deleteAll(refreshTokenRepository.findAllExpired(userId));
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }
}
