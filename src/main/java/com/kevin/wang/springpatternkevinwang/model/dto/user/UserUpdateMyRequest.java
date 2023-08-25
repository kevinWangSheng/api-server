package com.kevin.wang.springpatternkevinwang.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wang
 * @create 2023-2023-21-11:51
 */
@Data
public class UserUpdateMyRequest implements Serializable {

    private String userName;


    private String userAvatar;

    private String profile;

    private static final long serialVersionUID = 1L;
}
