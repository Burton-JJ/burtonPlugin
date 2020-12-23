package com.burton.plugin.servicePublish.exception;

/*********************************
 * <p> 文件名称: BurtonPluginExecoption
 * <p> 系统名称：交易银行系统V1.0
 * <p> 模块名称：com.burton.plugin.servicePublish.exception
 * <p> 功能说明: 
 * <p> 开发人员：jiangjun25372
 * <p> 开发时间：2020/12/23
 * <p> 修改记录：程序版本   修改日期    修改人员   修改单号   修改说明
 **********************************/
public class BurtonPluginExecoption extends RuntimeException{
    private String errorMsg;


    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param errorMsg the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public BurtonPluginExecoption(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
