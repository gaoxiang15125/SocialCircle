package github.zlg.socialcircle.server.service;

import github.zlg.socialcircle.api.vo.WeiXinUserVo;
import github.zlg.socialcircle.module.define.selfexception.DataBaseReadException;
import github.zlg.socialcircle.module.define.selfexception.MyException;
import github.zlg.socialcircle.module.dto.user.UserDto;
import github.zlg.socialcircle.module.dto.user.WeiXinUserDto;

/**
 * @program: social-circle-main
 * @description: 用户操作相关接口
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-30 16:45
 **/
public interface UserService {

    /**
     * 微信用户登录验证结果
     * @param weiXinUserVo 用户相关验证参数
     * @return 用户客户号
     * @throws MyException 客户号构造相关异常
     */
    String signInApp(WeiXinUserVo weiXinUserVo) throws MyException;

    /**
     * 判断是否存在用户信息，有则返回，无则返回 null
     * @param UUID
     * @return
     */
    UserDto isUserInfoExist(String UUID) throws MyException;

    /**
     * 将前端获取的微信用户信息存储，对新用户构造我方用户信息
     * @param weiXinUserDto
     * @return 我方用户信息 Dto
     */
    UserDto pullWeiXinUserInfo(WeiXinUserDto weiXinUserDto, String userUUID);

    /**
     * 获取用户信息
     * @param openid
     * @return
     */
    UserDto getUserInfo(String openid);

    /**
     * 更新用户信息
     * @param userDto
     * @param openid
     * @return
     */
    UserDto updateUserInfo(UserDto userDto, String openid);

}
