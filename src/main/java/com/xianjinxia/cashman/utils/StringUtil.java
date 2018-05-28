package com.xianjinxia.cashman.utils;

import org.springframework.util.ClassUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by liquan on 2017/12/5.
 */
public class StringUtil {

    public static final String NULL_CHARACTER_STRING ="";

    /**
     * 读取模板
     * @return
     */
    public static String getFileTemplate(String url) throws IOException {
        try(FileReader in= new FileReader(url)){
            String s="";
            char[] content=new char[1024*1024];
            int length=0;
            while((length=in.read(content))>0){
                s+=new String(content,0,length);
            }
            return s;
        }
    }

    /**
     * 填充模板数据
     * @param templateStr
     * @param params
     * @return
     */
    public static String fillData(String templateStr,Map<String,String> params){
        StringBuilder sb=new StringBuilder();
        for(Map.Entry<String,String> entry:params.entrySet()){
            templateStr=templateStr.replaceAll(entry.getKey(),entry.getValue());
            sb.setLength(0);
        }
        return templateStr;

    }

    /**
     * 获取根目录
     * @return
     */
    public static String getRootPath(){
        return ClassUtils.getDefaultClassLoader().getResource("").getPath();
    }

    /**
     * 读取模板
     *
     * @return
     */
    public static String getFileTemplateNew(String url) throws IOException {
        InputStream is = null;
        BufferedReader in =null;
        StringBuffer str =new StringBuffer("");
        try {
            //返回读取指定资源的输入流
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(url);
            String s="";
            in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((s = in.readLine()) != null) {
                str.append(new StringBuffer(s));
            }
        } finally {
            try {
                is.close();
                in.close();
            } catch (IOException e) {
            }
        }
        ;
        return str.toString();
    }

    public static String getJarRootPath(){
        String realPath =Thread.currentThread().getContextClassLoader().getResource("").getPath();
        //去掉路径信息中的协议名"file:"
        int pos=realPath.indexOf("file:");
        if(pos>-1) {
            realPath=realPath.substring(pos+5);
        }
        //去掉路径信息最后包含类文件信息的部分，得到类所在的路径
        pos=realPath.indexOf("/BOOT-INF/classes");
        realPath=realPath.substring(0,pos);
        //如果类文件被打包到JAR等文件中时，去掉对应的JAR等打包文件名
        if(realPath.endsWith("!")){
            realPath=realPath.substring(0,realPath.lastIndexOf("/"));
        }
        try{
            realPath=java.net.URLDecoder.decode(realPath,"utf-8");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return realPath;
    }

}
