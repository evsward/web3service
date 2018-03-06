package com.web3.scph.vo;

import com.web3.scph.po.Transaction;

import java.util.List;

public class TranListResponse {
    private int code;
    private String msg;
    private long size;
    private List<Transaction> list;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public long getSize() {
        return size;
    }

    public List<Transaction> getList() {
        return list;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setList(List<Transaction> list) {
        this.list = list;
    }
}
