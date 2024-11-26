package com.bank.sbercodebeautycontext.refactored_code.service;

import com.bank.sbercodebeautycontext.refactored_code.entity.Transaction;

import java.util.List;

/**
 * Сервис обработки транзакций
 */
public interface TransactionProcessor {

    /**
     * Метод обработки списка транзакций
     * @param transactions список транзакций на обработку
     */
    void processTransactions(List<Transaction> transactions);
}
