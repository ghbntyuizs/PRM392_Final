package com.example.project_prm392.entities;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String hash;
    private String previousHash;
    private String from;
    private String to;
    private String category;
    private int amount;
    private String time;
    private String transactionKey;

    public Transaction() {
    }

    public Transaction(String transactionKey, String from, String to, String category, int amount, String time, String previousHash) {
        this.transactionKey = transactionKey;
        this.from = from;
        this.to = to;
        this.category = category;
        this.amount = amount;
        this.time = time;
        this.hash = calculateHash();
        this.previousHash = previousHash;
    }

    public String getHash() {
        return hash;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getAmount() {
        return amount;
    }

    public String getTime() {
        return time;
    }

    public String getCategory() {
        return category;
    }

    public String getTransactionKey() {
        return transactionKey;
    }

    public String calculateHash() {
        return "";
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", time='" + time + '\'' +
                ", transactionKey='" + transactionKey + '\'' +
                '}';
    }
}
