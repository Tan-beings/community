package com.tanquandan.blogsystem.Exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomizedErrorController extends BasicErrorController {
    public CustomizedErrorController(){
        super(new DefaultErrorAttributes(),new ErrorProperties());
    }

    public CustomizedErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties){
        super(errorAttributes, errorProperties);

    }

    public CustomizedErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorProperties, errorViewResolvers);
    }

    @Override
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Map<String, Object> model = new HashMap<>();
        if(status.is4xxClientError()){
            // 浏览器错误
            model.put("message","浏览器发送的请求有问题哦~");
        }

        if(status.is5xxServerError()){
            // 服务器错误
            model.put("message","哎呀！服务器出现了问题，正在解决中~");
        }

        return new ModelAndView("error",model);
    }
}










