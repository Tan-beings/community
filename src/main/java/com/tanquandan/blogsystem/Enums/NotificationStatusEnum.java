package com.tanquandan.blogsystem.Enums;

public enum NotificationStatusEnum {
    READ(1),
    UNREAD(0);

    int id;
    NotificationStatusEnum(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }


}
