package app.tozzi.manager;

import app.tozzi.model.CreditTransactionType;
import app.tozzi.model.Wallet;
import app.tozzi.repository.CreditTransactionsRepository;
import app.tozzi.repository.CreditWalletsRepository;
import app.tozzi.repository.entity.CreditTransactionsEntity;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
@Observed(contextualName = "manager")
public class CreditManager {

    @Autowired
    private CreditTransactionsRepository creditTransactionsRepository;

    @Autowired
    private CreditWalletsRepository creditWalletsRepository;

    @Transactional
    public Wallet updateBalance(Long userID, BigDecimal amount, CreditTransactionType transactionType) {

        var wallet = creditWalletsRepository.findByUserId(userID)
                .orElseThrow(() -> new NoSuchElementException("User or wallet not found"));

        if (CreditTransactionType.DEBIT.equals(transactionType) && wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        var transaction = new CreditTransactionsEntity();
        transaction.setAmount(amount);
        transaction.setType(transactionType);
        transaction.setCreatedAt(new Date());
        transaction.setUserId(userID);
        creditTransactionsRepository.save(transaction);

        switch (transactionType) {
            case DEBIT -> wallet.setBalance(wallet.getBalance().subtract(amount));
            case REFUND -> wallet.setBalance(wallet.getBalance().add(amount));
        }

        creditWalletsRepository.save(wallet);
        return new Wallet(wallet.getBalance());
    }

    public Wallet getWallet(Long userID) {
        return new Wallet(creditWalletsRepository.findByUserId(userID)
                .orElseThrow(() -> new NoSuchElementException("User or wallet not found")).getBalance());
    }

}
