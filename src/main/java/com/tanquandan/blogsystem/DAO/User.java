package com.tanquandan.blogsystem.DAO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class User {
    private int id;
    private String account_id;
    private String name;
    private String token;
    private Long gmt_create;
    private Long gmt_modified;
    private String bio;
    private String avatar;

    public User( String account_id, String name, String token, Long gmt_create, Long gmt_modified) {

        this.account_id = account_id;
        this.name = name;
        this.token = token;
        this.gmt_create = gmt_create;
        this.gmt_modified = gmt_modified;
    }
}
