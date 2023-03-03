package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface LoginService {
   JSONObject authLogin(JSONObject jsonObject);
   JSONObject getInfo();
   JSONObject logout();
}
