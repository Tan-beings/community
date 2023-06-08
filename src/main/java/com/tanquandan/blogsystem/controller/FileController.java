package com.tanquandan.blogsystem.controller;


import com.tanquandan.blogsystem.DTO.FileDTO;
import com.tanquandan.blogsystem.Exception.CustomizedErrorCode;
import com.tanquandan.blogsystem.Exception.CustomizedException;
import com.tanquandan.blogsystem.Provider.FileResult;
import com.tanquandan.blogsystem.Provider.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;

@Controller
public class FileController {

    @Autowired
    FileService fileService;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) throws IOException {
        MultipartRequest multipartRequest = (MultipartRequest)request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        FileDTO fileDTO = new FileDTO();
        if(file != null) {
            FileResult fileResult = fileService.upload(file.getOriginalFilename(), file.getInputStream());
            fileDTO.setSuccess(1);
            fileDTO.setUrl(fileResult.getFileUrl());
        }else{
            fileDTO.setSuccess(0);
            fileDTO.setMessage("上传失败");
            throw new CustomizedException(CustomizedErrorCode.FILE_READ_ERROR);
        }
        return fileDTO;
    }
}
