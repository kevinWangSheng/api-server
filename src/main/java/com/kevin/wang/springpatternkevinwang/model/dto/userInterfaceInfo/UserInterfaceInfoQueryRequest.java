package com.kevin.wang.springpatternkevinwang.model.dto.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.kevin.wang.springpatternkevinwang.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wang
 * @create 2023-2023-21-11:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {
    private Long id;


    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;


    private static final long serialVersionUID = 1L;


}
