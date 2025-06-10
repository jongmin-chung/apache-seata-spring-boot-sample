package app.tozzi.repository;

import app.tozzi.repository.entity.CreditWalletsEntity;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Observed(contextualName = "w-repository")
public interface CreditWalletsRepository extends JpaRepository<CreditWalletsEntity, Long> {

    Optional<CreditWalletsEntity> findByUserId(Long userId);

}
