package app.tozzi.controller;

import app.tozzi.controller.model.WalletBalanceInput;
import app.tozzi.manager.CreditManager;
import app.tozzi.model.Wallet;
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
@RequestMapping("/credit/wallets")
@Observed(name = "credit", contextualName = "controller")
@Validated
public class CreditController {

    @Autowired
    private CreditManager creditManager;

    @Operation(
            operationId = "updateBalance",
            summary = "Wallet balance",
            description = "Update wallet balance",
            tags = {"Credit"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Wallet", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Wallet.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "404", description = "Wallet or user not found / Endpoint does not exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "405", description = "Method not allowed", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PostMapping(path = "/{userID}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Wallet updateBalance(@Pattern(regexp = "\\d+") @PathVariable String userID, @RequestBody @Valid WalletBalanceInput request) {
        return creditManager.updateBalance(Long.valueOf(userID), request.getAmount(), request.getTransactionType());
    }

    @Operation(
            operationId = "getUserWallet",
            summary = "User wallet",
            description = "Load user wallet",
            tags = {"Credit"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Wallet", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Wallet.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "404", description = "Wallet or user not found / Endpoint does not exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "405", description = "Method not allowed", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @GetMapping(path = "/{userID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Wallet getUserWallet(@Pattern(regexp = "\\d+") @PathVariable String userID) {
        return creditManager.getWallet(Long.valueOf(userID));
    }

}
