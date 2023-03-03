package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface ArticleService {
    JSONObject addArticle(JSONObject jsonObject);
    JSONObject listArticle(JSONObject jsonObject);
    JSONObject updateArticle(JSONObject jsonObject);
}
