package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewQuestionDao {
    int addQuestion(JSONObject question);
    List<JSONObject> getQuestion(JSONObject val);
    List<JSONObject> getQuestion_NoAccept(JSONObject val);
    Integer exits(String userId,int questionId);
    int getType(String userId,int questionId);
    int updateClicks(JSONObject val);
    JSONObject getQuestionById(int questionId);
    List<JSONObject> getQuestionByUser(JSONObject val);
    int deleteQuestion(JSONObject val);
}
