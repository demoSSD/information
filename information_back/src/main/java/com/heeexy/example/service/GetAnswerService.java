package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;


public interface GetAnswerService {
    JSONObject getAnswerById(String question);
    JSONObject getNewQuestion();
}
