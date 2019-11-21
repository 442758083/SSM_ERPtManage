package com.ischoolbar.programmer.services;

import com.ischoolbar.programmer.entity.User;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Created by Lenovo on 2019/10/18.
 */
@Service
public interface UserService {
  public User findByUserId(String id);
  public User findByUserName(String username);
  public List<User> findList(Map<String,Object> queryMap);
  public int getTotal(Map<String,Object> queryMap);
  public int add(User user);
  public int update(User user);
  public int delete(String ids);
}
