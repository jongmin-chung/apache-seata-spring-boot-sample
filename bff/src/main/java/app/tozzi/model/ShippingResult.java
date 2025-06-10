package app.tozzi.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShippingResult {

    private Long shippingID;
    private BigDecimal cost;
    private BigDecimal currentBalance;

}
