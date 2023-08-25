package com.kevin.wang.springpatternkevinwang.model.dto.file;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wang
 * @create 2023-2023-21-9:40
 */
@Data
public class UploadFileRequest implements Serializable {

    private String biz;

    private static final long serialVersionUID = 1L;

}
