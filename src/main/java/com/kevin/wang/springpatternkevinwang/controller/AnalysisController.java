package com.kevin.wang.springpatternkevinwang.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kevin.wang.springpatternkevinwang.common.BaseResponse;
import com.kevin.wang.springpatternkevinwang.common.ResultUtils;
import com.kevin.wang.springpatternkevinwang.service.InterfaceInfoService;
import com.kevin.wang.springpatternkevinwang.service.UserInterfaceInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wang
 * @create 2023-2023-10-20:07
 */
@RequestMapping("/analysis")
@RestController
public class AnalysisController {

    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @GetMapping("/top/interface/invoke")
    public BaseResponse<?> getTopInterfaceInvoke(@RequestParam(value = "pageSize",required = false,defaultValue = "3")Integer pageSize ) {
        Page<String> topInterfaceInvoke = userInterfaceInfoService.getTopInterfaceInvoke(pageSize);

        return ResultUtils.success(topInterfaceInvoke);
    }

}
