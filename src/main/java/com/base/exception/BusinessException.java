package com.base.exception;

/**
 * @author: football98
 * @createTime: 16-9-28
 * @classDescription: 自定义异常。
 */
public class BusinessException extends Exception{

    private String errMsg;
    private int errCode;

    public String getErrMsg() {
        return errMsg;
    }
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
    public int getErrCode() {
        return errCode;
    }
    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
    public BusinessException() {

    }

    public BusinessException(String errMsg) {
        this.errMsg = errMsg;
        this.errCode = ErrCode.USER_OP_ERROR;
    }

    public BusinessException(String errMsg, int errCode) {
        this.errMsg = errMsg;
        this.errCode = errCode;
    }
}
