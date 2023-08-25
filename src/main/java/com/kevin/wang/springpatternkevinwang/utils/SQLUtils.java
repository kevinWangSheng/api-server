package com.kevin.wang.springpatternkevinwang.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wang
 * @create 2023-2023-21-13:57
 */
public class SQLUtils {
    public static boolean vaildSortField(String sortField) {
        if(StringUtils.isBlank(sortField)) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "(", ")","=", ">", "<", ">=", "<=", "!=", "like", "in", "between", "is null", "is not null");
    }
}
