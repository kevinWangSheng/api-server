package com.kevin.wang.springpatternkevinwang.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wang
 * @create 2023-2023-21-11:47
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;
    private String userAccount;

    private String userPassword;

}
