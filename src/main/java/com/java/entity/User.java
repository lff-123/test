package com.java.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：刘芳芳
 * @Date：2019/4/16
 * @Description：
 */
@Data
public class User implements Serializable {
    private String userName;
    private String userPwd;

    public User(String userName, String userPwd) {
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public User() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("userName='").append(userName).append('\'');
        sb.append(", userPwd='").append(userPwd).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
