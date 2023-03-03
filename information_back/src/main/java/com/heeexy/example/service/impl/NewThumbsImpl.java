package com.heeexy.example.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.NewThumbsDao;
import com.heeexy.example.service.NewThumbsService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NewThumbsImpl implements NewThumbsService {
    @Resource
    private NewThumbsDao newThumbsDao;
    @Override
    public JSONObject addThumb(JSONObject thumb) {
        int isExits=newThumbsDao.exits(thumb);
        int res=0;
        res=isExits==0?newThumbsDao.addThumb(thumb):newThumbsDao.updateThumb(thumb);
        return res==1?CommonUtil.successJson(1):CommonUtil.errorJson(ErrorEnum.E_400);
    }

    @Override
    public JSONObject deleteThumb(JSONObject thumb) {
        int res=newThumbsDao.deleteThumb(thumb);
        return res==1?CommonUtil.successJson(1):CommonUtil.errorJson(ErrorEnum.E_400);
    }


}
