package com.tanquandan.blogsystem.Exception;


public enum CustomizedErrorCode implements ICustomizedErrorCode{

    QUESTION_NOT_FOUND(1001,"你访问的问题已经走丢啦，试试其他问题吧~"),
    TARGET_PARAM_NOT_FOUND(1002,"未选择任何问题或者评论进行回复"),
    TYPE_PARAM_NULL(1003,"提交回复失败~麻烦再回复一遍吧"),
    NO_LOGIN(1004,"需要登录后才能回复哦亲"),
    COMMENT_NOT_FOUND(1005,"啊呀，你回复的评论已经被删除了"),
    SYS_ERROR(100,"啊哦，我们这边遇到了问题，正在加急补救中，请稍等~");

    int code;
    String message;

    CustomizedErrorCode(int code,String message){
        this.code = code;
        this.message = message;
    }


    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getCode() {
        return this.code;
    }


}
