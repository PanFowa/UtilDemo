package com.pan.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private Properties properties;
    public PropertiesUtil(String fileName){
        this. properties = new Properties();
        InputStream stream = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(stream);
        } catch (IOException e) {
            logger.error("加载配置文件出错....",e);
        }
    }

    public String getString(String key){
        return this.properties.getProperty(key);
    }

    public Object getObject(String key){
        return this.properties.getProperty(key);
    }
}
