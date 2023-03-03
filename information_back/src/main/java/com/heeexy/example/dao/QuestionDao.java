package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionDao {
    int addQuestion(JSONObject question);
    List<JSONObject> getQuestion();
    int updateQuestion(JSONObject question);
    int deleteQuestion(JSONObject question);
    List<JSONObject> getQuestionByLike(String input);
    int isExit(JSONObject val);
}
