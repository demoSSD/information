package com.heeexy.example.util.old;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UploadProperties {
    @Value("${baseUrl.api}")
    private String baseApi;

    @Value("${baseUrl.os.windows.root}")
    private String windowsRoot;

    @Value("${baseUrl.os.linux.root}")
    private String linuxRoot;

    @Value("${baseUrl.os.windows.image}")
    private String windowsImage;

    @Value("${baseUrl.os.windows.file}")
    private String windowsFile;

    @Value("${baseUrl.os.windows.video}")
    private String windowsVideo;

    @Value("${baseUrl.os.linux.image}")
    private String linuxImage;

    @Value("${baseUrl.os.linux.file}")
    private String linuxFile;

    @Value("${baseUrl.os.linux.video}")
    private String linuxVideo;

    public String getBaseApi(){
        return baseApi;
    }

    public String getRoot() {
        String location = "";
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")) {
            location = windowsRoot;
        } else {
            location = linuxRoot;
        }
        return location;
    }

    public String getBaseImage() {
        String location = "";
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")) {
            location = windowsImage;
        } else {
            location = linuxImage;
        }
        return location;
    }

    public String getBaseFile() {
        String location = "";
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")) {
            location = windowsFile;
        } else {
            location = linuxFile;
        }
        return location;
    }

    public String getBaseVideo() {
        String location = "";
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")) {
            location = windowsVideo;//上面定义引入yml中位置
        } else {
            location = linuxVideo;
        }
        return location;
    }
}
