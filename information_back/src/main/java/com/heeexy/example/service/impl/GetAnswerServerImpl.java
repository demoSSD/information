package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.heeexy.example.dao.GetAnswerDao;
import com.heeexy.example.service.GetAnswerService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class GetAnswerServerImpl implements GetAnswerService {

    @Resource
    private GetAnswerDao getAnswerDao;

    @Override
    public JSONObject getAnswerById(String question) {
        JSONObject res=getAnswerDao.getAnswerById(question);
        return CommonUtil.successJson(res);
    }

    @Override
    public JSONObject getNewQuestion() {
        List<JSONObject> newQuestion = getAnswerDao.getNewQuestion();
        return CommonUtil.successJson(newQuestion);
    }
}
