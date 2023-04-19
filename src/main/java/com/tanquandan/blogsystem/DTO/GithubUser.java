package com.tanquandan.blogsystem.DTO;

import lombok.Data;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

@Data
public class GithubUser {
    private String login;
    private Long id;
    private String bio;
    private String avatar_url;
}
