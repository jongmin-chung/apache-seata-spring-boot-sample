package app.tozzi.manager;

import app.tozzi.model.Shipping;
import app.tozzi.repository.ShippingRepository;
import app.tozzi.repository.entity.ShippingEntity;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Observed(contextualName = "manager")
public class ShippingManager {

    @Autowired
    private ShippingRepository shippingRepository;

    @Transactional
    public Shipping buyShipping(Long userID, BigDecimal cost) {

        if (ThreadLocalRandom.current().nextBoolean()) {
            throw new RuntimeException("Good luck with the transaction!");
        }

        var entity = new ShippingEntity();
        entity.setCost(cost);
        entity.setUserId(userID);
        entity.setCreatedAt(new Date());
        entity = shippingRepository.save(entity);
        return new Shipping(entity.getId());
    }

}
