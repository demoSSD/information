package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionService {
    JSONObject addImage(MultipartFile file);
    JSONObject addQuestion(JSONObject question);
    JSONObject getQuestions(Integer currentPage);
    JSONObject deleteQuestion(JSONObject jsonObject);
    JSONObject updateQuestion(JSONObject jsonObject);
    JSONObject getQuestionsBylike(String input,Integer currentPage);
}
