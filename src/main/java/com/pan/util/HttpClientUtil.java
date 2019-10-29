package com.pan.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpClientUtil {
    /**
     * 发送xml请求到server端
     * @param url xml请求数据地址
     * @param xmlString 发送的xml数据流
     * @param charset 字符编码 utf-8/gbk
     * @return null发送失败，否则返回响应内容
     */
    public static String sendPost(String url, String xmlString, String charset){
        //创建httpclient工具对象
        HttpClient client = new HttpClient();
        //创建post请求方法
        PostMethod myPost = new PostMethod(url);
        //设置请求超时时间
        client.setConnectionTimeout(3000*1000);
        String responseString = null;
        try{
            //设置请求头部类型
//            myPost.setRequestHeader("Content-Type","text/xml");
            myPost.setRequestHeader("content-type","application/x-www-form-urlencoded");
            myPost.setRequestHeader("charset",charset);
            //设置请求体，即xml文本内容，一种是直接获取xml内容字符串，一种是读取xml文件以流的形式
            myPost.setRequestBody(xmlString);
//            UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(new HashMap<String,Object>(), "utf-8");
            int statusCode = client.executeMethod(myPost);
            //只有请求成功200了，才做处理
            if(statusCode == HttpStatus.SC_OK){
                InputStream inputStream = myPost.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,charset));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                responseString = stringBuffer.toString();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            myPost.releaseConnection();
        }
        return responseString;
    }
}
