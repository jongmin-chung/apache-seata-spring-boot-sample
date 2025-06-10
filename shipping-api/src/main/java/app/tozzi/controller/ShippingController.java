package app.tozzi.controller;

import app.tozzi.controller.model.ShippingInput;
import app.tozzi.manager.ShippingManager;
import app.tozzi.model.Shipping;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipping")
@Observed(name = "shipping", contextualName = "controller")
@Validated
public class ShippingController {

    @Autowired
    private ShippingManager shippingManager;

    @Operation(
            operationId = "buyShipping",
            summary = "Buy shipping",
            description = "Buy shipping",
            tags = {"Shipping"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Shipping", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Shipping.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "404", description = "Endpoint does not exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "405", description = "Method not allowed", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PostMapping(path = "/{userID}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Shipping buyShipping(@Pattern(regexp = "\\d+") @PathVariable String userID, @RequestBody @Valid ShippingInput request) {
        return shippingManager.buyShipping(Long.valueOf(userID), request.getCost());
    }



}
