package com.kevin.wang.springpatternkevinwang.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author wang
 * @create 2023-2023-21-0:29
 */
@Data
public class LoginUserVO {

    private Long id;

    private String userName;

    private String userAvatar;

    private String userProfile;

    private String userRole;

    private Date createTime;

    private Date updateTime;

    private String salt;

    private static final long serialVersionUID = 1L;
}
