package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface VoiceTextService {
    JSONObject uploadVoice(MultipartFile[] file,String temId,String fileName);
    JSONObject voiceToText(JSONObject json);
}
