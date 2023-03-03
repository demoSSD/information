package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heeexy.example.dao.QuestionDao;
import com.heeexy.example.service.QuestionService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import com.heeexy.example.util.old.UploadProperties;
import com.heeexy.example.util.old.UploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;


@Service
public class QuestionServiceImpl implements QuestionService {

    @Resource
    private UploadProperties uploadProperties;

    @Resource
    private QuestionDao questionDao;

    @Override//保存上传图片
    public JSONObject addImage(MultipartFile file) {
        //上传图片到服务器保存
        JSONObject res = UploadUtil.uploadFile(uploadProperties.getBaseImage(), file, "img",uploadProperties.getBaseApi());
        return res;
    }

    @Override
    public JSONObject addQuestion(JSONObject question) {
        if(questionDao.addQuestion(question)==1){
            return CommonUtil.successJson("");
        }else {
            return CommonUtil.errorJson(ErrorEnum.E_400);
        }
    }

    @Override
    public JSONObject getQuestions(Integer currentPage) {
        PageHelper.startPage(currentPage,10);//原理就是startPage()分页起效果，只针对后面的第一个查询
        List<JSONObject> data = questionDao.getQuestion();
        for(JSONObject d:data){
            String time = d.getString("create_time");
            d.remove("create_time");
            d.put("create_time",time.substring(0,19));
        }
        PageInfo<JSONObject> pageInfo = new PageInfo<>(data);
        return CommonUtil.successJson(pageInfo);
    }

    @Override
    public JSONObject deleteQuestion(JSONObject jsonObject) {
        if(questionDao.deleteQuestion(jsonObject)==1){
            return CommonUtil.successJson("");
        }
        return CommonUtil.errorJson(ErrorEnum.E_400);
    }

    @Override
    public JSONObject updateQuestion(JSONObject jsonObject) {
        if(questionDao.updateQuestion(jsonObject) == 1){
            return CommonUtil.successJson("");
        }
        return CommonUtil.errorJson(ErrorEnum.E_400);
    }

    @Override
    public JSONObject getQuestionsBylike(String input, Integer currentPage) {
        PageHelper.startPage(currentPage,10);//原理就是startPage()分页起效果，只针对后面的第一个查询
        List<JSONObject> data = questionDao.getQuestionByLike(input);
        for(JSONObject d:data){
            String time = d.getString("create_time");
            d.remove("create_time");
            d.put("create_time",time.substring(0,19));
        }
        PageInfo<JSONObject> pageInfo = new PageInfo<>(data);
        return CommonUtil.successJson(pageInfo);
    }


}
