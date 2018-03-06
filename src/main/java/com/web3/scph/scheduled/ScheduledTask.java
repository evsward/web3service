package com.web3.scph.scheduled;

import com.web3.scph.Application;
import com.web3.scph.client.Web3JClient;
import com.web3.scph.mapper.TransactionMapper;
import com.web3.scph.service.Web3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthTransaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Component
public class ScheduledTask {
    Logger logger = LoggerFactory.getLogger(getClass());

    public final static long ONE_Second = 1000;
    public final static long ONE_Minute = 60 * ONE_Second;
    public final static long ONE_WEEK = ONE_Minute * 60 * 60 * 24 * 7;

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private Web3Service web3Service;

    @Scheduled(fixedRate = ONE_WEEK)
    public void initCurrentBehindNum() {
        Application.currentBehindNum = transactionMapper.findLatestBlockNumber();
    }

    @Scheduled(fixedRate = ONE_Second * 10)
    public void syncBlockTask() throws Exception {
        long startTime = System.currentTimeMillis();
        Web3j web3j = new Web3JClient().getClient();
        long currentAdvancedNum = web3j.ethBlockNumber().send().getBlockNumber().longValue();
        logger.info("当前最高区块：" + currentAdvancedNum);
        logger.info("最高同步区块：" + Application.currentBehindNum);
        for (long i = Application.currentBehindNum + 1; i <= currentAdvancedNum; i++) {// 从数据库当前区块的下一个区块开始循环
            if (i == currentAdvancedNum) {
                Application.currentBehindNum = i;
                logger.info("批量同步最后一个区块" + Application.currentBehindNum);
            }
            EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(i)), false).send();
            EthBlock.Block block = ethBlock.getResult();
            List<EthBlock.TransactionResult> tranList = ethBlock.getBlock().getTransactions();
            if (tranList.size() == 0)
                continue;
            logger.info("当前区块交易数量：" + tranList.size());
            for (EthBlock.TransactionResult tran : tranList) {
                String transactionHash = (String) tran.get();
                long transLatest = transactionMapper.findTranHashExist(transactionHash);
                if (transLatest == 1)
                    continue;
                EthTransaction ethTransaction = web3Service.getTranByHash(transactionHash);
                org.web3j.protocol.core.methods.response.Transaction transaction = ethTransaction.getResult();
                logger.info("blockNumber:" + i);
                logger.info("blockHash:" + block.getHash());
                logger.info("blockTimestamp:" + block.getTimestamp());
                logger.info("tranHash:" + transactionHash);
                logger.info("fromAccount:" + transaction.getFrom());
                logger.info("toAccount:" + transaction.getTo());
                logger.info("value:" + transaction.getValue());
                logger.info("gasPrice:" + transaction.getGasPrice());
                logger.info("gas:" + transaction.getGas());
                logger.info("nonce:" + transaction.getNonce());
                transactionMapper.insertTran(i, block.getHash(), transactionHash, block.getTimestamp().longValue(), transaction.getFrom(), transaction.getTo(), transaction.getValue().doubleValue(), transaction.getGasPrice().doubleValue(), transaction.getGas().longValue(), transaction.getNonce().longValue());
            }
        }
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        logger.info("区块同步完毕，共耗时：" + costTime / 1000 + "s");
    }

    @Scheduled(fixedRate = ONE_Second * 10)
    public void syncPendingTransaction() {
        String pTranHash = "";
        try {
            Web3j web3j = Web3JClient.getClient();
            EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.PENDING, false).send();
            List<EthBlock.TransactionResult> pendingList = ethBlock.getResult().getTransactions();
            for (EthBlock.TransactionResult<String> pendingTran : pendingList) {
                pTranHash = pendingTran.get();
                EthTransaction pTran = web3Service.getTranByHash(pTranHash);
                org.web3j.protocol.core.methods.response.Transaction transaction = pTran.getResult();
                logger.info("同步正在进行的交易到数据库：" + pTranHash);
                long pendingTimeStamp = System.currentTimeMillis();
                logger.info("pendingTimeStamp: " + pendingTimeStamp);
                transactionMapper.insertPendingTran(0, "", pTranHash, pendingTimeStamp, transaction.getFrom(), transaction.getTo(), transaction.getValue().doubleValue(), transaction.getGasPrice().doubleValue(), transaction.getGas().longValue(), transaction.getNonce().longValue());
            }
        } catch (DuplicateKeyException e) {
            logger.warn("交易正在进行中：" + pTranHash);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = ONE_Second * 10)
    public void deletePendingTransaction() {
        // 删除交易库中已存在的交易hash相等的pending交易
        List<String> list = transactionMapper.selectPendingTransHash();
        for (String tranHash : list) {
            long transLatest = transactionMapper.findTranHashExist(tranHash);
            if (transLatest == 1) {
                //delete
                logger.info("交易：" + tranHash + " == 交易已完成");
                transactionMapper.deletePendingTranByHash(tranHash);
            }
        }
    }
}
