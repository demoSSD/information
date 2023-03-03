package com.heeexy.example.util.old;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class UploadUtil {

    /**
     *
     * @param baseUrl 文件要保存的目录
     * @param file 要保存的文件（MultipartFile）
     * @param type 上传的文件类型（img、video、file）
     * @param baseApi 返回前端地址
     * @return 包含文件名（filePath）和文件处理结果信息（msg）的JSONObject对象
     */
    public static JSONObject uploadFile(String baseUrl, MultipartFile file, String type, String baseApi){
        JSONObject result = new JSONObject();
        String filePath = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date())
                +"_"
                +file.getOriginalFilename();
        //result.put("filePath", File.separator+type+ File.separator+filePath);
        JSONObject data = new JSONObject();
        data.put("url","/api/resources/"+type+"/"+filePath);//返回前端地址
        filePath = baseUrl + filePath;//存储本地地址
        //创建文件
        File f = new File(filePath);
        //判断文件是否存在，存在则不处理,否则保存文件到本地
        String message = "请求成功！";
        if(f.exists()){
            message = "文件已存在！";
            result.put("message",message);
            result.put("errno",1);
            return result;
        }else{
            if(!f.getParentFile().exists()){
                boolean flag = f.getParentFile().mkdirs();
                log.info("\ncreate "+f.getParent()+": "+flag+"\n");
            }
            try {
                file.transferTo(f);
                log.info("\ncourse picture saved at "+f.getAbsolutePath()+" success!\n");
            }catch(IOException e){
                e.printStackTrace();
                message = "文件上传失败！(可能原因:服务器内存已满)";
                result.put("message",message);
                result.put("errno",1);
                return result;
            }
        }
        result.put("msg",message);
        result.put("errno",0);
        result.put("data",data);
        return result;
    }
}
