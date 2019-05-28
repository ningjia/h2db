package com.example.springboot.h2db;

import com.example.springboot.h2db.model.User;
import com.example.springboot.h2db.repository.UserRepository;
//import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

//    @Test
//    public void saveTest() throws Exception {
//        User user = new User();
//        user.setUserName("测试用户");
//        User result = userRepository.save(user);
//        log.info(result.toString());
//        Assert.assertNotNull(user.getUser_id());
//    }
//
//    @Test
//    public void findOneTest() throws Exception{
//        User user = userRepository.findOne(1l);
//        log.info(user.toString());
//        Assert.assertNotNull(user);
//        Assert.assertTrue(1l==user.getUser_id());
//    }

    @Test
    public void findUserTest() throws Exception{
        //插入测试数据
        User user = new User();
        user.setUserName("测试用户");
        user.setUserAge(20);
        user.setCreateTime(new Date());
        User result = userRepository.save(user);
        Assert.assertNotNull(user.getUser_id());
        log.info(result.toString());
        //查找用户数据
        List<User> users = userRepository.findByUserName("测试用户");
        Assert.assertTrue(users.size()>0);
        Assert.assertTrue(20==users.get(0).getUserAge());
        log.info(users.get(0).toString());
    }
}
