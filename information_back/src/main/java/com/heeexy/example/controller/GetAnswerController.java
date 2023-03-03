package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.GetAnswerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/answer")
public class GetAnswerController {

    @Resource
    private GetAnswerService getAnswerServer;

    //通过问题获取答案
    @GetMapping("/{question}")
    public JSONObject getAnswer(@PathVariable(name = "question") String question){
        return getAnswerServer.getAnswerById(question);
    }

    //获取最新5条问题
    @GetMapping("/getNewQuestion")//返回值(最新问题id，问题question，答案answer，时间)选择使用
    public JSONObject getNewQuestion(){
        return getAnswerServer.getNewQuestion();
    }
}
