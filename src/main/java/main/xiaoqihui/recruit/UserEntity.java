package main.xiaoqihui.recruit;

import lombok.Data;

import java.time.LocalDateTime;

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
