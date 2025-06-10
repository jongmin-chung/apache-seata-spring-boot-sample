package app.tozzi.manager;

import app.tozzi.model.ShippingResult;
import app.tozzi.service.CreditService;
import app.tozzi.service.ShippingService;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@Observed(contextualName = "manager")
public class BFFManager {

    @Autowired
    private CreditService creditService;

    @Autowired
    private ShippingService shippingService;

    @GlobalTransactional
    public ShippingResult buyShipping(Long userID, BigDecimal cost) {
        var wallet = creditService.updateBalance(userID, cost);
        var shipping = shippingService.buyShipping(userID, cost);
        wallet = creditService.getWallet(userID);
        var result = new ShippingResult();
        result.setCost(cost);
        result.setShippingID(shipping.getId());
        result.setCurrentBalance(wallet.getBalance());
        return result;
    }

}
