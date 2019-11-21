package com.ischoolbar.programmer.entity;

import org.springframework.stereotype.Component;

/**
 * Created by Lenovo on 2019/10/18.
 */

@Component
public class User {
  private Long id;//用户id，主键、自增
  private String username;//用户名
  private String password;//密码
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        '}';
  }
}
