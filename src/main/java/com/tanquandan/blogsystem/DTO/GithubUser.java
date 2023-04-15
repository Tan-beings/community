package com.tanquandan.blogsystem.DTO;

import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

public class GithubUser {
    private String login;
    private Long id;
    private String bio;

    public GithubUser(){

    }

    public GithubUser(String login, Long id, String bio) {
        this.login = login;
        this.id = id;
        this.bio = bio;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
