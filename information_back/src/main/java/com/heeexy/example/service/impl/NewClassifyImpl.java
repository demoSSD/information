package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.NewClassifyDao;
import com.heeexy.example.dao.NewQuestionDao;
import com.heeexy.example.service.NewClassifyService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewClassifyImpl implements NewClassifyService {
    @Resource
    private NewClassifyDao newClassifyDao;

    @Override
    public JSONObject getAllClassify() {
        List<JSONObject> res=newClassifyDao.getAllClassify();
        return CommonUtil.successJson(res);
    }

    @Override
    public JSONObject getTags(JSONObject classify) {
        List<JSONObject> res=newClassifyDao.getTags(classify);
        return CommonUtil.successJson(res);
    }

    @Override
    public JSONObject getAllTags() {
        List<JSONObject> res=newClassifyDao.getAllTags();
        return CommonUtil.successJson(res);
    }

    @Override
    public JSONObject addClassify(JSONObject classify) {
        JSONArray val=classify.getJSONArray("list");
        List<String> val2=val.toJavaList(String.class);
        int res=newClassifyDao.addClassify(val2);
        if(res>0)
            return CommonUtil.successJson();
        else
          return CommonUtil.errorJson(ErrorEnum.E_400);
    }

    @Override
    public JSONObject addTagsByClassify(JSONObject tags) {
        JSONArray val=tags.getJSONArray("list");
        List<JSONObject> val2=val.toJavaList(JSONObject.class);
        int res=newClassifyDao.addTagsByClassify(val2);
        if(res>0)
            return CommonUtil.successJson();
        else
            return CommonUtil.errorJson(ErrorEnum.E_400);
    }

    @Override
    public JSONObject deleteTag(JSONObject val) {
        newClassifyDao.deleteTag(val);
        return CommonUtil.successJson();
    }
}
