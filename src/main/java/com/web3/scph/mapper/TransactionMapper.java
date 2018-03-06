package com.web3.scph.mapper;

import com.web3.scph.po.Transaction;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TransactionMapper {
    String unionColumn = "id,block_number,block_hash,transaction_hash,block_timestamp,from_account,to_account,tran_value,tran_gas_price,tran_gas_amount,tran_nonce";
    String unionWithoutIDColumn = "block_number,block_hash,transaction_hash,block_timestamp,from_account,to_account,tran_value,tran_gas_price,tran_gas_amount,tran_nonce";
    String unionSubPendingSql = "(SELECT " + unionColumn + " FROM ethereum_transactions UNION ALL SELECT " + unionColumn + " FROM pending_transactions order by block_timestamp desc) as utable";
    String unionPendingSql = "SELECT " + unionColumn + " FROM ethereum_transactions UNION ALL SELECT " + unionColumn + " FROM pending_transactions order by block_timestamp desc";

    @Select("SELECT count(*) FROM ethereum_transactions where transaction_hash=#{transactionHash}")
    long findTranHashExist(@Param("transactionHash") String transactionHash);

    @Select("SELECT max(block_number) FROM ethereum_transactions")
    long findLatestBlockNumber();

    @Insert("INSERT INTO ethereum_transactions(" + unionWithoutIDColumn + ") VALUES(#{blockNumer}, #{blockHash}, #{transactionHash}, #{blockTimestamp}, #{fromAccount}, #{toAccount}, #{transValue}, #{tranGasPrice}, #{tranGasAmount}, #{tranNonce})")
    int insertTran(@Param("blockNumer") long blockNumer, @Param("blockHash") String blockHash, @Param("transactionHash") String transactionHash, @Param("blockTimestamp") long blockTimestamp, @Param("fromAccount") String fromAccount, @Param("toAccount") String toAccount, @Param("transValue") double transValue, @Param("tranGasPrice") double tranGasPrice, @Param("tranGasAmount") long tranGasAmount, @Param("tranNonce") long tranNonce);

    @Insert("INSERT INTO pending_transactions(" + unionWithoutIDColumn + ") VALUES(#{blockNumer}, #{blockHash}, #{transactionHash}, #{blockTimestamp}, #{fromAccount}, #{toAccount}, #{transValue}, #{tranGasPrice}, #{tranGasAmount}, #{tranNonce})")
    int insertPendingTran(@Param("blockNumer") long blockNumer, @Param("blockHash") String blockHash, @Param("transactionHash") String transactionHash, @Param("blockTimestamp") long blockTimestamp, @Param("fromAccount") String fromAccount, @Param("toAccount") String toAccount, @Param("transValue") double transValue, @Param("tranGasPrice") double tranGasPrice, @Param("tranGasAmount") long tranGasAmount, @Param("tranNonce") long tranNonce);

    //------------------------------------------------------------------------------------------------------------------
    @Select("select count(*) from " + unionSubPendingSql)
    long findTotalTransNum();

    @Select(unionPendingSql)
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "blockNumer", column = "block_number"),
            @Result(property = "blockHash", column = "block_hash"),
            @Result(property = "blockTimestamp", column = "block_timestamp"),
            @Result(property = "transactionHash", column = "transaction_hash"),
            @Result(property = "fromAccount", column = "from_account"),
            @Result(property = "toAccount", column = "to_account"),
            @Result(property = "transValue", column = "tran_value"),
            @Result(property = "tranGasPrice", column = "tran_gas_price"),
            @Result(property = "tranGasAmount", column = "tran_gas_amount"),
            @Result(property = "tranNonce", column = "tran_nonce")
    })
    List<Transaction> findAllTransList();

    //------------------------------------------------------------------------------------------------------------------
    @Select("SELECT count(*) FROM " + unionSubPendingSql + " where utable.from_account=#{account} or utable.to_account=#{account}")
    long findSBTransListNum(@Param("account") String account);

    @Select("SELECT " + unionColumn+ " FROM (SELECT " + unionColumn+ " FROM ethereum_transactions where from_account=#{account} or to_account=#{account} UNION ALL SELECT " + unionColumn+ " FROM pending_transactions where from_account=#{account} or to_account=#{account} order by block_timestamp desc) as utable")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "blockNumer", column = "block_number"),
            @Result(property = "blockHash", column = "block_hash"),
            @Result(property = "blockTimestamp", column = "block_timestamp"),
            @Result(property = "transactionHash", column = "transaction_hash"),
            @Result(property = "fromAccount", column = "from_account"),
            @Result(property = "toAccount", column = "to_account"),
            @Result(property = "transValue", column = "tran_value"),
            @Result(property = "tranGasPrice", column = "tran_gas_price"),
            @Result(property = "tranGasAmount", column = "tran_gas_amount"),
            @Result(property = "tranNonce", column = "tran_nonce")
    })
    List<Transaction> findSBTransList(@Param("account") String account);

    //------------------------------------------------------------------------------------------------------------------
    @Select("SELECT count(*) FROM " + unionSubPendingSql + " where utable.from_account=#{account} and utable.to_account=#{token}")
    long findSBTokenTransListNum(@Param("account") String account, @Param("token") String token);

    @Select("SELECT " + unionColumn+ " FROM (SELECT " + unionColumn+ " FROM ethereum_transactions where from_account=#{account} and to_account=#{token} UNION ALL SELECT " + unionColumn+ " FROM pending_transactions where from_account=#{account} and to_account=#{token} order by block_timestamp desc) as utable")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "blockNumer", column = "block_number"),
            @Result(property = "blockHash", column = "block_hash"),
            @Result(property = "blockTimestamp", column = "block_timestamp"),
            @Result(property = "transactionHash", column = "transaction_hash"),
            @Result(property = "fromAccount", column = "from_account"),
            @Result(property = "toAccount", column = "to_account"),
            @Result(property = "transValue", column = "tran_value"),
            @Result(property = "tranGasPrice", column = "tran_gas_price"),
            @Result(property = "tranGasAmount", column = "tran_gas_amount"),
            @Result(property = "tranNonce", column = "tran_nonce")
    })
    List<Transaction> findSBTokenTransList(@Param("account") String account, @Param("token") String token);
    //------------------------------------------------------------------------------------------------------------------

    @Select("SELECT * FROM pending_transactions")
    @Results({
            @Result(column = "transaction_hash")
    })
    List<String> selectPendingTransHash();

    @Delete("DELETE FROM pending_transactions where transaction_hash=#{transactionHash}")
    void deletePendingTranByHash(@Param("transactionHash") String transactionHash);
}
