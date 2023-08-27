package com.kevin.wang.springpatternkevinwang.model.dto.interfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author wang
 * @create 2023-2023-21-9:43
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {
    private String name;

    private String description;

    private String url;

    private String method;

    private String requestHeader;

    private String responseHeader;

}
