package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.config.annotation.Logical;
import com.heeexy.example.config.annotation.RequiresPermissions;
import com.heeexy.example.service.UserService;
import com.heeexy.example.util.CommonUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: heeexy
 * @description: 用户/角色/权限相关controller
 * @date: 2017/11/2 10:19
 */
@RestController
@RequestMapping("/webset")
public class WebSettingController {
    @Resource
    private UserService userService;

    @RequiresPermissions("webSetting:backGroundImg")
    @PostMapping("/setBackImg")
    public JSONObject setBackImg(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, " nickname, roleIds, deleteStatus, userId");
        return userService.updateUser(requestJson);
    }
    @RequiresPermissions("webSetting:userDefImg")
    @PostMapping("/setUserImg")
    public JSONObject setUserImg(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, " nickname, roleIds, deleteStatus, userId");
        return userService.updateUser(requestJson);
    }
    @RequiresPermissions("webSetting:homeText")
    @PostMapping("/setHomeText")
    public JSONObject setHomeText(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, " nickname, roleIds, deleteStatus, userId");
        return userService.updateUser(requestJson);
    }
}
