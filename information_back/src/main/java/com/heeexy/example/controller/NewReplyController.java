package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.config.annotation.RequiresPermissions;
import com.heeexy.example.service.NewQuestionService;
import com.heeexy.example.service.NewReplyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/new_reply")
public class NewReplyController {
    @Resource
    private NewReplyService newReplyService;

    //添加 一条评论
    @PostMapping("/addReply")
    public JSONObject addQuestion(@RequestBody JSONObject reply){
        return newReplyService.addReply(reply);
    }

    //根据问题id得到回复
    @PostMapping("/getQuestionReply")
    public JSONObject getQuestionReply(@RequestBody JSONObject val){
        return newReplyService.getQuestionReply(val);
    }

    @PostMapping("/getUserReply")
    public JSONObject getUserReply(@RequestBody JSONObject val){
        return newReplyService.getUserReply(val);
    }

    @PostMapping("/updateReply")
    public JSONObject updateReply(@RequestBody JSONObject val){
        return newReplyService.updateAccept(val);
    }


    @RequiresPermissions("questionControl:deleteRep")
    @PostMapping("/deleteReply")
    public JSONObject deleteReply(@RequestBody JSONObject val){
        return newReplyService.updateAccept(val);
    }
}
