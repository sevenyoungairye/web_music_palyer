package com.lovely.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.sql.Timestamp;

/**
 * 用户表
 * @author echo lovely
 * @date 2020/8/15 11:55
 */
public class MusicUser {
    private Integer userId ;
    private String userName;
    private String userPwd;
    private Integer isLogout;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;

    public MusicUser() {}

    public MusicUser(Integer userId, String userName, String userPwd, Integer isLogout, Timestamp createDate) {
        this.userId = userId;
        this.userName = userName;
        this.userPwd = userPwd;
        this.isLogout = isLogout;
        this.createDate = createDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public Integer getIsLogout() {
        return isLogout;
    }

    public void setIsLogout(Integer isLogout) {
        this.isLogout = isLogout;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "MusicUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", isLogout=" + isLogout +
                ", createDate=" + createDate +
                '}' + "\n";
    }
}
