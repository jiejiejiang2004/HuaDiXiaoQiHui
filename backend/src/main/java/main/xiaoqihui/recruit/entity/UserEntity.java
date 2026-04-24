package main.xiaoqihui.recruit.entity;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class UserEntity {
    private Long userId;
    private String mobile;
    private String password;
    private String realName;
    private String email;
    private String avatar;
    private String identityType;
    private String userType;
    private String status;
    private String currentCity;
    private String school;
    private String major;
    private Integer graduationYear;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
