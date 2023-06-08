package com.tanquandan.blogsystem.Enums;

public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);

   private final int id;
   CommentTypeEnum(int id){
        this.id = id;
    }
   public int getType(){
       return this.id;
   }


}
