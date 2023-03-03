package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    JSONObject listUser(JSONObject jsonObject);
    JSONObject addUser(JSONObject jsonObject);
    JSONObject getAllRoles();
    JSONObject updateUser(JSONObject jsonObject);
    JSONObject listRole();
    JSONObject listAllPermission();
    JSONObject addRole(JSONObject jsonObject);
    JSONObject updateRole(JSONObject jsonObject);
    JSONObject deleteRole(JSONObject jsonObject);

    JSONObject addImage(MultipartFile file,String UserId);
    JSONObject getUserInfo(JSONObject val);
    JSONObject updateUserInfo(JSONObject val);
    JSONObject newAddUser(JSONObject val);
    JSONObject upDatePassWord(JSONObject val);
}
