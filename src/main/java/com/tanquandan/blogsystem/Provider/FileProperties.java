package com.tanquandan.blogsystem.Provider;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.ufile")
public class FileProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName ;
    private String key ;
}
