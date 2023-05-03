package com.tanquandan.blogsystem.Service;

import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.Mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    // 如果存在User则更新，不存在则插入
    public int CreateOrUpdate(User user){
        User u_check = userMapper.findById(user.getAccount_id());
        int res = 0;
        if(u_check==null){
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(System.currentTimeMillis());
            res = userMapper.insert(user);
        }
        else if(u_check != null){
            u_check.setAvatar(user.getAvatar());
            u_check.setName(user.getName());
            u_check.setToken(user.getToken());
            u_check.setAccount_id(user.getAccount_id());
            res = userMapper.update(u_check);
        }
        return res;

    }

}
