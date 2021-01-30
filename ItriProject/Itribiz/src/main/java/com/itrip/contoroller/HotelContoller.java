package com.itrip.contoroller;

import cn.itrip.common.Dto;

import cn.itrip.common.DtoUtil;
import com.itrip.dao.itripAreaDic.ItripAreaDicMapper;
import com.itrip.dao.itripLabelDic.ItripLabelDicMapper;
import com.itrip.pojo.ItripAreaDic;
import com.itrip.pojo.ItripLabelDic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

@Controller
public class HotelContoller {
    @Resource
    ItripAreaDicMapper DAO;

    @Resource
    ItripLabelDicMapper DAO2;

    @RequestMapping(value = "/api/hotel/queryhotcity/{id}",method = RequestMethod.GET,produces="application/json; charset=utf-8")
    public @ResponseBody Dto re(@PathVariable("id")int id) throws Exception {
        List<ItripAreaDic> list=DAO.Itrip_jd(id);
        return DtoUtil.returnDataSuccess(list);
    }

    @RequestMapping(value = "/api/hotel/queryhotelfeature",method = RequestMethod.GET,produces="application/json; charset=utf-8")
    public @ResponseBody Dto re2() throws Exception {
        List<ItripLabelDic> list=DAO2.list();
        return DtoUtil.returnDataSuccess(list);
    }

}
