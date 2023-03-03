package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;

public interface NewClassifyService {
    JSONObject getAllClassify();
    JSONObject getTags(JSONObject classify);
    JSONObject getAllTags();
    JSONObject addClassify(JSONObject classify);

    JSONObject addTagsByClassify(JSONObject tags);
    JSONObject deleteTag(JSONObject val);
}
