package com.kevin.wang.springpatternkevinwang.model.enums;

import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wang
 * @create 2023-2023-20-15:49
 */
public enum UserRoleEnums {

    USER("用户", "user"),
    ADMIN("管理员", "admin"),
    BAN("被封号", "ban");
    private final String text;

    private final String value;

    UserRoleEnums(String text,String value){
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public static List<String> getValues(){
        return Arrays.stream(UserRoleEnums.values()).map(userRoleEnums -> userRoleEnums.getValue()).collect(Collectors.toList());
    }

    public static UserRoleEnums getUser(String value){
        if(StringUtils.isBlank(value)){
           return null;
        }
        for(UserRoleEnums enums :UserRoleEnums.values()){
            if(enums.value.equals(value)){
                return enums;
            }
        }
        return null;
    }



}
