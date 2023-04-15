package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.DTO.AccessToken;
import com.tanquandan.blogsystem.DTO.GithubUser;
import com.tanquandan.blogsystem.Mapper.UserMapper;
import com.tanquandan.blogsystem.Provider.GithubProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class AuthController {

    @Autowired
    GithubProvider githubProvider;

    @Autowired
    UserMapper userMapper;



    @Value("${github.client.id}")
    private String ClientId;
    @Value("${github.client.secret}")
    private String ClientSecret;
    @Value("${github.redirect.uri}")
    private String RedirectUri;


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletResponse response){
        AccessToken accessToken = new AccessToken();
        accessToken.setClient_id(ClientId);
        accessToken.setClient_secret(ClientSecret);
        accessToken.setCode(code);
        accessToken.setRedirect_uri(RedirectUri);
        accessToken.setState(state);

        String token = githubProvider.GetAccessToken(accessToken);
        GithubUser githubUser = githubProvider.GetUser(token);
        System.out.println("name: "+githubUser.getLogin());
        if(githubUser != null && githubUser.getId() != null){
            User u = new User();
            u.setAccount_id(githubUser.getId()+"");
            u.setName(githubUser.getLogin());
            u.setToken(UUID.randomUUID().toString());
            u.setGmt_create(System.currentTimeMillis());
            u.setGmt_modified(u.getGmt_create());
            u.setBio(githubUser.getBio());
            userMapper.insert(u);
            response.addCookie(new Cookie("token",u.getToken()));
        }

        return "redirect:/";
    }
}
