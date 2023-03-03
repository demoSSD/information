package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;

import com.heeexy.example.service.NewThumbsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/new_thumbs")
public class NewThumbsController {
    @Resource
    private NewThumbsService newThumbsService;

    //添加 一条评论
    @PostMapping("/addThumb")
    public JSONObject addThumb(@RequestBody JSONObject thumb){
        return newThumbsService.addThumb(thumb);
    }
    @PostMapping("/deleteThumb")
    public JSONObject deleteThumb(@RequestBody JSONObject thumb){
        return newThumbsService.deleteThumb(thumb);
    }
}
