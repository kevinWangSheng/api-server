package com.kevin.wang.springpatternkevinwang.model.enums;

import lombok.Data;

/**
 * @author wang
 * @create 2023-2023-30-23:48
 */
public enum InterfaceInfoStatusEnum {
    Online("online",1),
    Down("down",0);
    private String status;

    private Integer value;

     InterfaceInfoStatusEnum(String status,Integer value) {
        this.status = status;
        this.value = value;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
