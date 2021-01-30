package com.itrip.contoroller;



import com.itrip.dao.itripHotel.ItripHotelMapper;
import com.itrip.pojo.ItripHotel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

@Controller
public class HotelContoller {
    @Resource
    ItripHotelMapper dao;

    @RequestMapping(value = "/list")
    @ResponseBody
    public ItripHotel getlist() throws Exception {

        Jedis jedis=new Jedis("127.0.0.1");
        jedis.auth("123456");
        jedis.setex("zheng",60*60*2,"zhong");

        ItripHotel cn=dao.getItripHotelById(new Long(1));
        return cn;
    }

}
