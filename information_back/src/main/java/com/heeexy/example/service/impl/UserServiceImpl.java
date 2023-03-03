package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.UserDao;
import com.heeexy.example.service.UserService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import com.heeexy.example.util.model.User;
import com.heeexy.example.util.old.UploadProperties;
import com.heeexy.example.util.old.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author: heeexy
 * @description: 用户/角色/权限
 * @date: 2017/11/2 10:18
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Resource
    private UploadProperties uploadUser;
    /**
     * 用户列表
     */
    public JSONObject listUser(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = userDao.countUser(jsonObject);
        List<JSONObject> list = userDao.listUser(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 添加用户
     */
    public JSONObject addUser(JSONObject jsonObject) {
        int exist = userDao.queryExistUsername(jsonObject);
        if (exist > 0) {
            return CommonUtil.errorJson(ErrorEnum.E_10009);
        }
        userDao.addUser(jsonObject);
        userDao.batchAddUserRole(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 查询所有的角色
     * 在添加/修改用户的时候要使用此方法
     */
    public JSONObject getAllRoles() {
        List<JSONObject> roles = userDao.getAllRoles();
        return CommonUtil.successPage(roles);
    }

    /**
     * 修改用户
     */
    public JSONObject updateUser(JSONObject jsonObject) {
        //不允许修改管理员信息
        if (jsonObject.getIntValue("userId") == 10001) return CommonUtil.successJson();
        userDao.updateUser(jsonObject);
        userDao.removeUserAllRole(jsonObject.getIntValue("userId"));
        if (!jsonObject.getJSONArray("roleIds").isEmpty()) {
            userDao.batchAddUserRole(jsonObject);
        }
        return CommonUtil.successJson();
    }

    /**
     * 角色列表
     */
    public JSONObject listRole() {
        List<JSONObject> roles = userDao.listRole();
        int a=1;
        return CommonUtil.successPage(roles);
    }

    /**
     * 查询所有权限, 给角色分配权限时调用
     */

    public JSONObject listAllPermission() {
        List<JSONObject> permissions = userDao.listAllPermission();
        return CommonUtil.successPage(permissions);
    }

    /**
     * 添加角色
     */
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    public JSONObject addRole(JSONObject jsonObject) {
        userDao.insertRole(jsonObject);
        userDao.insertRolePermission(jsonObject.getString("roleId"), (List<Integer>) jsonObject.get("permissions"));
        return CommonUtil.successJson();
    }

    /**
     * 修改角色
     */
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    public JSONObject updateRole(JSONObject jsonObject) {
        String roleId = jsonObject.getString("roleId");
        List<Integer> newPerms = (List<Integer>) jsonObject.get("permissions");
        JSONObject roleInfo = userDao.getRoleAllInfo(jsonObject);
        Set<Integer> oldPerms = (Set<Integer>) roleInfo.get("permissionIds");
        //修改角色名称
        dealRoleName(jsonObject, roleInfo);
        //添加新权限
        saveNewPermission(roleId, newPerms, oldPerms);
        //移除旧的不再拥有的权限
        removeOldPermission(roleId, newPerms, oldPerms);
        return CommonUtil.successJson();
    }

    /**
     * 修改角色名称
     */
    private void dealRoleName(JSONObject paramJson, JSONObject roleInfo) {
        String roleName = paramJson.getString("roleName");
        if (!roleName.equals(roleInfo.getString("roleName"))) {
            userDao.updateRoleName(paramJson);
        }
    }

    /**
     * 为角色添加新权限
     */
    private void saveNewPermission(String roleId, Collection<Integer> newPerms, Collection<Integer> oldPerms) {
        List<Integer> waitInsert = new ArrayList<>();
        for (Integer newPerm : newPerms) {
            if (!oldPerms.contains(newPerm)) {
                waitInsert.add(newPerm);
            }
        }
        if (waitInsert.size() > 0) {
            userDao.insertRolePermission(roleId, waitInsert);
        }
    }

    /**
     * 删除角色 旧的 不再拥有的权限
     */
    private void removeOldPermission(String roleId, Collection<Integer> newPerms, Collection<Integer> oldPerms) {
        List<Integer> waitRemove = new ArrayList<>();
        for (Integer oldPerm : oldPerms) {
            if (!newPerms.contains(oldPerm)) {
                waitRemove.add(oldPerm);
            }
        }
        if (waitRemove.size() > 0) {
            userDao.removeOldPermission(roleId, waitRemove);
        }
    }

    /**
     * 删除角色
     */
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    public JSONObject deleteRole(JSONObject jsonObject) {
        JSONObject roleInfo = userDao.getRoleAllInfo(jsonObject);
        List<JSONObject> users = (List<JSONObject>) roleInfo.get("users");
        if (users != null && users.size() > 0) {
            return CommonUtil.errorJson(ErrorEnum.E_10008);
        }
        userDao.removeRole(jsonObject);
        userDao.removeRoleAllPermission(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject newAddUser(JSONObject val) {
        int isExit=userDao.isExit(val);
        int res=0;
        if(isExit==1)
            return CommonUtil.errorJson(ErrorEnum.E_10009);
        else{
            User user=new User();
            user.setUsername(val.getString("userId"));
            user.setNickname(val.getString("userId"));
            user.setPassword(val.getString("password"));
            res=userDao.newAddUser(user);
            userDao.newAddUserRole(user);
        }

        return res==1?CommonUtil.successJson("注册成功，请重新登录"):CommonUtil.errorJson(ErrorEnum.E_400);
    }

    @Override
    public JSONObject addImage(MultipartFile file,String userId) {
       JSONObject json=new JSONObject();
        if(file.isEmpty())
            return CommonUtil.errorJson(ErrorEnum.E_20013);
        else{
            //保存时的文件名
            String dateName=userId+file.getOriginalFilename();
            //保存文件的绝对路径
            String filePath = uploadUser.getBaseImage()+dateName;
            File newFile = new File(filePath);
            try {
                file.transferTo(newFile);
                String url="/resources/img/"+dateName;
                json.put("relativeUrl",url);
            } catch (IOException e) {
                e.printStackTrace();
                return CommonUtil.errorJson(ErrorEnum.E_20013);
            }
            return CommonUtil.successJson(json);
        }
    }

    @Override
    public JSONObject getUserInfo(JSONObject val) {
        JSONObject res=userDao.getUserInfo(val);
        res.put("replys",userDao.getUserReplyCount(val));
        res.put("questions",userDao.getUserQuestionCount(val));
        res.put("approve",userDao.getUserApproveCount(val));
        return CommonUtil.successJson(res);
    }

    @Override
    public JSONObject updateUserInfo(JSONObject val) {
        int res=userDao.updateUserInfo(val);
        return res==1?CommonUtil.successJson("修改成功"):CommonUtil.errorJson(ErrorEnum.E_400);
    }

    @Override
    public JSONObject upDatePassWord(JSONObject val) {
        int tem=userDao.isCorrect(val);
        int res=0;
        if(tem!=1){
            return CommonUtil.errorJson(ErrorEnum.E_10010);
        }else{
            res=userDao.upDatePassWord(val);
        }
        return res==1?CommonUtil.successJson("修改成功"):CommonUtil.errorJson(ErrorEnum.E_400);
    }


}
