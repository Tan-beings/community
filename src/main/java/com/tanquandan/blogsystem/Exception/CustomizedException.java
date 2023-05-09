package com.tanquandan.blogsystem.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.plaf.IconUIResource;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomizedException extends RuntimeException{
    public String message;
    public int code;
    public CustomizedException(CustomizedErrorCode customizedErrorCode){
        this.message = customizedErrorCode.message;
        this.code = customizedErrorCode.code;
    }

}
