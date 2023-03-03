package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.RecommendService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Resource
    private RecommendService recommendService;

    //通过内容获取推荐的信息
    @GetMapping("/getCommendList")
    public JSONObject recommendList(String content){
       return recommendService.getRecommendList(content);
    }
    //获取初始推荐
    @GetMapping("/getList")
    public JSONObject getList(){
        return recommendService.getList();
    }

    @GetMapping("/test")
    public JSONObject test(){
        System.out.println("////////////");
        String str="{name:'张三',age:22}";
        return CommonUtil.successJson(JSONObject.toJSON(str));
    }



}
