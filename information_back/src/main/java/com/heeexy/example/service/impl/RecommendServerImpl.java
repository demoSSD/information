package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.heeexy.example.dao.GetAnswerDao;
import com.heeexy.example.service.RecommendService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import com.heeexy.example.util.old.recommendService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendServerImpl implements RecommendService {
    @Resource
    private GetAnswerDao answerDao;


    //与算法连接，获取推荐的问题列表
    @Override
    public JSONObject getRecommendList(String content) {
        TTransport transport = null;
        JSONObject jsonObject = new JSONObject();
        try {
            transport = new TSocket("127.0.0.1",9999 ,10000 );//端口连接
            TProtocol protocol = new TBinaryProtocol(transport);
            recommendService.Client client = new recommendService.Client(protocol);
            transport.open();//建立连接
            String result = client.say(content);//结果返回
            if(getRecommendResultList(result) != null){//如果没有相似则返回空
                jsonObject.put("recommendList",getRecommendResultList(result));

            }else {
                jsonObject.put("recommendList","");
            }
            jsonObject.put("tip","诶~人家没看懂耶，要不您换个说法呢~");
            jsonObject.put("error","");
            return CommonUtil.successJson(jsonObject);
        } catch (Exception e) {
//            e.printStackTrace();
            jsonObject.put("recommendList","");
            jsonObject.put("tip","诶~人家没看懂耶，要不您换个说法呢~");
            jsonObject.put("error","算法端没有连接上");
            return CommonUtil.errorJson(ErrorEnum.E_10011);
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }

    @Override
    public JSONObject getList() {//获取初始推荐列表
        List<JSONObject> data = answerDao.getList();
        List<String> list = new ArrayList<>();
        for(JSONObject d: data){
            list.add(d.getString("question"));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("recommendList",list);
        return CommonUtil.successJson(jsonObject);
    }

    //进行算法端获取的数据进行分割获取数据
    public List<String> getRecommendResultList(String resultSequence){
        if("0".equals(resultSequence)){
            return null;
        }
        String[] result = resultSequence.split("-");
        List<String> sequence = new ArrayList<>();
        for (String item : result) {
            sequence.add(item);
        }
        return sequence;
    }
}
