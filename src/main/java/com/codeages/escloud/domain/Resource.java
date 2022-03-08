package com.codeages.escloud.domain;

import java.io.Serializable;
import java.util.List;

public class Resource implements Serializable {
    /**
     * 资源编号
     */
    private String no;

    /**
     * 外部编号，客户端自己定义
     */
    private String extno;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源类型
     */
    private String type;

    /**
     * 资源大小，单位B
     */
    private Integer size;

    /**
     * 资源时长(秒)，或者页数
     */
    private Integer length;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 转码状态
     */
    private String processStatus;

    /**
     * 转码进度
     */
    private List<LevelStatus> levelsStatus;

    /**
     * 是否分享
     */
    private Integer isShare;

    /**
     * 转码成功的时间戳，秒
     */
    private Integer processedTime;

    /**
     * 创建的时间戳，秒
     */
    private Integer createdTime;

    /**
     * 更新的时间戳,秒
     */
    private Integer updatedTime;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getExtno() {
        return extno;
    }

    public void setExtno(String extno) {
        this.extno = extno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public Integer isShare() {
        return isShare;
    }

    public void setShare(Integer share) {
        isShare = share;
    }

    public Integer getProcessedTime() {
        return processedTime;
    }

    public void setProcessedTime(Integer processedTime) {
        this.processedTime = processedTime;
    }

    public Integer getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Integer createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Integer updatedTime) {
        this.updatedTime = updatedTime;
    }

    public List<LevelStatus> getLevelsStatus() {
        return levelsStatus;
    }

    public void setLevelsStatus(List<LevelStatus> levelsStatus) {
        this.levelsStatus = levelsStatus;
    }

    public Integer getIsShare() {
        return isShare;
    }

    public void setIsShare(Integer isShare) {
        this.isShare = isShare;
    }
}
