package com.openapi.gateway.component.service.impl;

import com.openapi.gateway.component.service.InterfaceService;
import org.springframework.stereotype.Service;

/**
 * @author wang
 * @create 2023-2023-03-12:47
 */
@Service
public class InterfaceServiceImpl implements InterfaceService {
    @Override
    public String service() {
        return "wangwu";
    }
}
