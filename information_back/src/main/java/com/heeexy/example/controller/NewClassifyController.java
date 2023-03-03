package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.NewClassifyService;
import com.heeexy.example.service.NewQuestionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/new_classify")
public class NewClassifyController {
    @Resource
    private NewClassifyService newClassifyService;

    //得到大类
    @GetMapping("/getAllClassify")
    public JSONObject addQuestion(){
        return newClassifyService.getAllClassify();
    }
    //得到标签
    @PostMapping("/getTags")
    public JSONObject getTags(@RequestBody JSONObject classify){
        return newClassifyService.getTags(classify);
    }
    //得到标签
    @GetMapping("/getAllTags")
    public JSONObject getAllTags(){
        return newClassifyService.getAllTags();
    }

    //批量添加分类
    @PostMapping("/addClassify")
    public JSONObject addClassify(@RequestBody JSONObject classify){
        return newClassifyService.addClassify(classify);}

    //批量添加标签
    @PostMapping("/addTagsByClassify")
    public JSONObject addTagsByClassify(@RequestBody JSONObject tags){
        return newClassifyService.addTagsByClassify(tags);}

    //删除标签
    @PostMapping("/deleteTag")
    public JSONObject deleteTag(@RequestBody JSONObject tag){
        return newClassifyService.deleteTag(tag);}
}
