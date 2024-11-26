
package com.bank.sbercodebeautycontext.refactored_code;

import com.bank.sbercodebeautycontext.refactored_code.entity.Status;
import com.bank.sbercodebeautycontext.refactored_code.entity.Transaction;
import com.bank.sbercodebeautycontext.refactored_code.repository.TransactionRepository;
import com.bank.sbercodebeautycontext.refactored_code.service.TransactionProcessor;
import com.bank.sbercodebeautycontext.refactored_code.service.TransactionProcessorImpl;
import com.bank.sbercodebeautycontext.refactored_code.utils.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Сервис тестирования работы системы(юнит-тесты)
 */
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransactionProcessorTest {

    @Autowired
    private TransactionProcessor processor;

    @Autowired
    private TransactionRepository repository;

    @MockBean
    private Logger logger;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void testProcessTransactions_Success() {
        Transaction t1 = new Transaction("1", 5000.0, LocalDate.of(2023, 1, 1), Status.PENDING);
        Transaction t2 = new Transaction("2", 15000.0, LocalDate.of(2023, 1, 2), Status.PENDING);
        Transaction t3 = new Transaction("3", 2000.0, LocalDate.of(2023, 1, 3), Status.PENDING);

        List<Transaction> transactions = Arrays.asList(t1, t2, t3);

        processor.processTransactions(transactions);

        Transaction updatedT1 = repository.findById("1").orElse(null);
        Transaction updatedT2 = repository.findById("2").orElse(null);
        Transaction updatedT3 = repository.findById("3").orElse(null);

        assertNotNull(updatedT1);
        assertEquals("PROCESSED", updatedT1.getStatus());

        assertNotNull(updatedT2);
        assertEquals("PROCESSED", updatedT2.getStatus());

        assertNotNull(updatedT3);
        assertEquals("COMPLETED", updatedT3.getStatus());

        Mockito.verify(logger, Mockito.times(2)).info(Mockito.anyString());
    }

    @Test
    public void testProcessTransaction_WithException() {
        Transaction t1 = new Transaction("4", 7000.0, LocalDate.of(2023, 1, 4), Status.PENDING);

        // Имитация исключения при сохранении транзакции
        Mockito.doThrow(new RuntimeException("Database error"))
                .when(repository).save(Mockito.any(Transaction.class));

        List<Transaction> transactions = List.of(t1);

        processor.processTransactions(transactions);

        Transaction updatedT1 = repository.findById("4").orElse(null);
        assertNotNull(updatedT1);
        assertEquals("PROCESSED", updatedT1.getStatus());

        Mockito.verify(logger).error(Mockito.contains("Error processing transaction"), Optional.ofNullable(Mockito.any()));
    }

    @Test
    public void testProcessTransactions_EmptyList() {
        List<Transaction> transactions = List.of();

        processor.processTransactions(transactions);

        Mockito.verifyNoInteractions(logger);
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    public void testProcessTransactions_NullValues() {
        Transaction t1 = new Transaction("5", 0.0, null, Status.PENDING);

        List<Transaction> transactions = List.of(t1);

        processor.processTransactions(transactions);

        Transaction updatedT1 = repository.findById("5").orElse(null);
        assertNotNull(updatedT1);
        assertEquals("PROCESSED", updatedT1.getStatus());

        Mockito.verify(logger).info(Mockito.anyString());
    }

    @Test
    public void testProcessTransactions_Performance() {
        // Создание большого количества транзакций
        int numTransactions = 10000;
        List<Transaction> transactions = Arrays.asList(new Transaction[numTransactions]);
        for (int i = 0; i < numTransactions; i++) {
            transactions.set(i, new Transaction(String.valueOf(i), i * 10.0, LocalDate.now(), Status.PENDING));
        }

        long startTime = System.currentTimeMillis();
        processor.processTransactions(transactions);
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;
        System.out.println("Processing " + numTransactions + " transactions took " + duration + " ms");

        // Ожидаем, что обработка не займет более определенного времени (например, 5000 ms)
        assertTrue(duration < 5000, "Processing took too long");
    }
}
