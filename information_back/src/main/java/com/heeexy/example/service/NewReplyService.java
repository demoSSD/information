package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface NewReplyService {
    JSONObject addReply(JSONObject reply);
    JSONObject getQuestionReply(JSONObject val);

    JSONObject getUserReply(JSONObject val);
    JSONObject updateAccept(JSONObject val);
}
