
package com.bank.sbercodebeautycontext.refactored_code.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Модель данных транзакции
 */
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @NotBlank(message = "Field 'id' cannot be empty!")
    private String id;

    @NotNull(message = "Field 'amount' cannot be empty!")
    @Max(value = 10000, message = "Amount must not be greater than 10000!")
    @Digits(integer = 19, fraction = 2, message = "Amount must have up to 19 digits in the integer part and 2 digits in the fractional part!")
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull(message = "Field 'date' cannot be empty!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull(message = "Field 'status' cannot be empty!")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public Transaction() {
    }

    public Transaction(String id, Double amount, LocalDate date, Status status) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Transaction{");
        sb.append("id=").append(id);
        sb.append(", amount=").append(amount);
        sb.append(", date=").append(date);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
