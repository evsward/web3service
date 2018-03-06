package com.web3.scph.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.web3.scph.po.Transaction;
import com.web3.scph.service.Web3Service;
import com.web3.scph.vo.TranListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.EthTransaction;

import java.util.List;

@RestController
@RequestMapping("/web3")
public class TransactionController {
    @Autowired
    private Web3Service web3Service;

    @RequestMapping(value = "/totalTrans")
    @CrossOrigin
    public long selectTotalTransAmount() {
        return web3Service.findTotalTransNum();
    }

    @RequestMapping(value = "/blockHeight")
    @CrossOrigin
    public long findLatestBlockNumber() {
        return web3Service.findLatestBlockNumber();
    }


    @RequestMapping(value = "/trans")
    @CrossOrigin
    public TranListResponse selectTranList(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Transaction> list = web3Service.findAllTransList();
        TranListResponse tranListResponse = new TranListResponse();
        tranListResponse.setCode(1);
        tranListResponse.setMsg("success");
        tranListResponse.setSize(web3Service.findTotalTransNum());
        PageInfo<Transaction> pageInfo = new PageInfo<Transaction>(list);
        tranListResponse.setList(list);
        return tranListResponse;
    }

    @RequestMapping(value = "/trans/{account}")
    @CrossOrigin
    public TranListResponse selectSBTranList(@PathVariable String account, @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Transaction> list = web3Service.findSBTransList(account);
        TranListResponse tranListResponse = new TranListResponse();
        tranListResponse.setCode(1);
        tranListResponse.setMsg("success");
        tranListResponse.setSize(web3Service.findSBTransListNum(account));
        PageInfo<Transaction> pageInfo = new PageInfo<Transaction>(list);
        tranListResponse.setList(list);
        return tranListResponse;
    }

    @RequestMapping(value = "/trans/token")
    @CrossOrigin
    public TranListResponse selectSBTokenTranList(@RequestParam String account, @RequestParam String token, @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Transaction> list = web3Service.findSBTokenTransList(account, token);
        TranListResponse tranListResponse = new TranListResponse();
        tranListResponse.setCode(1);
        tranListResponse.setMsg("success");
        tranListResponse.setSize(web3Service.findSBTokenTransListNum(account, token));
        PageInfo<Transaction> pageInfo = new PageInfo<Transaction>(list);
        tranListResponse.setList(list);
        return tranListResponse;
    }

    @RequestMapping("/{tranHash}")
    @CrossOrigin
    public EthTransaction getTranByHash(@PathVariable String tranHash) {
        return web3Service.getTranByHash(tranHash);
    }
}
