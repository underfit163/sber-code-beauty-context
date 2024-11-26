package com.bank.sbercodebeautycontext.refactored_code.repository;

import com.bank.sbercodebeautycontext.refactored_code.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий доступа к данным транзакций
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
}