package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface NewThumbsDao {
    int addThumb(JSONObject thumb);
    int exits(JSONObject thumb);
    int updateThumb(JSONObject thumb);
    int deleteThumb(JSONObject thumb);
}
