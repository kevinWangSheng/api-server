package com.kevin.wang.springpatternkevinwang.model.dto.user;

import com.kevin.wang.springpatternkevinwang.annotation.EncryptField;
import com.kevin.wang.springpatternkevinwang.annotation.EncryptFields;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wang
 * @create 2023-2023-21-11:46
 */
@Data
public class UserAddRequest implements Serializable {
    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;


    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    private String userPassword;

    private static final long serialVersionUID = 1L;
}
