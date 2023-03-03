package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;



public interface NewThumbsService {
    JSONObject addThumb(JSONObject thumb);
    JSONObject deleteThumb(JSONObject thumb);
}
