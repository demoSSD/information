package com.heeexy.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.heeexy.example.service.VoiceTextService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.constants.ErrorEnum;
import com.heeexy.example.util.old.Transmit;
import com.heeexy.example.util.old.UploadProperties;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Service
public class VoiceTextImpl implements VoiceTextService {

    @Resource
    private UploadProperties uploadPropertie;

    @Override
    public JSONObject uploadVoice(MultipartFile[] files,String temId,String name) {
        JSONObject json=new JSONObject();
        if(files.length==0)
            return CommonUtil.errorJson(ErrorEnum.E_90003);
        else{
            MultipartFile file=files[0];
            String fileName=temId+name;
            String filePath = uploadPropertie.getBaseFile()+fileName;
            File newFile = new File(filePath);
            try {
                file.transferTo(newFile);
               // String url="/resources/file/"+fileName;
                String url=filePath;
                json.put("relativeUrl",url);
            } catch (IOException e) {
                e.printStackTrace();
                return CommonUtil.errorJson(ErrorEnum.E_20013);
            }
            return CommonUtil.successJson(json);
        }
    }

    @Override
    public JSONObject voiceToText(JSONObject json) {
        String url=json.getString("url");
        System.out.println(url);

        TTransport transport = null;
        JSONObject jsonObject = new JSONObject();
        try {
                transport = new TSocket("127.0.0.1",9520 ,900000 );//端口连接
                TProtocol protocol = new TBinaryProtocol(transport);
                Transmit.Client client = new Transmit.Client(protocol);
                transport.open();//建立连接
                String result = client.sayMsg("123",url);//结果返回
                if(result != null){//如果没有相似则返回空
                    result= ZhConverterUtil.convertToSimple(result);
                    jsonObject.put("result",result);
                }else {
                    jsonObject.put("result","null");
                }
                return CommonUtil.successJson(jsonObject);
            } catch (Exception e) {
                return CommonUtil.errorJson(ErrorEnum.E_10011);
            } finally {
                if (null != transport) {
                    transport.close();
                }
            }
    }
}
