package app.tozzi.controller.model;

import app.tozzi.model.CreditTransactionType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletBalanceInput {

    @NotNull
    @Digits(integer = 15, fraction = 2)
    private BigDecimal amount;

    @NotNull
    private CreditTransactionType transactionType;
}
