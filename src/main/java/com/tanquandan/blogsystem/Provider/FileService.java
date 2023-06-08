package com.tanquandan.blogsystem.Provider;

import ch.qos.logback.core.util.FileUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.tanquandan.blogsystem.Util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

@Service
public class FileService {
    @Autowired
    private FileProperties fileProperties;

    public FileResult upload(String filename, InputStream inputStream){
        String keyName = FileUtils.newUUIDFileName(filename);
        System.out.println(keyName);
        OSS ossClient = new OSSClientBuilder().build(fileProperties.getEndpoint(), fileProperties.getAccessKeyId(), fileProperties.getAccessKeySecret());
        ossClient.putObject(new PutObjectRequest(fileProperties.getBucketName(), keyName, inputStream));
        FileResult fileResult = new FileResult();
        fileResult.setFileName(keyName);
        fileResult.setFileUrl("https://"+fileProperties.getBucketName()+"."+fileProperties.getEndpoint()+"/"+keyName);
        return fileResult;
    }
}
