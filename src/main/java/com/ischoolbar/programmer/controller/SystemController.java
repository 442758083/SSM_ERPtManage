package com.ischoolbar.programmer.controller;


import com.ischoolbar.programmer.entity.User;
import com.ischoolbar.programmer.services.UserService;
import com.ischoolbar.programmer.util.CpachaUtil;
import com.ischoolbar.programmer.util.LoggerHelper;
import com.ischoolbar.programmer.util.Md5Utils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
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
 * Created by Lenovo on 2019/10/17.
 */
@RequestMapping("/system")
@Controller
public class SystemController {


  @Autowired
  private UserService userService;

  @RequestMapping(value = "index")
  public ModelAndView index(ModelAndView model){
    System.out.println("进入INDEX方法");
    model.setViewName("system/index");
    return model;
  }

  @RequestMapping(value = "/login",method=RequestMethod.GET)
  public ModelAndView login(ModelAndView model){
    LoggerHelper.tracingLog("进入登录界面");
    model.setViewName("system/login");
    return model;
  }

  @RequestMapping(value = "login",method = RequestMethod.POST)
  @ResponseBody
  public Map<String, String> Login(
      @RequestParam(value="username",required=true) String username,
      @RequestParam(value="password",required=true) String password,
      @RequestParam(value="vcode",required=true) String vcode,
      HttpServletRequest request){
    Map<String, String> ret = new HashMap<String, String>();
    LoggerHelper.tracingLog("username="+username+"---password="+password);
    if(StringUtils.isEmpty(username)){
      ret.put("type", "error");
      ret.put("msg", "用户名不能为空!");
      return ret;
    }
    if(StringUtils.isEmpty(password)){
      ret.put("type", "error");
      ret.put("msg", "密码不能为空!");
      return ret;
    }
    if(StringUtils.isEmpty(vcode)){
      ret.put("type", "error");
      ret.put("msg", "验证码不能为空!");
      return ret;
    }

    String loginCpacha = (String)request.getSession().getAttribute("loginCpacha");
    if(StringUtils.isEmpty(loginCpacha)){
      ret.put("type", "error");
      ret.put("msg", "长时间未操作,会话已失效，请刷新后重试!");
      return ret;
    }

    if(!vcode.toUpperCase().equals(loginCpacha.toUpperCase())){
      ret.put("type", "error");
      ret.put("msg", "验证码错误!");
      return ret;
    }


    User user = userService.findByUserName(username);

    if(user == null){
      ret.put("type", "error");
      ret.put("msg", "不存在该用户");
      return ret;
    }


    if(!(Md5Utils.MD5Encode(password,false)).equals(user.getPassword())){
      ret.put("type", "error");
      ret.put("msg", "密码错误!");
      return ret;
    }
    request.getSession().setAttribute("user", user);
    ret.put("type", "success");
    ret.put("msg", "登录成功");

    return ret;
  }

  @RequestMapping(value = "/login_out",method = RequestMethod.GET)
  public String login_out(HttpServletRequest request){
    request.getSession().setAttribute("user", null);
    return "redirect:login";
  }

  /**
   * 获取验证码
   * @param request
   * @param vl
   * @param w
   * @param h
   * @param response
   */
  @RequestMapping(value="/get_cpacha",method=RequestMethod.GET)
  public void getCpacha(HttpServletRequest request,
      @RequestParam(value="vl",defaultValue="4",required=false) Integer vl,
      @RequestParam(value="w",defaultValue="98",required=false) Integer w,
      @RequestParam(value="h",defaultValue="33",required=false) Integer h,
      HttpServletResponse response){
    CpachaUtil cpachaUtil = new CpachaUtil(vl, w, h);
    String generatorVCode = cpachaUtil.generatorVCode();
    request.getSession().setAttribute("loginCpacha", generatorVCode);
    BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
    try {
      ImageIO.write(generatorRotateVCodeImage, "gif", response.getOutputStream());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
