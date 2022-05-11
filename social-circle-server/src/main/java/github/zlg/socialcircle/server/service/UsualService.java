package github.zlg.socialcircle.server.service;

import github.zlg.socialcircle.module.dto.socialcircle.CircleModuleDto;
import github.zlg.socialcircle.module.dto.user.UserDto;
import github.zlg.socialcircle.module.entity.circlerelease.SocialCircleUser;
import github.zlg.socialcircle.module.entity.content.CircleModule;
import github.zlg.socialcircle.module.entity.userrelease.AppUserSecret;
import github.zlg.socialcircle.module.entity.userrelease.User;
import github.zlg.socialcircle.module.entity.userrelease.WeiXinUser;

import java.io.StringReader;
import java.util.List;

/**
 * @program: social-circle-main
 * @description: 通用业务操作接口
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-12 19:08
 **/
public interface UsualService {

    User selectUserByUserId(String userUUID);

    boolean isOpenIdExistInUserTable(String openid);

    List<CircleModuleDto> getCircleModuleInfo(String circleId);

    SocialCircleUser getCircleOwnerInfo(String circleId);

    /**
     * 判断用户是否收藏了指定圈子,收藏了返回两者映射关系
     */
    boolean isUserInCircle(String socialCircleId, String userUUID);

    /**
     * 获取圈子内包含的用户数量
     */
    int getPeopleNumInCircle(String socialCircleId);

    /**
     * 判断当前用户是否可以修改当前圈子（仅圈主、管理员可以修改圈子）
     */
    boolean isUserManagerSocialCircle(String socialCircleId, String userUUID);

    /**
     * 判断用户是否可以修改当前消息
     */
    boolean isUserManagerNoticeMessage(String noticeMessageId, String userOpenId);

    /**
     * 判断当前消息是否超出撤回时间
     */
    boolean isMessageOutOfTime(String noticeMessageId);

}
