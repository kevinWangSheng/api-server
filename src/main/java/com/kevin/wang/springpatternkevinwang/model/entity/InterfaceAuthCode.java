package com.kevin.wang.springpatternkevinwang.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 接口授权信息
 * @TableName interface_auth_code
 */
@Data
@TableName("interface_auth_code")
public class InterfaceAuthCode implements Serializable {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 接口信息id
     */
    @TableField("interface_info_id")
    private Long interfaceInfoId;

    /**
     * 客户端appcode，授权使用
     */
    @TableField("app_code")
    private String appCode;

    /**
     * 客户端key,暂时表示一个接口信息整体，没用到现在
     */
    @TableField("app_key")
    private String appKey;

    /**
     * 客户端用户的秘钥
     */
    @TableField("app_secret")
    private String appSecret;

    /**
     * 创廻时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    /**
     * 替换时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;

    /**
     * 是否删除，0：否，1：是否
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        InterfaceAuthCode other = (InterfaceAuthCode) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getInterfaceInfoId() == null ? other.getInterfaceInfoId() == null : this.getInterfaceInfoId().equals(other.getInterfaceInfoId()))
            && (this.getAppCode() == null ? other.getAppCode() == null : this.getAppCode().equals(other.getAppCode()))
            && (this.getAppKey() == null ? other.getAppKey() == null : this.getAppKey().equals(other.getAppKey()))
            && (this.getAppSecret() == null ? other.getAppSecret() == null : this.getAppSecret().equals(other.getAppSecret()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInterfaceInfoId() == null) ? 0 : getInterfaceInfoId().hashCode());
        result = prime * result + ((getAppCode() == null) ? 0 : getAppCode().hashCode());
        result = prime * result + ((getAppKey() == null) ? 0 : getAppKey().hashCode());
        result = prime * result + ((getAppSecret() == null) ? 0 : getAppSecret().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", interfaceInfoId=").append(interfaceInfoId);
        sb.append(", appCode=").append(appCode);
        sb.append(", appKey=").append(appKey);
        sb.append(", appSecret=").append(appSecret);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}