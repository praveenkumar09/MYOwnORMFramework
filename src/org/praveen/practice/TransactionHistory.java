package org.praveen.practice;

import org.praveen.annoatation.Column;
import org.praveen.annoatation.Entity;
import org.praveen.annoatation.PrimaryKey;

@Entity(tableName = "dpl_transactionHistory")
public class TransactionHistory {

    @PrimaryKey(columnName = "transactionId")
    private long transactionId;

    @Column(columnName = "accountNo")
    private int accountNumber;

    @Column(columnName = "name")
    private String name;

    @Column(columnName = "transType")
    private String transactionType;

    @Column(columnName = "amount")
    private int amount;

    public TransactionHistory(int accountNumber, String name, String transactionType, int amount) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public TransactionHistory() {
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransactionHistory{" +
                "transactionId=" + transactionId +
                ", accountNumber=" + accountNumber +
                ", name='" + name + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                '}';
    }
}
