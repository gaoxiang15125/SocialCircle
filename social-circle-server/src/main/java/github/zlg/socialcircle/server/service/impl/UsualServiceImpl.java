package github.zlg.socialcircle.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.zlg.socialcircle.module.define.DatabaseEnum;
import github.zlg.socialcircle.module.dto.socialcircle.CircleModuleDto;
import github.zlg.socialcircle.module.entity.circlerelease.SocialCircleUser;
import github.zlg.socialcircle.module.entity.content.CircleModule;
import github.zlg.socialcircle.module.entity.noticerelease.Comment;
import github.zlg.socialcircle.module.entity.userrelease.User;
import github.zlg.socialcircle.module.entity.userrelease.WeiXinUser;
import github.zlg.socialcircle.module.mapper.message.CommentMapper;
import github.zlg.socialcircle.module.mapper.module.CircleModuleMapper;
import github.zlg.socialcircle.module.mapper.module.ModuleContentMapper;
import github.zlg.socialcircle.module.mapper.socialcircle.SocialCircleUserMapper;
import github.zlg.socialcircle.module.mapper.user.UserMapper;
import github.zlg.socialcircle.module.mapper.user.WeiXinUserMapper;
import github.zlg.socialcircle.module.util.DataChangeUtil;
import github.zlg.socialcircle.server.config.SocialCircleConfig;
import github.zlg.socialcircle.server.service.UsualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: social-circle-main
 * @description: 通用服务实现类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-12 19:24
 **/
@Service
public class UsualServiceImpl implements UsualService {

    UserMapper userMapper;

    WeiXinUserMapper weiXinUserMapper;

    SocialCircleUserMapper socialCircleUserMapper;

    ModuleContentMapper moduleContentMapper;

    CircleModuleMapper circleModuleMapper;

    CommentMapper commentMapper;

    // 应用程序配置相关信息
    SocialCircleConfig socialCircleConfig;

    // 所有权限判断逻辑

    @Autowired
    public UsualServiceImpl(UserMapper userMapper,
                            WeiXinUserMapper weiXinUserMapper,
                            SocialCircleUserMapper socialCircleUserMapper,
                            ModuleContentMapper moduleContentMapper,
                            CircleModuleMapper circleModuleMapper,
                            CommentMapper commentMapper,
                            SocialCircleConfig socialCircleConfig) {

        this.userMapper = userMapper;
        this.weiXinUserMapper = weiXinUserMapper;
        this.socialCircleUserMapper = socialCircleUserMapper;
        this.moduleContentMapper = moduleContentMapper;
        this.circleModuleMapper = circleModuleMapper;
        this.commentMapper = commentMapper;
        this.socialCircleConfig = socialCircleConfig;
    }

    @Override
    public User selectUserByUserId(String userUUID) {
        return userMapper.selectById(userUUID);
    }

    /**
     * 用户信息采用事务的方式提交，一个判断即可确定两张表是否包含我们的信息
     * @param openid
     * @return
     */
    @Override
    public boolean isOpenIdExistInUserTable(String openid) {
        QueryWrapper<WeiXinUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WeiXinUser::getOpenId, openid);
        return weiXinUserMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 根据圈子id 获取圈子包含的板块信息
     * @param circleId
     * @return
     */
    @Override
    public List<CircleModuleDto> getCircleModuleInfo(String circleId) {
        QueryWrapper<CircleModule> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CircleModule::getCircleId, circleId);

        List<CircleModule> modules = circleModuleMapper.selectList(queryWrapper);

        List<CircleModuleDto> moduleDtoList = new ArrayList<>();
        for(CircleModule module: modules) {
            moduleDtoList.add(DataChangeUtil.changeEntityToDto(module));
        }
        return moduleDtoList;
    }

    /**
     * 根据圈子 id 获取圈主用户id
     * @param circleId
     * @return
     */
    @Override
    public SocialCircleUser getCircleOwnerInfo(String circleId) {
        QueryWrapper<SocialCircleUser> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().eq(SocialCircleUser::getSocialCircleId,circleId)
                .eq(SocialCircleUser::getMemberType, DatabaseEnum.MEMBER_TYPE.QLeader.getCode());
        SocialCircleUser circleUser = socialCircleUserMapper.selectOne(queryWrapper);
        return circleUser;
    }

    @Override
    public boolean isUserInCircle(String socialCircleId, String userUUID) {
        QueryWrapper<SocialCircleUser> queryWrapper = getSocialCircleUserByUserAndCircle(socialCircleId, userUUID);
        return socialCircleUserMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public int getPeopleNumInCircle(String socialCircleId) {
        QueryWrapper<SocialCircleUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SocialCircleUser::getSocialCircleId, socialCircleId);
        return socialCircleUserMapper.selectCount(queryWrapper);
    }

    /**
     * 判断用户是否可以修改当前圈子
     * @param socialCircleId
     * @param openId
     * @return
     */
    @Override
    public boolean isUserManagerSocialCircle(String socialCircleId, String openId) {

        QueryWrapper<SocialCircleUser> queryWrapper = getSocialCircleUserByUserAndCircle(socialCircleId, openId);
        SocialCircleUser socialCircleUser = socialCircleUserMapper.selectOne(queryWrapper);
        if(socialCircleUser == null) {
            return false;
        }

        return socialCircleUser.getMemberType() == DatabaseEnum.MEMBER_TYPE.QManager.getCode()
                || socialCircleUser.getMemberType() == DatabaseEnum.MEMBER_TYPE.QLeader.getCode();
    }

    @Override
    public boolean isUserManagerNoticeMessage(String noticeMessageId, String userOpenId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Comment::getId, noticeMessageId)
                .eq(Comment::getUserId, userOpenId);
        Comment comment = commentMapper.selectOne(queryWrapper);
        return comment == null;
    }

    @Override
    public boolean isMessageOutOfTime(String noticeMessageId) {
        // JDK1.8 之后就已经有了更优雅的解决方法，这里使用下
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime.minusSeconds(socialCircleConfig.getNoticeCancelTimeLimit());
//        Date compareTime =
        // todo 后面应该改为 redis 缓存消息，到达时间后写入数据库
        // 暂时返回 false
        return false;
    }

    /**
     * 获取 userId socialCirclrId 限制条件的 圈子 —— 用户 关系表
     * @param socialCircleID 圈子 ID
     * @param userUUID 用户 ID
     * @return
     */
    private QueryWrapper<SocialCircleUser> getSocialCircleUserByUserAndCircle(String socialCircleID, String userUUID) {
        QueryWrapper<SocialCircleUser> socialUser = new QueryWrapper<>();
        socialUser.lambda().eq(SocialCircleUser::getUserId, userUUID)
                .eq(SocialCircleUser::getSocialCircleId,socialCircleID);
        return socialUser;
    }
}
