package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NewClassifyDao {
    List<JSONObject> getAllClassify();
    List<JSONObject> getTags(JSONObject classify);
    List<JSONObject> getAllTags();

    int addClassify(List<String> val);
    int addTagsByClassify(List<JSONObject> val);
    int deleteTag(JSONObject val);
}
