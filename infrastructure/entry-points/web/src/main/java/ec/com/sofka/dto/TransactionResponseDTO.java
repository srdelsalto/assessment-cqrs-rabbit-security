package ec.com.sofka.dto;

import ec.com.sofka.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class TransactionResponseDTO {
    private String operationId;
    private BigDecimal fee;
    private BigDecimal finalAmount;
    private TransactionType type;
    private LocalDateTime timestamp;
    private String customerId;

    public TransactionResponseDTO(String operationId, BigDecimal fee, BigDecimal finalAmount, TransactionType type, LocalDateTime timestamp, String customerId) {
        this.operationId = operationId;
        this.fee = fee;
        this.finalAmount = finalAmount;
        this.type = type;
        this.timestamp = timestamp;
        this.customerId = customerId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}