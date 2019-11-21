package com.ischoolbar.api;

import com.ischoolbar.programmer.entity.User;
import com.ischoolbar.programmer.services.UserService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Lenovo on 2019/11/14.
 */
@Controller
@RequestMapping(value = "/api")
public class getUserInfo {

  private static String sign = "e4da3b7fbbce2345d7772b0674a318d5";

  @Autowired
  private UserService userService;

  @RequestMapping(value = "/getUserInfoById",method = RequestMethod.GET)
  @ResponseBody
  public Map<String,Object> getUserInfoById(HttpServletRequest request, HttpServletResponse response){
    Map<String, Object> ret = new HashMap<String, Object>();
    String id = request.getParameter("id");
    String app_sign = request.getParameter("sign");
    if(!(StringUtils.isEmpty(id)||StringUtils.isEmpty(app_sign))){
      ret.put("code","400");
      ret.put("state","fail");
      ret.put("msg","请确认参数是否正确!");
      return ret;
    }
    if(!app_sign.equals(sign)){
      ret.put("code","400");
      ret.put("state","fail");
      ret.put("msg","签名不正确!");
      return ret;
    }
    User user = userService.findByUserId(id);
    if(user!= null){
      ret.put("code","200");
      ret.put("state","success");
      ret.put("msg","查询成功");
      ret.put("user",user);
    }else {
      ret.put("code","400");
      ret.put("state","fail");
      ret.put("msg","该用户不存在!");
      ret.put("user",null);
    }
    return ret;
  }


}
