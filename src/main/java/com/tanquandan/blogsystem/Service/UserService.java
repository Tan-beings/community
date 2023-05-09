package com.tanquandan.blogsystem.Service;

import com.tanquandan.blogsystem.DAO.User;
import com.tanquandan.blogsystem.DAO.UserExample;
import com.tanquandan.blogsystem.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User findByAccountId(String accountId){
        UserExample selectUserExample = new UserExample();
        selectUserExample.createCriteria().andAccountIdEqualTo(accountId);
        List<User> users = userMapper.selectByExample(selectUserExample);
        if(users.size() <= 0) {
            return null;
        }
        return users.get(0);
    }

    public int updateById(User oldUser,User newUser){
        oldUser.setAvatar(newUser.getAvatar());
        oldUser.setName(newUser.getName());
        oldUser.setToken(newUser.getToken());
        oldUser.setAccountId(newUser.getAccountId());
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(oldUser.getAccountId());
        return userMapper.updateByExampleSelective(oldUser,userExample);
    }

    public User findByToken(String token){
        UserExample selectUserExample = new UserExample();
        selectUserExample.createCriteria().andTokenEqualTo(token);
        List<User> users = userMapper.selectByExample(selectUserExample);
        if(users.size() <= 0) {
            return null;
        }
        return users.get(0);
    }

    // 如果存在User则更新，不存在则插入
    public int CreateOrUpdate(User user){
        User u_check = findByAccountId(user.getAccountId());
        int res = 0;
        if(u_check == null){
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(System.currentTimeMillis());
            res = userMapper.insert(user);
        }
        else if(u_check != null){
           res = updateById(u_check,user);
        }
        return res;
    }

}
