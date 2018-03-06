package com.web3.scph.test;

import com.web3.scph.client.Web3JClient;
import org.web3j.protocol.Web3j;

import java.io.IOException;
import java.math.BigInteger;

public class Web3Test {

    public void testSendTransaction() {
        Web3j web3j = new Web3JClient().getClient();
        long a = 1l;
        org.web3j.protocol.core.methods.request.Transaction transaction = new org.web3j.protocol.core.methods.request.Transaction("", new BigInteger(""), new BigInteger(""), new BigInteger(""), "", new BigInteger(""), "");
        try {
            web3j.ethSendTransaction(transaction).send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
