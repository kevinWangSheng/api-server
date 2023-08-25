package com.kevin.wang.springpatternkevinwang.model.enums;

import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wang
 * @create 2023-2023-21-0:04
 */

public enum FileUploadBizEnum {
    USER_AVATR("用户头像","user_avatar");
    private String text;

    private String value;

    FileUploadBizEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static List<String> getValues(){
        return Arrays.stream(values()).map(item->item.getValue()).collect(Collectors.toList());
    }

    public static FileUploadBizEnum getByValue(String value){
        if(ObjectUtil.isEmpty(value)){
            return null;
        }
        for(FileUploadBizEnum  eum: FileUploadBizEnum.values()){
            if(eum.getValue().equals(value)){
                return eum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }


}
