package com.web3.scph.po;

public class Transaction {
    private int id;
    private long blockNumer;
    private String blockHash;
    private String blockTimestamp;
    private String transactionHash;
    private String fromAccount;
    private String toAccount;
    private double transValue;
    private double tranGasPrice;
    private int tranGasAmount;
    private int tranNonce;

    public double getTransValue() {
        return transValue;
    }

    public void setTransValue(double transValue) {
        this.transValue = transValue;
    }

    public double getTranGasPrice() {
        return tranGasPrice;
    }

    public void setTranGasPrice(double tranGasPrice) {
        this.tranGasPrice = tranGasPrice;
    }

    public int getTranGasAmount() {
        return tranGasAmount;
    }

    public void setTranGasAmount(int tranGasAmount) {
        this.tranGasAmount = tranGasAmount;
    }

    public int getTranNonce() {
        return tranNonce;
    }

    public void setTranNonce(int tranNonce) {
        this.tranNonce = tranNonce;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBlockNumer(long blockNumer) {
        this.blockNumer = blockNumer;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public void setBlockTimestamp(String blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public int getId() {
        return id;
    }

    public long getBlockNumer() {
        return blockNumer;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public String getBlockTimestamp() {
        return blockTimestamp;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getToAccount() {

        return toAccount;
    }
}
