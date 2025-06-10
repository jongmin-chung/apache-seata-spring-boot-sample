package app.tozzi.service;

import app.tozzi.service.model.Shipping;
import app.tozzi.service.model.ShippingInput;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class ShippingService {

    @Value("${shipping.url}")
    private String baseURL;

    @Observed(contextualName = "shipping-service")
    public Shipping buyShipping(Long userID, BigDecimal cost) {

        var request = new ShippingInput();
        request.setCost(cost);

        return RestClient.builder().baseUrl(baseURL).build().post()
                .uri(uriBuilder -> uriBuilder.path("/{id}").build(userID))
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(Shipping.class);
    }

}
