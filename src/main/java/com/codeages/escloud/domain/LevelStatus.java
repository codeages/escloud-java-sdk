package com.codeages.escloud.domain;

import java.io.Serializable;

public class LevelStatus implements Serializable {
    /**
     * 转码清晰度级别
     */
    private String level;

    /**
     * 转码状态
     */
    private String status;

    /**
     * 转码进度
     */
    private Integer transcodingProgress;

    /**
     * 错误码
     */
    private Integer errorCode;

    /**
     * 完成时间
     */
    private Integer finishedTime;


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTranscodingProgress() {
        return transcodingProgress;
    }

    public void setTranscodingProgress(Integer transcodingProgress) {
        this.transcodingProgress = transcodingProgress;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Integer finishedTime) {
        this.finishedTime = finishedTime;
    }
}
