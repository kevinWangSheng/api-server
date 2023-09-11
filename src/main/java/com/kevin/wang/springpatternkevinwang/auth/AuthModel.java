package com.kevin.wang.springpatternkevinwang.auth;

import com.dubbo.model.entity.User;
import com.kevin.wang.springpatternkevinwang.common.ErrorCode;
import com.kevin.wang.springpatternkevinwang.exception.BussinessException;
import com.kevin.wang.springpatternkevinwang.exception.ThrowUtils;
import com.kevin.wang.springpatternkevinwang.model.vo.LoginUserVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

/**
 * @author wang
 * @create 2023-2023-07-8:56
 */
@Component
public class AuthModel {
    public boolean userInfo(User loginUserVO, String password){
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(loginUserVO.getUserAccount(),password);
            ThrowUtils.throwIf(token==null, ErrorCode.OPERATION_ERROR);
            subject.login(token);
            return true;
        } catch (UnknownAccountException e) {
            throw new BussinessException(ErrorCode.NOT_FOUND_ERROR,"用户名不存在");
        }catch (IncorrectCredentialsException e) {
            throw new BussinessException(ErrorCode.NOT_FOUND_ERROR,"密码错误");
        }
    }
}
