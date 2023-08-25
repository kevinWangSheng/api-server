package com.kevin.wang.springpatternkevinwang.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wang
 * @create 2023-2023-20-23:28
 */
@Data
@TableName(value = "post")
public class Post implements Serializable {
    @TableId(type= IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "title")
    private String title;

    @TableField(value = "content")
    private String content;

    @TableField(value = "tags")
    private String tags;

    @TableField(value = "thumbNum")
    private Integer thumbNum;

    @TableField(value = "favourNum")
    private Integer favourNum;

    @TableField(value = "userId")
    private Long userId;

    @TableField(value = "createTime")
    private Date createTime;

    @TableField(value = "updateTime")
    private Date updateTime;

    @TableField(value = "isDelete")
    private int isDelete;

    private static final long serialVersionUID = 1l;



}
