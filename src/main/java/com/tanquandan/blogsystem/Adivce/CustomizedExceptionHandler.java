package com.tanquandan.blogsystem.Adivce;

import com.alibaba.fastjson2.JSON;
import com.tanquandan.blogsystem.DTO.RequestDTO;
import com.tanquandan.blogsystem.Exception.CustomizedErrorCode;
import com.tanquandan.blogsystem.Exception.CustomizedErrorController;
import com.tanquandan.blogsystem.Exception.CustomizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(CustomizedException.class)
        ModelAndView handleControllerException(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
            if(request.getContentType().equals("application/json")){
                // 处理ajax请求
                // 如果是自定义的请求
                RequestDTO requestDTO = null;
                if(ex instanceof CustomizedException){
                    requestDTO =  RequestDTO.errorOf((CustomizedException) ex);
                }else{
                    // 如果是服务器或者浏览器错误
                    requestDTO = RequestDTO.errorOf(CustomizedErrorCode.SYS_ERROR);
                }
                try{
                    response.setContentType("application/json");
                    response.setStatus(200);
                    response.setCharacterEncoding("utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(JSON.toJSONString(requestDTO));
                    writer.close();
                }catch(IOException ioe){

                }
                return null;
            }else{
                Map<String,String> model = new HashMap<>();
                if(ex instanceof CustomizedException){
                    model.put("message", CustomizedErrorCode.QUESTION_NOT_FOUND.getMessage());
                    return new ModelAndView("error", model);
                }
                model.put("message", CustomizedErrorCode.SYS_ERROR.getMessage());
                    return new ModelAndView("error",model);
                }

        }


    }
