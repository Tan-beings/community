package com.tanquandan.blogsystem.Util;

import com.alibaba.fastjson2.util.UUIDUtils;
import com.tanquandan.blogsystem.Exception.CustomizedErrorCode;
import com.tanquandan.blogsystem.Exception.CustomizedException;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileUtils {
    private static final List<String> suffixs = new ArrayList<>();
    static{
        Collections.addAll(suffixs,"png","jpg","jpeg","bmp","wbep");
    }

    public static String newUUIDFileName(String filename){
        if(filename.equals("") ){
           return "";
        }
        String[] filepaths = filename.split("\\.");
        String suffix = filepaths[filepaths.length-1];
        if(!hasSuffix(suffix)){
            throw new CustomizedException(CustomizedErrorCode.FILENAME_ILLEGAL);
        }

        return newUUIDPNGName();
    }

    private static boolean hasSuffix(String suffix) {
        return suffixs.contains(suffix);
    }

    private static String newUUIDPNGName(){
        return UUID.randomUUID().toString().replace("-","")+".png";

    }
}
