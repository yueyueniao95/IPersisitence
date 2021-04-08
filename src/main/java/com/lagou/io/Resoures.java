package com.lagou.io;

import java.io.InputStream;

public class Resoures {

    //将配置文件已流的方式加载到内存
    public static InputStream getInputStream(String path){
        InputStream inputStream = Resoures.class.getClassLoader().getResourceAsStream(path);
        return inputStream;
    }

}
