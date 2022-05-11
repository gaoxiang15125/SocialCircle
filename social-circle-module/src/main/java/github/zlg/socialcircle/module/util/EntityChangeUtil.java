package github.zlg.socialcircle.module.util;

import github.zlg.socialcircle.module.define.DatabaseEnum;
import github.zlg.socialcircle.module.entity.userrelease.User;
import github.zlg.socialcircle.module.entity.userrelease.WeiXinUser;

/**
 * @program: social-circle-main
 * @description: 实体类之间相互转换
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-01 15:49
 **/
public class EntityChangeUtil {

    /**
     * 将微信用户信息转化为app内用户信息
     * @param weiXinUser
     */
    public static User changeWeiXinUserToUser(WeiXinUser weiXinUser) {
        User user = new User();
        user.setUserName(weiXinUser.getUserName());
        user.setUserLogo(weiXinUser.getUserLogo());
        user.setSex(weiXinUser.getSex());
        return user;
    }
}
