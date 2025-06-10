package app.tozzi.repository;

import app.tozzi.repository.entity.ShippingEntity;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Observed(contextualName = "repository")
public interface ShippingRepository extends JpaRepository<ShippingEntity, Long> {
}
