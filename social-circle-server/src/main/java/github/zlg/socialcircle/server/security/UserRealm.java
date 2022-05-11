package github.zlg.socialcircle.server.security;

import github.zlg.socialcircle.api.define.WeiXinEnum;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @program: social-circle-main
 * @description: 用户鉴权实现类，包含 认证和授权 两个功能
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-23 15:02
 **/
public class UserRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        /**
         * 执行具体的授权逻辑
         */
        System.out.println("通过判断也好，什么都好的授权逻辑");
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        /**
         * 执行身份认证逻辑
         * 通过微信平台获取用户信息，与数据库做对比，最终取出我们的用户
         */
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(usernamePasswordToken.getUsername(), WeiXinEnum.DEFAULT_PASSWORD, getName());
        return authenticationInfo;
    }
}
