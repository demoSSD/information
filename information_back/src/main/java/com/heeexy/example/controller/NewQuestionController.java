package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.config.annotation.RequiresPermissions;
import com.heeexy.example.service.NewQuestionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/new_question")
public class NewQuestionController {
    @Resource
    private NewQuestionService newQuestionService;

    //添加一个问题
    @PostMapping("/addQuestion")
    public JSONObject addQuestion(@RequestBody  JSONObject question){
        return newQuestionService.addQuestion(question);
    }

    //筛选问题列表
    @PostMapping("/getQuestion")
    public JSONObject getQuestion(@RequestBody  JSONObject  val){
        return newQuestionService.getQuestion(val);
    }

    //筛选问题列表
    @PostMapping("/addClick")
    public JSONObject addClick(@RequestBody  JSONObject  val){
        return newQuestionService.updateClicks(val);
    }

    //筛选问题列表
    @PostMapping("/getQuestionById")
    public JSONObject getQuestionById(@RequestBody  JSONObject  val){
        return newQuestionService.getQuestionById(val);
    }

    //得到用户所有问题
    @PostMapping("/getQuestionByUser")
    public JSONObject getQuestionByUser(@RequestBody  JSONObject  val){
        return newQuestionService.getQuestionByUser(val);
    }

    @RequiresPermissions("questionControl:deleteQue")
    @PostMapping("/deleteQuestion")
    public JSONObject deleteQuestion(@RequestBody JSONObject val){
        return newQuestionService.deleteQuestion(val);
    }
}
