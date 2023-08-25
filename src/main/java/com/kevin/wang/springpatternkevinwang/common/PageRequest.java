package com.kevin.wang.springpatternkevinwang.common;

import com.kevin.wang.springpatternkevinwang.constant.CommonConstant;
import lombok.Data;

/**
 * @author wang
 * @create 2023-2023-20-20:36
 */
@Data
public class PageRequest {
    private long current = 1l;

    private long pageSize = 10;

    private String sortField;

    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
