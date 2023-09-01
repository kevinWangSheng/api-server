package com.kevin.wang.springpatternkevinwang.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wang
 * @create 2023-2023-20-23:21
 */
@Data
@TableName(value = "user")
public class User implements Serializable {
    @TableId(type=IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "userAccount")
    private String userAccount;

    @TableField(value = "userPassword")
    private String userPassword;

    @TableField(value = "unionId")
    private String unionId;

    @TableField(value ="mpOpenId")
    private String mpOpenId;

    @TableField(value = "userAvatar")
    private String userAvatar;

    @TableField(value = "userRole")
    private String userRole;

    @TableField(value = "userName")
    private String userName;

    @TableField(value = "userProfile")
    private String userProfile;

    @TableField(value = "createTime")
    private Date createTime;

    @TableField(value = "updateTime")
    private Date updateTime;

    @TableLogic
    private Integer isDelete;

    @TableField(value="accessKey")
    private String accessKey;

    @TableField("secretKey")
    private String secretKey;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
