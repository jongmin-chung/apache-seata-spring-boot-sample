package app.tozzi.controller.model;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BuyShippingInput {

    @NotNull
    @Digits(integer = 15, fraction = 2)
    private BigDecimal cost;

}
