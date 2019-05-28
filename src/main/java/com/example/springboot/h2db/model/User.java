package com.example.springboot.h2db.model;

import lombok.Data;
import javax.persistence.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

@Entity
@Table(name = "t_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(name="user_name") //由于在使用JPA的findby方法时，名称中不能出现下划线，所以在此处将名称中的下划线去掉
    private String userName;

    @Column(name="user_age")
    private Integer userAge;

    @Column(name="create_time")
    private Date createTime;

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", userName='" + userName + '\'' +
                ", userAge=" + userAge +
                ", createTime=" + createTime +
                '}';
    }
}
