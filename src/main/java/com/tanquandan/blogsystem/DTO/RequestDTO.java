package com.tanquandan.blogsystem.DTO;

import com.tanquandan.blogsystem.Exception.CustomizedErrorCode;
import com.tanquandan.blogsystem.Exception.CustomizedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    public int code;
    public String message;

    public static RequestDTO errorOf(int code,String message){
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setCode(code);
        requestDTO.setMessage(message);
        return requestDTO;
    }


    public static RequestDTO errorOf(CustomizedException e){
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setMessage(e.getMessage());
        requestDTO.setCode(e.getCode());
        return requestDTO;
    }

    public static RequestDTO errorOf(CustomizedErrorCode customizedErrorCode){
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setCode(customizedErrorCode.getCode());
        requestDTO.setMessage(customizedErrorCode.getMessage());
        return requestDTO;
    }

    public static RequestDTO okOf(){
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setCode(200);
        return requestDTO;
    }
}
