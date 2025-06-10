package app.tozzi.service;

import app.tozzi.service.model.CreditTransactionType;
import app.tozzi.service.model.Wallet;
import app.tozzi.service.model.WalletBalanceInput;
import io.micrometer.observation.annotation.Observed;
import org.apache.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class CreditService {

    @Value("${credit.url}")
    private String baseURL;

    @Observed(contextualName = "credit-update-service")
    public Wallet updateBalance(Long userID, BigDecimal cost) {

        var request = new WalletBalanceInput();
        request.setAmount(cost);
        request.setTransactionType(CreditTransactionType.DEBIT);

        return RestClient.builder().baseUrl(baseURL).build().post()
                .uri(uriBuilder -> uriBuilder.path("/{id}").build(userID))
                .contentType(MediaType.APPLICATION_JSON)
                .header(RootContext.KEY_XID, RootContext.getXID())
                .body(request)
                .retrieve()
                .body(Wallet.class);
    }

    @Observed(contextualName = "credit-get-service")
    public Wallet getWallet(Long userID) {

        return RestClient.builder().baseUrl(baseURL).build().get()
                .uri(uriBuilder -> uriBuilder.path("/{id}").build(userID))
                .retrieve()
                .body(Wallet.class);
    }

}
