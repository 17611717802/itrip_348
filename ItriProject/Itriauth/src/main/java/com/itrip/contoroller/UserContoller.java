package com.itrip.contoroller;




import cn.itrip.common.*;
import com.alibaba.fastjson.JSONArray;
import com.itrip.dao.itripUser.ItripUserMapper;
import com.itrip.pojo.ItripUser;
import com.itrip.pojo.ItripUserVO;
import cz.mallat.uasparser.UserAgentInfo;
import cn.itrip.common.JredisApi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Controller
public class UserContoller {

    @Resource
    ItripUserMapper dao;

    @Resource
    JredisApi jredisApi;

    @Resource
    SMS_Sent sms_sent;

    @RequestMapping(value = "/api/registerbyphone",method = RequestMethod.POST,produces="application/json; charset=utf-8")
    public @ResponseBody Dto re(@RequestBody ItripUserVO vo, HttpServletRequest request) throws Exception {
        try {
            //第一步，插入数据库数据
            ItripUser itripUser=new ItripUser();
            itripUser.setUserCode(vo.getUserCode());
            itripUser.setUserName(vo.getUserName());
            itripUser.setUserPassword(MD5.getMd5(vo.getUserPassword(),32));
            itripUser.setActivated(0);
            dao.insertItripUser(itripUser);
            //第二步，发送短信验证码,把手机号和验证码发送到redis中
            int mess= (int) (Math.random() * 1000000);
            jredisApi.SetRedis(vo.getUserCode(),""+mess,7200);
            //发送给用户短信
            sms_sent.sentSms(vo.getUserCode(),""+mess);

            return  DtoUtil.returnSuccess("注册成功");
        }
        catch (Exception ex){
            return  DtoUtil.returnFail("注册失败","1001");
        }
    }

    @RequestMapping(value = "/api/dologin",method = RequestMethod.POST,produces="application/json; charset=utf-8")
    @ResponseBody
   public Dto Dologin(String name, String password, HttpServletRequest request) throws Exception {
        ItripUser user=dao.getlogin(name, MD5.getMd5(password,32));
        //存入redis 中key value 过期时间
        if (user!=null)
        {
            //token= md5 加密 userID+userCode+时间戳
            String agent=request.getHeader("User-Agent");
            String token=generateToken(agent,user);
            jredisApi.SetRedis(token,JSONArray.toJSONString(user),7200);
            ItripTokenVO tokenVO=new ItripTokenVO(token, Calendar.getInstance().getTimeInMillis()+7200,Calendar.getInstance().getTimeInMillis());
            return DtoUtil.returnDataSuccess(tokenVO);
        }
        return  DtoUtil.returnFail("登陆失败","1000");
    }

    @RequestMapping(value = "/api/validatephone",method = RequestMethod.PUT,produces="application/json; charset=utf-8")

    public @ResponseBody Dto validation(String user, String code) throws Exception {
        //判断redis中的值

        String us=jredisApi.getRedis(user);
        if (us!=null&&us.equals(code)){
            //如果匹配把Mysql中的用户激活
            dao.jh(us);
            return DtoUtil.returnSuccess("激活成功",200);
        }
       return DtoUtil.returnFail("激活失败","404");
    }


    /**
     *
     * @param agent 是浏览器上的agent
     * @param user
     * @return
     */
    public String generateToken(String agent, ItripUser user) {
        // TODO Auto-generated method stub
        try {
            UserAgentInfo userAgentInfo = UserAgentUtil.getUasParser().parse(
                    agent);
            StringBuilder sb = new StringBuilder();
            sb.append("token:");//统一前缀
            if (userAgentInfo.getDeviceType().equals(UserAgentInfo.UNKNOWN)) {
                if (UserAgentUtil.CheckAgent(agent)) {
                    sb.append("MOBILE-");
                } else {
                    sb.append("PC-");
                }
            } else if (userAgentInfo.getDeviceType()
                    .equals("Personal computer")) {
                sb.append("PC-");
            } else
                sb.append("MOBILE-");
//			sb.append(user.getUserCode() + "-");
            sb.append(MD5.getMd5(user.getUserCode(),32) + "-");//加密用户名称
            sb.append(user.getId() + "-");
            sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                    + "-");
            sb.append(MD5.getMd5(agent, 6));// 识别客户端的简化实现——6位MD5码

            return sb.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
