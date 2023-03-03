package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.util.model.Reply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewReplyDao {
    int addReply(Reply reply1);
    List<JSONObject> getQuestionReply(int questionId,int sort);
    List<JSONObject> getUserReply(JSONObject val);
    int updateAccept(JSONObject val);
}
