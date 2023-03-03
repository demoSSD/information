package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.heeexy.example.dao.CustomerDao;
import com.heeexy.example.service.CustomerService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomerServerImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;

    //通过信息状态获取留言列表
    @Override
    public JSONObject getChatList(Integer status, Integer currentPage) {
        PageHelper.startPage(currentPage,10);//原理就是startPage()分页起效果，只针对后面的第一个查询
        List<JSONObject> data = customerDao.getChatList(status);
        for(JSONObject d:data){
            String time = d.getString("messageTime");
            d.remove("messageTime");
            d.put("messageTime",time.substring(0,19));
        }
        PageInfo<JSONObject> pageInfo = new PageInfo<>(data);

        return CommonUtil.successJson(pageInfo);
    }

    //进行留言添加
    @Override
    public JSONObject addMessage(String content, String email,String phone) {
        customerDao.addMessage(content,email,phone);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject updateMessage(Integer messageId) {
        if(messageId==null){
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
        customerDao.updateMessage(messageId);
        return CommonUtil.successJson();
    }




}
