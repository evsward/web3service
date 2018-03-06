package com.web3.scph.service;

import com.web3.scph.client.Web3JClient;
import com.web3.scph.mapper.TransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.exceptions.MessageDecodingException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;

import java.io.IOException;
import java.util.List;

@Component
public class Web3Service {
    @Autowired
    private TransactionMapper transactionMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    public EthTransaction getTranByHash(String tranHash) {
        try {
            Web3j web3j = Web3JClient.getClient();
            EthTransaction ethTransaction = web3j.ethGetTransactionByHash(tranHash).send();
            Transaction transaction = null;
            try {
                transaction = ethTransaction.getResult();
                transaction.getBlockNumber();
            } catch (MessageDecodingException e) {
                logger.warn("有正在进行中的交易: " + transaction.getHash());
                transaction.setBlockNumber("0x0");
                ethTransaction.setResult(transaction);
                return ethTransaction;
            } catch (NullPointerException e1) {
                logger.error("未获取到任何交易信息，交易hash或许有误：" + tranHash);
            }
            return ethTransaction;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public List<com.web3.scph.po.Transaction> findAllTransList() {
        List<com.web3.scph.po.Transaction> list = transactionMapper.findAllTransList();
        return list;
    }

    public long findTotalTransNum() {
        return transactionMapper.findTotalTransNum();
    }

    //------------------------------------------------------------------------------------------------------------------
    public List<com.web3.scph.po.Transaction> findSBTransList(String account) {
        List<com.web3.scph.po.Transaction> list = transactionMapper.findSBTransList(account);
        return list;
    }

    public long findSBTransListNum(String account) {
        return transactionMapper.findSBTransListNum(account);
    }

    //------------------------------------------------------------------------------------------------------------------
    public List<com.web3.scph.po.Transaction> findSBTokenTransList(String account, String token) {
        List<com.web3.scph.po.Transaction> list = transactionMapper.findSBTokenTransList(account, token);
        //TODO: add pending list here
        return list;
    }

    public long findSBTokenTransListNum(String account, String token) {
        return transactionMapper.findSBTokenTransListNum(account, token);
    }

    public long findLatestBlockNumber() {
        Web3j web3j = new Web3JClient().getClient();
        long currentAdvancedNum = 0;
        try {
            currentAdvancedNum = web3j.ethBlockNumber().send().getBlockNumber().longValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("当前最高区块：" + currentAdvancedNum);
        return currentAdvancedNum;
    }

}
