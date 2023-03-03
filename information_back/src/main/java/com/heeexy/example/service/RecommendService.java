package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface RecommendService {
    JSONObject getRecommendList(String content);
    JSONObject getList();
}
