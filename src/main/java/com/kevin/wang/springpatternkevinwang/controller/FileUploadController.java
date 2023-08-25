package com.kevin.wang.springpatternkevinwang.controller;

import cn.hutool.core.io.FileUtil;
import com.kevin.wang.springpatternkevinwang.common.BaseResponse;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.common.ResultUtils;
import com.kevin.wang.springpatternkevinwang.constant.CommonConstant;
import com.kevin.wang.springpatternkevinwang.constant.FileConstant;
import com.kevin.wang.springpatternkevinwang.exception.BussinessException;
import com.kevin.wang.springpatternkevinwang.manager.CosManager;
import com.kevin.wang.springpatternkevinwang.model.dto.file.UploadFileRequest;
import com.kevin.wang.springpatternkevinwang.model.entity.User;
import com.kevin.wang.springpatternkevinwang.model.enums.FileUploadBizEnum;
import com.kevin.wang.springpatternkevinwang.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author wang
 * @create 2023-2023-21-22:37
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileUploadController {
    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;
    private final String[] allowFileTypes = {".jpg",".png",".gif",".jpeg",".bmp",".mp4",".avi",".flv",".wmv",".mp3",".wav",".wma",".ogg",".doc",".docx",".xls",".xlsx",".ppt",".pptx",".pdf",".txt",".zip",".rar",".7z",".tar",".gz",".gzip",".iso",".img"};
    @PostMapping("/upload")
    public BaseResponse<String> upload(@RequestPart("file") MultipartFile multipartFile,
                               UploadFileRequest uploadFileRequest, HttpServletRequest request){

        if(multipartFile.isEmpty()){
            return new BaseResponse<>(500,"上传的文件不能空",null);
        }
        FileUploadBizEnum fileUploadEnum = FileUploadBizEnum.getByValue(uploadFileRequest.getBiz());
        if (fileUploadEnum == null) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        validFile(multipartFile);
        User loginUser = userService.getLoginUser(request);

        String uuid = RandomStringUtils.randomAlphabetic(8);

        // 获取文件名
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = uuid+"-"+originalFilename;

        String filePath = String.format("/%s/%s/%s",fileUploadEnum.getValue(),loginUser.getId(),fileName);

        File file = null;
        try {
            file = File.createTempFile(filePath,null);
            multipartFile.transferTo(file);
            cosManager.putObject(filePath,file);
            return ResultUtils.success(FileConstant.COS_HOST+fileName);
        } catch (IOException e) {
            log.error("文件上传失败，文件位置：{}",filePath);
            throw new BussinessException(ErrorCode.PARAMS_ERROR.getCode(), "文件上传失败");
        }finally {
            boolean delete = file.delete();
            if(!delete){
                file.delete();
            }
        }
    }

    private void validFile(MultipartFile multipartFile) {
        long fileSize = multipartFile.getSize();
        int maxSize = 1024*1024*10; // 10MB
        if(fileSize>maxSize) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR, "上传的文件过大，不能超过10M");
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        for (String type : allowFileTypes) {
            if(type.equals(suffix))
                return ;
        }
        throw new BussinessException(ErrorCode.PARAMS_ERROR,"文件类型不收入");
    }
}
