package com.example.springboot.h2db.repository;

import com.example.springboot.h2db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    //自定义方法，按照userName查询用户数据
    List<User> findByUserName(String user_name);
}
