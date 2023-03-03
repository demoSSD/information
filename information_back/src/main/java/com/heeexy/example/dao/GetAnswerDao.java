package com.heeexy.example.dao;


import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GetAnswerDao {
    JSONObject getAnswerById(String question);
    List<JSONObject> getList();
    List<JSONObject> getNewQuestion();
}
