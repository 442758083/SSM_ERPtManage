package com.ischoolbar.programmer.controller;

import com.ischoolbar.programmer.entity.User;
import com.ischoolbar.programmer.page.Page;
import com.ischoolbar.programmer.services.UserService;
import com.ischoolbar.programmer.util.ExportExcelUtil;
import com.ischoolbar.programmer.util.ExportExcelWrapper;
import com.ischoolbar.programmer.util.LoggerHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Lenovo on 2019/10/29.
 */
@Controller
@RequestMapping("/user")
public class UserController {
  @Autowired
  public UserService userService;


  /**
   * 用户管理列表页
   * @param model
   * @return
   */
  @RequestMapping(value = "/list",method = RequestMethod.GET)
  public ModelAndView list(ModelAndView model){
    model.setViewName("user/user_list");
    return model;
  }

  @RequestMapping(value = "/get_list",method = RequestMethod.POST)
  @ResponseBody
  public Map<String,Object> getList(
      @RequestParam(value = "username",required =false,defaultValue = "") String username,
      Page page
  ){
    Map<String, Object> ret = new HashMap<String, Object>();
    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put("username", "%"+username+"%");
    queryMap.put("offset", page.getOffset());
    queryMap.put("pageSize",page.getRows());
    ret.put("rows", userService.findList(queryMap));
    ret.put("total", userService.getTotal(queryMap));
    return ret;
  }

  /**
   * @param user
   * @return
   */
  @RequestMapping(value = "/add",method = RequestMethod.POST)
  @ResponseBody
  public Map<String,String> add(User user){
    Map<String, String> ret = new HashMap<String, String>();
    LoggerHelper.tracingLog("username="+user.getUsername()+"----password="+user.getPassword());
    if(user == null){
      ret.put("type", "error");
      ret.put("msg", "用户信息不能为空!");
      return ret;
    }
    if(StringUtils.isEmpty(user.getUsername())){
      ret.put("type", "error");
      ret.put("msg", "用户名不能为空!");
      return ret;
    }
    if(StringUtils.isEmpty(user.getPassword())){
      ret.put("type", "error");
      ret.put("msg", "密码不能为空!");
      return ret;
    }
    User existUser = userService.findByUserName(user.getUsername());
    if(existUser != null){
      ret.put("type", "error");
      ret.put("msg", "该用户名已经存在!");
      return ret;
    }
    if(userService.add(user) <= 0){
      ret.put("type", "error");
      ret.put("msg", "添加失败!");
      return ret;
    }
    ret.put("type","success");
    ret.put("msg","添加成功");
    return ret;
  }

  @RequestMapping(value = "/update",method = RequestMethod.POST)
  @ResponseBody
  public Map<String,String> update(User user){
    Map<String, String> ret = new HashMap();
    LoggerHelper.tracingLog("username="+user.getUsername()+"----password="+user.getPassword());

    if(StringUtils.isEmpty(user.getUsername())){
      ret.put("type", "error");
      ret.put("msg", "用户名不能为空!");
      return ret;
    }
    if(StringUtils.isEmpty(user.getPassword())){
      ret.put("type", "error");
      ret.put("msg", "密码不能为空!");
      return ret;
    }
    User existUser = userService.findByUserName(user.getUsername());
    if(existUser != null){
      if(user.getId() != existUser.getId()){
        ret.put("type", "error");
        ret.put("msg", "该用户名已经存在!");
        return ret;
      }

    }
    if(userService.update(user)<=0){
      ret.put("type", "error");
      ret.put("msg", "修改失败!");
      return ret;
    }
    ret.put("type","success");
    ret.put("msg","保存成功");
    return ret;
  }

  /**
   *
   * @param ids
   * @return
   */
  @RequestMapping(value = "/delete",method = RequestMethod.POST)
  @ResponseBody
  public Map<String,String> delete(
      @RequestParam(value = "ids[]",required = true) Long[] ids
  ){
    Map<String, String> ret = new HashMap<String, String>();
    if(ids == null){
      ret.put("type", "error");
      ret.put("msg", "请选择要删除的数据!");
      return ret;
    }
    String idsString = "";
    for(Long id:ids){
      idsString += id + ",";
    }
    idsString = idsString.substring(0,idsString.length()-1);
    if(userService.delete(idsString) <= 0){
      ret.put("type", "error");
      ret.put("msg", "删除失败!");
      return ret;
    }
    ret.put("type", "success");
    ret.put("msg", "修改成功!");
    return ret;
  }

  @RequestMapping(value = "/getexcle",method = RequestMethod.GET)
  public void getExcel(HttpServletRequest request, HttpServletResponse response)throws Exception {
    List<User> list = new ArrayList<User>();
    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put("username", "%%");
    queryMap.put("offset", 0);
    queryMap.put("pageSize",10000);
    list=userService.findList(queryMap);
    for(User user:list){

    }
    String[] columnNames = { "ID", "用户名", "密码"};
    String fileName = "用户列表";
    ExportExcelWrapper<User> util = new ExportExcelWrapper<User>();
    util.exportExcel(fileName, fileName, columnNames, list, response, ExportExcelUtil.EXCEL_FILE_2003);
  }
}
