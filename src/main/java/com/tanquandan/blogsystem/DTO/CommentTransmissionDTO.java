package com.tanquandan.blogsystem.DTO;

import lombok.Data;

@Data
public class CommentTransmissionDTO {
    public Long parentId;
    public Integer type;
    public String content;
    public String receiver;
}
