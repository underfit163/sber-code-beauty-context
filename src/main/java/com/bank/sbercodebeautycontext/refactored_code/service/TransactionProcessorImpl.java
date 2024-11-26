
package com.bank.sbercodebeautycontext.refactored_code.service;

import com.bank.sbercodebeautycontext.refactored_code.entity.Status;
import com.bank.sbercodebeautycontext.refactored_code.utils.Logger;
import com.bank.sbercodebeautycontext.refactored_code.repository.TransactionRepository;
import com.bank.sbercodebeautycontext.refactored_code.entity.Transaction;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;

@Service
public class TransactionProcessorImpl implements TransactionProcessor {

    private final TransactionRepository repository;
    private final Logger logger;
    private final Validator validator;

    @Autowired
    public TransactionProcessorImpl(TransactionRepository repository, Logger logger, Validator validator) {
        this.repository = repository;
        this.logger = logger;
        this.validator = validator;
    }

    @Override
    public void processTransactions(List<Transaction> transactions) {
        //Предлагаю сначала сохранять в базу и из нее обрабатывать(если у нас упадет по, то мы потеряем транзакции, а так не потеряем)
        //Возможно нужно разбить на пачки, потому что вызовов метода мб много и мы можем забить пул Executor, как с этим бороться?
        if (transactions != null) {
            transactions.forEach(this::processTransaction);
        }
    }

    /**
     * Метод обработки транзакции в отдельном потоке
     *
     * @param transaction транзакция для обработки
     */
    @Async("transactionExecutor")
    @Transactional
    protected void processTransaction(Transaction transaction) {
        try {
            if (!validateTransaction(transaction)) {
                logger.warn("Transaction validation failed {}", transaction);
                return;
            }
            if (!isPending(transaction)) {
                logger.warn("Transaction was processed {}", transaction);
                return;
            }
            //Что делать с дулями транзакций? Как при сохранении транзакции с cуществующим id в базе выбрасывать исключение?

            updateTransactionStatus(transaction, Status.PROCESSED);
            //При сохранении в потоках мы не забьем пулы бд, как с этим бороться и мб нам сохранять обработанные транзакии пачкой в бд?
            repository.save(transaction);
            logger.info("Transaction success processed {}", transaction);
        } catch (RejectedExecutionException e) {
            logger.error("Error full queue task for transaction {} - {}", transaction, e);
            //Как обработать забитее очереди задач Executor
        } catch (Exception e) {
            logger.error("Error processing transaction {} - {}", transaction, e);
            // Нужно добавить механизм повтора обработки транзакции
        }
    }

    private Boolean validateTransaction(Transaction transaction) {
        // Выполнить валидацию
        Set<ConstraintViolation<Transaction>> violations = validator.validate(transaction);
        // Обработка ошибок валидации
        violations.forEach(violation -> logger.error(violation.getMessage() + " {}", transaction));
        return violations.isEmpty();
    }

    private boolean isPending(Transaction transaction) {
        return Status.PENDING.equals(transaction.getStatus());
    }

    private void updateTransactionStatus(Transaction transaction, Status status) {
        transaction.setStatus(status);
    }
}