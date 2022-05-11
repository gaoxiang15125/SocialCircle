package github.zlg.socialcircle.api.weixinservice;

import github.zlg.socialcircle.api.vo.WeiXinUserVo;

/**
 * @program: social-circle-main
 * @description: 用户登录相关操作接口类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-18 20:36
 **/
public interface UserLogin {

    /**
     * 验证用户登录权限并返回用户信息
     * @param token 用户验证码
     * @return
     */
    String login(String token) throws Exception;

}
