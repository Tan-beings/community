package com.tanquandan.blogsystem.controller;

import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.DTO.AccessToken;
import com.tanquandan.blogsystem.DTO.GithubUser;
import com.tanquandan.blogsystem.Provider.GithubProvider;
import com.tanquandan.blogsystem.Service.UserService;
import com.tanquandan.blogsystem.mapper.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    UserService userService;

    @Autowired
    UserMapper userMapper;


    @Value("${github.client.id}")
    private String ClientId;
    @Value("${github.client.secret}")
    private String ClientSecret;
    @Value("${github.redirect.uri}")
    private String RedirectUri;


    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response,
                           HttpServletRequest request) {
        AccessToken accessToken = new AccessToken();
        accessToken.setClient_id(ClientId);
        accessToken.setClient_secret(ClientSecret);
        accessToken.setCode(code);
        accessToken.setRedirect_uri(RedirectUri);
        accessToken.setState(state);

        String token = githubProvider.GetAccessToken(accessToken);
        GithubUser githubUser = githubProvider.GetUser(token);

        if (githubUser != null && githubUser.getId() != null) {
                User u = new User();
                u.setAccountId(githubUser.getId() + "");
                u.setName(githubUser.getLogin());
                u.setToken(UUID.randomUUID().toString());
                u.setAvatar(githubUser.getAvatar_url());
                userService.CreateOrUpdate(u);
                request.getSession().setAttribute("CurrentUser", u);
                response.addCookie(new Cookie("token", u.getToken()));

        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response,HttpSession session){
        // 1. 清除session
        session.removeAttribute("CurrentUser");
        // 2.清除cookie
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(-1);
        response.addCookie(cookie);

        return "redirect:/";
    }
}
