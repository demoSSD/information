package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface CustomerDao {
    List<JSONObject> getChatList(Integer status);
    int addMessage(String content,String email,String phone);
    int updateMessage(Integer id);
}
