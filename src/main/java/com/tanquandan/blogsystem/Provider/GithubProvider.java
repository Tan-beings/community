package com.tanquandan.blogsystem.Provider;

import com.alibaba.fastjson2.JSON;
import com.tanquandan.blogsystem.DTO.AccessToken;
import com.tanquandan.blogsystem.DTO.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class GithubProvider {

    public String GetAccessToken(AccessToken accessToken){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        try {
            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessToken));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                // access_token=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&scope=user&token_type=bearer
                String tokenstr =  response.body().string().split("&")[0];
                return tokenstr.split("=")[1];
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser GetUser(String AccessToken) {
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url("https://api.github.com/user")
                    .header("Authorization","token "+ AccessToken)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String str = response.body().string();
                System.out.println(str);
                System.out.println("AccessToken: "+str);
                return JSON.parseObject(str, GithubUser.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

}
