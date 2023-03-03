package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface NewQuestionService {
    JSONObject addQuestion(JSONObject question);
    JSONObject getQuestion(JSONObject val);
    JSONObject updateClicks(JSONObject val);
    JSONObject getQuestionById(JSONObject val);
    JSONObject getQuestionByUser(JSONObject val);
    JSONObject deleteQuestion(JSONObject val);
}
