package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.NewQuestionDao;
import com.heeexy.example.dao.NewThumbsDao;
import com.heeexy.example.dao.QuestionDao;
import com.heeexy.example.service.NewQuestionService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewQuestionServiceImpl implements NewQuestionService {

    @Resource
    private NewQuestionDao newQuestionDao;


    @Override
    public JSONObject addQuestion(JSONObject question) {
        int res=newQuestionDao.addQuestion(question);
        if(res==1)
            return CommonUtil.successJson("发布成功");
        else
           return CommonUtil.errorJson(ErrorEnum.E_400);
    }

    @Override
    public JSONObject getQuestion(JSONObject val) {
        int sort=val.getIntValue("sort");
        List<JSONObject> res=new ArrayList<JSONObject>();
        if(sort==4)
            res=newQuestionDao.getQuestion_NoAccept(val);
        else
            res=newQuestionDao.getQuestion(val);
        for(JSONObject json:res) {
            Integer tem=newQuestionDao.exits(val.getString("userId"),json.getIntValue("id"));
            if(tem==null)
                json.put("type",0);
            else
                json.put("type",tem);
        }
        return CommonUtil.successJson(res);
    }

    @Override
    public JSONObject updateClicks(JSONObject val) {
        int res=newQuestionDao.updateClicks(val);
        return res==1?CommonUtil.successJson(1):CommonUtil.successJson();
    }

    @Override
    public JSONObject getQuestionById(JSONObject val) {
        JSONObject res=newQuestionDao.getQuestionById(val.getIntValue("questionId"));
        if(res==null)
            return CommonUtil.errorJson(ErrorEnum.E_20012);
        Integer tem=newQuestionDao.exits(val.getString("userId"),val.getIntValue("questionId"));
        if(tem==null)
            res.put("type",0);
        else
            res.put("type",tem);
        return CommonUtil.successJson(res);
    }

    @Override
    public JSONObject getQuestionByUser(JSONObject val) {
        List<JSONObject> res=newQuestionDao.getQuestionByUser(val);
        if(res==null)
            return CommonUtil.errorJson(ErrorEnum.E_20012);
        for(JSONObject json:res) {
            Integer tem=newQuestionDao.exits(val.getString("userId"),json.getIntValue("id"));
            if(tem==null)
                json.put("type",0);
            else
                json.put("type",tem);
        }
        return CommonUtil.successJson(res);
    }

    @Override
    public JSONObject deleteQuestion(JSONObject val) {
        int res=newQuestionDao.deleteQuestion(val);

        return res==1?CommonUtil.successJson(1):CommonUtil.errorJson(ErrorEnum.E_400);
    }
}
