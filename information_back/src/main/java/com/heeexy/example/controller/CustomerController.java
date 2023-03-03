package com.heeexy.example.controller;


import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.service.CustomerService;
import com.heeexy.example.util.model.Message;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Resource
    private CustomerService customerServer;

    //获取留言信息
    @GetMapping("/getChatList/{status}/{currentPage}")//类型（已处理1，未处理0）//获取留言
    public JSONObject getChatList(@PathVariable(name = "status")Integer status,
                                  @PathVariable(name = "currentPage")Integer currentPage){

        return customerServer.getChatList(status, currentPage);
    }
    //添加留言信息
    @PostMapping("/addMessage")
    public JSONObject addMessage(@RequestBody Message message){
        return customerServer.addMessage(message.getContent(), message.getEmail(),message.getPhone());
    }

    @PostMapping("/updateMessage")
    public JSONObject updateMessage(@RequestBody JSONObject jsonObject){
        return customerServer.updateMessage(Integer.valueOf(jsonObject.getString("messageId")));
    }


}
