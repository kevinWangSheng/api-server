package com.kevin.wang.springpatternkevinwang.common;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author wang
 * @create 2023-2023-20-20:35
 */
@Data
public class DeleteRequest  implements Serializable {

    private Long id;

    private static final long serialVersionId =1l;
}
