package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.UserService;
import com.heeexy.example.service.VoiceTextService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/voiceText")
public class VoiceTextController {

    @Resource
    private VoiceTextService voiceTextService;

    @PostMapping("/uploadVoice")//参数为图片
    public JSONObject uploadVoice(@RequestParam("file") MultipartFile[] file,@RequestParam("temId") String temId,@RequestParam("fileName") String fileName){
        return voiceTextService.uploadVoice(file,temId,fileName);
    }

    @PostMapping("/voiceToText")//参数为图片
    public JSONObject voiceToText(@RequestBody JSONObject json){
        return voiceTextService.voiceToText(json);
    }
}
