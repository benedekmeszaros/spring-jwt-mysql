package pmf.awp.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pmf.awp.project.model.RefreshToken;
import pmf.awp.project.model.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByValue(String value);

    boolean existsByOwner(User owner);

    boolean existsByValue(String value);

    @Query("select rt from RefreshToken rt where rt.owner.id = :userId and rt.expireAt <= now()")
    List<RefreshToken> findAllExpired(Integer userId);
}
