package com.kevin.wang.springpatternkevinwang.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wang
 * @create 2023-2023-21-11:49
 */
@Data
public class UserRegistryRequest implements Serializable {
    private String userAccount;

    private String password;

    private String checkPassword;

    private static final long serialVersionUID = 1L;
}
