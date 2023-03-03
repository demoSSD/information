package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

public interface CustomerService {
    JSONObject getChatList(Integer status, Integer currentPage);
    JSONObject addMessage(String content,String email,String phone);
    JSONObject updateMessage(Integer messageId);
}
