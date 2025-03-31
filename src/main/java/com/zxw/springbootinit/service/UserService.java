package com.zxw.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zxw.openapicommon.model.entity.User;
import com.zxw.springbootinit.model.dto.user.*;
import com.zxw.springbootinit.model.vo.LoginUserVO;
import com.zxw.springbootinit.model.vo.UserUpdateSignVo;
import com.zxw.springbootinit.model.vo.UserVO;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户登录（微信开放平台）
     *
     * @param wxOAuth2UserInfo 从微信获取的用户信息
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLoginByMpOpen(WxOAuth2UserInfo wxOAuth2UserInfo, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 更新用户的（公钥私钥）签名
     * @param request
     * @return
     */
    UserUpdateSignVo updateUserSign(HttpServletRequest request);
    /**
     * 用户电子邮件注册
     *
     * @param userEmailRegisterRequest 用户电子邮件注册请求
     * @return long
     */
    long userEmailRegister(UserEmailRegisterRequest userEmailRegisterRequest);

    /**
     * 邮箱登录
     * @param userEmailLoginRequest
     * @param request
     * @return
     */
    UserVO userEmailLogin(UserEmailLoginRequest userEmailLoginRequest, HttpServletRequest request);

    /**
     * 用户绑定电子邮件
     *
     * @param userBindEmailRequest 用户电子邮件登录请求
     * @param request               要求
     * @return {@link UserVO}
     */
    UserVO userBindEmail(UserBindEmailRequest userBindEmailRequest, HttpServletRequest request);
    /**
     * 用户取消绑定电子邮件
     *
     * @param request                要求
     * @param userUnBindEmailRequest 用户取消绑定电子邮件请求
     * @return {@link UserVO}
     */
    UserVO userUnBindEmail(UserUnBindEmailRequest userUnBindEmailRequest, HttpServletRequest request);
}
