package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.QuestionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @PostMapping("/addImage")//参数为图片
    public JSONObject addImage(@RequestParam("file") MultipartFile file){
        return questionService.addImage(file);
    }

    @PostMapping("/addQuestion")//参数为问题（question），答案（answer）
    public JSONObject addQuestion(@RequestBody JSONObject question){
        return questionService.addQuestion(question);
    }

    @GetMapping("/getQuestions/{currentPage}")//参数为页数，默认一页10条数据
    public JSONObject getQuestions(@PathVariable Integer currentPage){
        return questionService.getQuestions(currentPage);
    }

    @PostMapping("/updateQuestion")//（参数为id，question，answer）
    public JSONObject updateQuestion(@RequestBody JSONObject jsonObject){
        return questionService.updateQuestion(jsonObject);
    }

    @PostMapping("/deleteQuestion")//参数为id
    public JSONObject deleteQuestion(@RequestBody JSONObject jsonObject){
        return questionService.deleteQuestion(jsonObject);
    }

    @GetMapping("/getQuestions/{input}/{currentPage}")
    public JSONObject getQuestionsByLike(@PathVariable String input, @PathVariable Integer currentPage){
        System.out.println(input);
        return questionService.getQuestionsBylike(input,currentPage);
    }
}
