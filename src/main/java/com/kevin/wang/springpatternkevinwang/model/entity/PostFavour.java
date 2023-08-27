package com.kevin.wang.springpatternkevinwang.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wang
 * @create 2023-2023-21-0:01
 */
@Data
@Accessors(chain = true)
@TableName(value = "post_favour")
public class PostFavour implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "userId")
    private Long userId;

    @TableField(value = "postId")
    private Long postId;

    @TableField(value = "createTime")
    private Date createTime;

    @TableField(value = "updateTime")
    private Date updateTime;

    private static final long serialVersionUID = 1l;
}
