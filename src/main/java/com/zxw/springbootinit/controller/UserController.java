package com.zxw.springbootinit.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.zxw.openapicommon.model.entity.User;
import com.zxw.springbootinit.annotation.AuthCheck;
import com.zxw.springbootinit.common.BaseResponse;
import com.zxw.springbootinit.common.DeleteRequest;
import com.zxw.springbootinit.common.ErrorCode;
import com.zxw.springbootinit.common.ResultUtils;
import com.zxw.springbootinit.config.EmailConfig;
import com.zxw.springbootinit.config.WxOpenConfig;
import com.zxw.springbootinit.constant.UserConstant;
import com.zxw.springbootinit.exception.BusinessException;
import com.zxw.springbootinit.exception.ThrowUtils;
import com.zxw.springbootinit.model.dto.user.*;
import com.zxw.springbootinit.model.vo.LoginUserVO;
import com.zxw.springbootinit.model.vo.UserUpdateSignVo;
import com.zxw.springbootinit.model.vo.UserVO;
import com.zxw.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.zxw.springbootinit.constant.EmailConstant.*;
import static com.zxw.springbootinit.service.impl.UserServiceImpl.SALT;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private WxOpenConfig wxOpenConfig;
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private EmailConfig emailConfig;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    // region 登录相关

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户电子邮件登录
     *
     * @param userEmailLoginRequest 用户登录请求
     * @param request               请求
     * @return {@link BaseResponse}<{@link User}>
     */
    @PostMapping("/email/login")
    public BaseResponse<UserVO> userEmailLogin(@RequestBody UserEmailLoginRequest userEmailLoginRequest, HttpServletRequest request) {
        if (userEmailLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO user = userService.userEmailLogin(userEmailLoginRequest, request);
        redisTemplate.delete(CAPTCHA_CACHE_KEY + userEmailLoginRequest.getEmailAccount());
        return ResultUtils.success(user);
    }

    /**
     * 用户绑定电子邮件
     *
     * @param request              请求
     * @param userBindEmailRequest 用户绑定电子邮件请求
     * @return {@link BaseResponse}<{@link UserVO}>
     */
    @PostMapping("/bind/login")
    public BaseResponse<UserVO> userBindEmail(@RequestBody UserBindEmailRequest userBindEmailRequest, HttpServletRequest request) {
        if (userBindEmailRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO user = userService.userBindEmail(userBindEmailRequest, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户取消绑定电子邮件
     *
     * @param request                请求
     * @param userUnBindEmailRequest 用户取消绑定电子邮件请求
     * @return {@link BaseResponse}<{@link UserVO}>
     */
    @PostMapping("/unbindEmail")
    public BaseResponse<UserVO> userUnBindEmail(@RequestBody UserUnBindEmailRequest userUnBindEmailRequest, HttpServletRequest request) {
        if (userUnBindEmailRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO user = userService.userUnBindEmail(userUnBindEmailRequest, request);
        redisTemplate.delete(CAPTCHA_CACHE_KEY + userUnBindEmailRequest.getEmailAccount());
        return ResultUtils.success(user);
    }

    /**
     * 用户电子邮件注册
     *
     * @param userEmailRegisterRequest 用户电子邮件注册请求
     * @return {@link BaseResponse}<{@link UserVO}>
     */
    @PostMapping("/email/register")
    public BaseResponse<Long> userEmailRegister(@RequestBody UserEmailRegisterRequest userEmailRegisterRequest) {
        if (userEmailRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userEmailRegister(userEmailRegisterRequest);
        redisTemplate.delete(CAPTCHA_CACHE_KEY + userEmailRegisterRequest.getEmailAccount());
        return ResultUtils.success(result);
    }

    /**
     * 获取验证码
     *
     * @param emailAccount 电子邮件帐户
     * @return {@link BaseResponse}<{@link String}>
     */
    @GetMapping("/getCaptcha")
    public BaseResponse<Boolean> getCaptcha(String emailAccount) {
        if (StringUtils.isBlank(emailAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailPattern, emailAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不合法的邮箱地址！");
        }
        String captcha = RandomUtil.randomNumbers(6);
        try {
            sendEmail(emailAccount, captcha);
            redisTemplate.opsForValue().set(CAPTCHA_CACHE_KEY + emailAccount, captcha, 5, TimeUnit.MINUTES);
            return ResultUtils.success(true);
        } catch (Exception e) {
            log.error("【发送验证码失败】" + e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "验证码获取失败");
        }
    }

    /**
     * 发送邮件
     * @param emailAccount
     * @param captcha
     * @throws MessagingException
     */
    private void sendEmail(String emailAccount, String captcha) throws javax.mail.MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        // 邮箱发送内容组成
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setSubject(EMAIL_SUBJECT);
        helper.setText(buildEmailContent(EMAIL_HTML_CONTENT_PATH, captcha), true);
        helper.setTo(emailAccount);
        helper.setFrom(EMAIL_TITLE + '<' + emailConfig.getEmailFrom() + '>');
        mailSender.send(message);
    }
    /**
     * 生成电子邮件内容
     *
     * @param captcha       验证码
     * @param emailHtmlPath 电子邮件html路径
     * @return {@link String}
     */
    public static String buildEmailContent(String emailHtmlPath, String captcha) {
        // 加载邮件html模板
        ClassPathResource resource = new ClassPathResource(emailHtmlPath);
        InputStream inputStream = null;
        BufferedReader fileReader = null;
        StringBuilder buffer = new StringBuilder();
        String line;
        try {
            inputStream = resource.getInputStream();
            fileReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            log.info("发送邮件读取模板失败{}", e.getMessage());
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 替换html模板中的参数
        return MessageFormat.format(buffer.toString(), captcha, EMAIL_TITLE, EMAIL_TITLE_ENGLISH, PLATFORM_RESPONSIBLE_PERSON, PLATFORM_ADDRESS);
    }

    /**
     * 用户登录（微信开放平台）
     */
    @GetMapping("/login/wx_open")
    public BaseResponse<LoginUserVO> userLoginByWxOpen(HttpServletRequest request, HttpServletResponse response,
                                                       @RequestParam("code") String code) {
        WxOAuth2AccessToken accessToken;
        try {
            WxMpService wxService = wxOpenConfig.getWxMpService();
            accessToken = wxService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo userInfo = wxService.getOAuth2Service().getUserInfo(accessToken, code);
            String unionId = userInfo.getUnionId();
            String mpOpenId = userInfo.getOpenid();
            if (StringUtils.isAnyBlank(unionId, mpOpenId)) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败，系统错误");
            }
            return ResultUtils.success(userService.userLoginByMpOpen(userInfo, request));
        } catch (Exception e) {
            log.error("userLoginByWxOpen error", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败，系统错误");
        }
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    // endregion

    // region 增删改查

    /**
     * 创建用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        String defaultPassword = "12345678";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
                                            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request) {
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                   HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取用户封装列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                       HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    // endregion

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
                                              HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 用户可以更换公钥私钥
     *
     * @param
     * @param request
     * @return
     */
    @PostMapping("/update/sign")
    public BaseResponse<UserUpdateSignVo> updateUserSign(HttpServletRequest request) {
        if (request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserUpdateSignVo userUpdateSignVo = userService.updateUserSign(request);
        return ResultUtils.success(userUpdateSignVo);
    }
}
