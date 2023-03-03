package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.NewQuestionDao;
import com.heeexy.example.dao.NewReplyDao;
import com.heeexy.example.dao.QuestionDao;
import com.heeexy.example.service.NewReplyService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import com.heeexy.example.util.model.Reply;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewReplyImpl implements NewReplyService {
    @Resource
    private NewReplyDao newReplyDao;
    @Resource
    private NewQuestionDao  newQuestionDao2;

    @Resource
    private QuestionDao questionDao2;

    @Override
    public JSONObject addReply(JSONObject reply) {
        Reply reply1 = new Reply();
        reply1.setReply_id(reply.getString("reply_id"));
        reply1.setContent(reply.getString("content"));
        reply1.setQuestion_id(reply.getIntValue("question_id"));
        int res=newReplyDao.addReply(reply1);
        JSONObject info = new JSONObject();
        if(res==1){
            info.put("id",reply1.getId());
            return CommonUtil.successJson(info);
        }else{
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }

    }

    @Override
    public JSONObject getQuestionReply(JSONObject val) {
        int sort=val.getIntValue("sort");
        List<JSONObject> res=newReplyDao.getQuestionReply(val.getIntValue("questionId"),sort);
        if(res==null)
            return CommonUtil.successJson(0);
        for(JSONObject json:res) {
            Integer tem=newQuestionDao2.exits(val.getString("userId"),json.getIntValue("id"));
            if(tem==null)
                json.put("type",0);
            else
                json.put("type",tem);
        }
        return CommonUtil.successJson(res);
    }

    @Override
    public JSONObject getUserReply(JSONObject val) {
        List<JSONObject> res=newReplyDao.getUserReply(val);
        if(res==null)
            return CommonUtil.successJson(null);
        for(JSONObject json:res) {
            Integer tem=newQuestionDao2.exits(val.getString("userId"),json.getIntValue("id"));
            if(tem==null)
                json.put("type",0);
            else
                json.put("type",tem);
        }
        return CommonUtil.successJson(res);
    }

    @Override
    public JSONObject updateAccept(JSONObject val) {
        int res=newReplyDao.updateAccept(val);
        int isExit=questionDao2.isExit(val);
        if(val.getIntValue("type")==1&&isExit==0)
        {
            questionDao2.addQuestion(val);
        }
        return res==1?CommonUtil.successJson(1):CommonUtil.errorJson(ErrorEnum.E_400);
    }

}
