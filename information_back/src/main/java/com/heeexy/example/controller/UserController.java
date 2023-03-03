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
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 查询用户列表
     */
    @RequiresPermissions("user:list")
    @GetMapping("/list")
    public JSONObject listUser(HttpServletRequest request) {
        return userService.listUser(CommonUtil.request2Json(request));
    }

    @RequiresPermissions("user:add")
    @PostMapping("/addUser")
    public JSONObject addUser(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "username, password, nickname, roleIds");
        return userService.addUser(requestJson);
    }

    @RequiresPermissions("user:update")
    @PostMapping("/updateUser")
    public JSONObject updateUser(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, " nickname, roleIds, deleteStatus, userId");
        return userService.updateUser(requestJson);
    }

    @RequiresPermissions(value = {"user:add", "user:update"}, logical = Logical.OR)
    @GetMapping("/getAllRoles")
    public JSONObject getAllRoles() {
        return userService.getAllRoles();
    }

    /**
     * 角色列表
     */
    @RequiresPermissions("role:list")
    @GetMapping("/listRole")
    public JSONObject listRole() {
        return userService.listRole();
    }

    /**
     * 查询所有权限, 给角色分配权限时调用
     */
    @RequiresPermissions("role:list")
    @GetMapping("/listAllPermission")
    public JSONObject listAllPermission() {
        return userService.listAllPermission();
    }

    /**
     * 新增角色
     */
    @RequiresPermissions("role:add")
    @PostMapping("/addRole")
    public JSONObject addRole(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "roleName,permissions");
        return userService.addRole(requestJson);
    }

    /**
     * 修改角色
     */
    @RequiresPermissions("role:update")
    @PostMapping("/updateRole")
    public JSONObject updateRole(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "roleId,roleName,permissions");
        return userService.updateRole(requestJson);
    }

    /**
     * 删除角色
     */
    @RequiresPermissions("role:delete")
    @PostMapping("/deleteRole")
    public JSONObject deleteRole(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "roleId");
        return userService.deleteRole(requestJson);
    }

    @PostMapping("/addImage")//参数为图片
    public JSONObject addImage(@RequestParam("file") MultipartFile file,@RequestParam("userId") String userId){
        return userService.addImage(file,userId);
    }

    @PostMapping("/getUserInfo")
    public JSONObject getUserInfo(@RequestBody JSONObject requestJson){
        return userService.getUserInfo(requestJson);
    }

    @PostMapping("/updateUserInfo")
    public JSONObject updateUserInfo(@RequestBody JSONObject requestJson){
        return userService.updateUserInfo(requestJson);
    }

    @PostMapping("/newAddUser")
    public JSONObject newAddUser(@RequestBody JSONObject val){
        return userService.newAddUser(val);
    }

    @PostMapping("/upDatePassWord")
    public JSONObject upDatePassWord(@RequestBody JSONObject val){
        return userService.upDatePassWord(val);
    }

}
