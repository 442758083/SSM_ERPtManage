package com.ischoolbar.programmer.services.Impl;

import com.ischoolbar.programmer.dao.UserDao;
import com.ischoolbar.programmer.entity.User;
import com.ischoolbar.programmer.services.UserService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Lenovo on 2019/10/18.
 */
@Service
public class UserServiceImpl implements UserService{

  @Autowired
  private User user;
  @Autowired
  private UserDao userDao;

  @Autowired
  private UserService userService;

  @Override
  public User findByUserId(String id) {
    return userDao.findByUserId(id);
  }

  @Override
  public User findByUserName(String username) {
    return userDao.findByUserName(username);
  }

  @Override
  public List<User> findList(Map<String,Object> queryMap) {
    return userDao.findList(queryMap);
  }

  @Override
  public int getTotal(Map<String,Object> queryMap) {
    return userDao.getTotal(queryMap);
  }

  @Override
  public int add(User user) {
    return userDao.add(user);
  }

  @Override
  public int update(User user) {
    return userDao.update(user);
  }

  @Override
  public int delete(String ids) {
    return userDao.delete(ids);
  }
}
