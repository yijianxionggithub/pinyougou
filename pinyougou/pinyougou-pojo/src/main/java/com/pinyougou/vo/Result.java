package com.pinyougou.vo;

import java.io.Serializable;

/**
 * @version V1.0
 * @ClassName: ValidateActionJson
 * @Description: 提示页面操作信息类
 * @Auther: Jianxiong Yi
 * @Date: 2018/5/27 08:27
 */
public class Result implements Serializable {
    private boolean success;
    private String message;

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * 操作成功
     * @param message 提示信息
     * @return 结果对象
     */
    public static Result ok(String message) {
        return new Result(true,message);
    }

    /**
     * 操作失败
     * @param message 提示信息
     * @return 结果对象
     */
    public static Result fail(String message) {
        return new Result(false,message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
