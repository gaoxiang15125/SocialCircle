package github.zlg.socialcircle.module.util;

import github.zlg.socialcircle.module.define.DatabaseEnum;
import github.zlg.socialcircle.module.dto.modulecontent.ModuleContentDto;
import github.zlg.socialcircle.module.dto.modulecontent.CommentDto;
import github.zlg.socialcircle.module.dto.socialcircle.CircleModuleDto;
import github.zlg.socialcircle.module.dto.socialcircle.SocialCircleDto;
import github.zlg.socialcircle.module.dto.user.UserDto;
import github.zlg.socialcircle.module.dto.user.WeiXinUserDto;
import github.zlg.socialcircle.module.entity.circlerelease.SocialCircle;
import github.zlg.socialcircle.module.entity.content.CircleModule;
import github.zlg.socialcircle.module.entity.content.ModuleContent;
import github.zlg.socialcircle.module.entity.noticerelease.Comment;
import github.zlg.socialcircle.module.entity.userrelease.User;
import github.zlg.socialcircle.module.entity.userrelease.WeiXinUser;

import java.time.LocalDateTime;

/**
 * @program: social-circle-main
 * @description: 负责将 dto 转为 entity，entity 转为 dto
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-30 13:40
 **/
public class DataChangeUtil {

    /**
     * 将用户信息dto转化为数据库entity
     */
    public static User changeDtoToEntity(UserDto userDto){
        User user = new User();

        // id 存在赋值，否则设置为 null
        user.setId(SecretUtil.decryptId(userDto.getId()));

        user.setDescription(userDto.getDescription());
        user.setUserName(userDto.getUserName());
        user.setUserLogo(userDto.getHeadImg());
        user.setSex(DatabaseEnum.SEX_TYPE.getSexByStr(userDto.getSex()).getCode());
        return user;
    }

    /**
     * 将用户entity 转化为 用户 dto
     * @param user
     * @return
     */
    public static UserDto changeEntityToDto(User user){
        UserDto userDto = new UserDto();

        // 使用数据库 id 进行赋值
        userDto.setId(SecretUtil.encryptId(user.getId()));

        userDto.setDescription(user.getDescription());
        userDto.setHeadImg(user.getUserLogo());
        userDto.setSex(DatabaseEnum.SEX_TYPE.getSexByCode(user.getSex()).getSexStr());
        userDto.setUserName(user.getUserName());
        return userDto;
    }

    /**
     * 将圈子entity 转化为 圈子简略信息，最终将以 list 的形式返回
     * @param socialCircle
     * @return
     */
    public static SocialCircleDto changeEntityToDto(SocialCircle socialCircle){
        SocialCircleDto socialCircleDto = new SocialCircleDto();

        // 设置加密 key
        socialCircleDto.setId(SecretUtil.encryptId(socialCircle.getId()));

        socialCircleDto.setCircleDescription(socialCircle.getCircleDescription());
        socialCircleDto.setCircleLogo(socialCircle.getCircleLogo());
        socialCircleDto.setCircleName(socialCircle.getCircleName());
        socialCircleDto.setLatitude(socialCircle.getLatitude());
        socialCircleDto.setLongitude(socialCircle.getLongitude());
        socialCircleDto.setCreatTime(socialCircle.getCreatTime());
        return socialCircleDto;
    }

    /**
     * 将圈子dto 转化为数据库对象，仅维护简要信息
     * @param socialCircleDto
     * @return
     */
    public static SocialCircle changeDtoToEntity(SocialCircleDto socialCircleDto){

        SocialCircle socialCircle = new SocialCircle();

        socialCircle.setId(SecretUtil.decryptId(socialCircleDto.getId()));

        socialCircle.setLongitude(socialCircleDto.getLongitude());
        socialCircle.setLatitude(socialCircleDto.getLatitude());
        socialCircle.setCircleDescription(socialCircleDto.getCircleDescription());
        socialCircle.setCircleLogo(socialCircleDto.getCircleLogo());
        socialCircle.setCircleName(socialCircleDto.getCircleName());

        if(socialCircleDto.getCreatTime() == null) {
            socialCircle.setCreatTime(LocalDateTime.now());
        } else {
            socialCircle.setCreatTime(socialCircleDto.getCreatTime());
        }

        return socialCircle;
    }

    /**
     * 将 数据库消息 转化为 消息 Dto
     * @param comment 数据库消息对象
     * @return 消息 Dto 不包含发送者信息
     */
    public static CommentDto changeEntityToDto(Comment comment){

        CommentDto commentDto = new CommentDto();

        // 设置消息唯一 id
        commentDto.setId(SecretUtil.encryptId(comment.getId()));

        commentDto.setUserId(SecretUtil.encryptId(comment.getUserId()));
        commentDto.setCircleId(SecretUtil.encryptId(comment.getCircleId()));
        commentDto.setModuleId(SecretUtil.encryptId(comment.getModuleId()));

        commentDto.setCreatTime(comment.getCreatTime());
        commentDto.setMessage(comment.getTalkInfo());
        commentDto.setMessageStatus(comment.getMessageStatus());
        return commentDto;
    }

    /**
     * 将消息Dto 转化为 数据库消息对象,与业务相关字段需自行处理
     * @param commentDto
     * @return
     */
    public static Comment changeDtoToEntity(CommentDto commentDto){

        Comment comment = new Comment();

        // 获取消息更新时 id
        comment.setId(SecretUtil.decryptId(commentDto.getId()));

        comment.setCircleId(SecretUtil.decryptId(commentDto.getCircleId()));

        comment.setUserId(SecretUtil.decryptId(commentDto.getUserId()));

        comment.setModuleId(SecretUtil.decryptId(commentDto.getModuleId()));

        if(commentDto.getCreatTime() == null) {
            commentDto.setCreatTime(LocalDateTime.now());
        } else {
            comment.setCreatTime(commentDto.getCreatTime());
        }

        comment.setTalkInfo(commentDto.getMessage());
        comment.setMessageStatus(commentDto.getMessageStatus());
        return comment;
    }

    /**
     * 将数据库板块子项内容 转化为 板块子项Dto
     * @param moduleContent
     * @return
     */
    public static ModuleContentDto changeEntityToDto(ModuleContent moduleContent){

        ModuleContentDto moduleContentDto = new ModuleContentDto();

        moduleContentDto.setId(SecretUtil.encryptId(moduleContent.getId()));

        // 索引相关字段赋值
        moduleContentDto.setCreatorOpenId(SecretUtil.encryptId(moduleContent.getCreatorId()));

        moduleContentDto.setCollectionNum(moduleContent.getCollectionNum());
        moduleContentDto.setDescription(moduleContent.getContentInfo());
        moduleContentDto.setThumbUpNum(moduleContent.getThumbUpNum());
        moduleContentDto.setCreatTime(moduleContent.getCreatTime());
        return moduleContentDto;
    }

    /**
     * 将 内容Dto 转化为 数据库板块对象
     * Dto 内仅包含创建者信息，并不能取到别的信息
     * @param moduleContentDto
     * @return
     */
    public static ModuleContent changeDtoToEntity(ModuleContentDto moduleContentDto){

        ModuleContent moduleContent = new ModuleContent();

        moduleContent.setId(SecretUtil.decryptId(moduleContentDto.getId()));

        moduleContent.setCreatorId(SecretUtil.decryptId(moduleContentDto.getCreatorOpenId()));

        moduleContent.setCollectionNum(moduleContentDto.getCollectionNum());
        moduleContent.setContentInfo(moduleContentDto.getDescription());
        moduleContent.setThumbUpNum(moduleContentDto.getThumbUpNum());

        if(moduleContentDto.getCreatTime() == null) {
            moduleContent.setCreatTime(LocalDateTime.now());
        }
        moduleContent.setCreatTime(moduleContentDto.getCreatTime());
        moduleContent.setCreatTime(LocalDateTime.now());
        return moduleContent;
    }

    /**
     * 将数据库微信对象 转化为 微信Dto
     * @param weiXinUser
     * @return
     */
    public static WeiXinUserDto changeEntityToDto(WeiXinUser weiXinUser){

        WeiXinUserDto weiXinUserDto = new WeiXinUserDto();
        weiXinUserDto.setNickName(weiXinUser.getUserName());
        weiXinUserDto.setGender(DatabaseEnum.SEX_TYPE.getSexByCode(weiXinUser.getSex()).getCode());
        weiXinUserDto.setAvatarUrl(weiXinUser.getUserLogo());
        weiXinUserDto.setCountry(weiXinUser.getCountry());
        weiXinUserDto.setProvince(weiXinUser.getProvince());
        weiXinUserDto.setCity(weiXinUser.getCity());
        weiXinUserDto.setLanguage(weiXinUser.getLanguage());
        return weiXinUserDto;
    }

    /**
     * 将微信Dto 转化为 数据库微信对象
     * @param weiXinUserDto
     * @return
     */
    public static WeiXinUser changeDtoToEntity(WeiXinUserDto weiXinUserDto){

        WeiXinUser weiXinUser = new WeiXinUser();
        weiXinUser.setSex(DatabaseEnum.SEX_TYPE.getSexByCode(weiXinUserDto.getGender()).getCode());
        weiXinUser.setUserLogo(weiXinUserDto.getAvatarUrl());
        weiXinUser.setUserName(weiXinUserDto.getNickName());
        weiXinUser.setCountry(weiXinUserDto.getCountry());
        weiXinUser.setProvince(weiXinUserDto.getProvince());
        weiXinUser.setCity(weiXinUserDto.getCity());
        weiXinUser.setLanguage(weiXinUserDto.getLanguage());

        weiXinUser.setCreatTime(LocalDateTime.now());
        return weiXinUser;
    }

    /**
     * 将圈子模块对象  转化为 圈子模块Dto
     * @param circleModule
     * @return
     */
    public static CircleModuleDto changeEntityToDto(CircleModule circleModule){

        CircleModuleDto circleModuleDto = new CircleModuleDto();

        circleModuleDto.setId(SecretUtil.encryptId(circleModule.getId()));

        circleModuleDto.setCircleId(SecretUtil.encryptId(circleModule.getCircleId()));

        circleModuleDto.setModuleDescription(circleModule.getModuleDescription());
        circleModuleDto.setModuleName(circleModule.getModuleName());
        circleModuleDto.setCreatTime(circleModule.getCreatTime());
        return circleModuleDto;
    }

    /**
     * 将圈子模块Dto 转化为 圈子模块对象,创建者信息由上层业务补充
     * @param circleModuleDto
     * @return
     */
    public static CircleModule changeDtoToEntity(CircleModuleDto circleModuleDto){

        CircleModule circleModule = new CircleModule();

        // 更新操作需要 圈子id，新增操作不需要 圈子id
        circleModule.setId(SecretUtil.decryptId(circleModuleDto.getId()));

        // 板块相关表索引字段
        circleModule.setCircleId(SecretUtil.decryptId(circleModuleDto.getCircleId()));

        circleModule.setModuleName(circleModuleDto.getModuleName());
        circleModule.setModuleDescription(circleModuleDto.getModuleDescription());

        if(circleModuleDto.getCreatTime() == null) {
            circleModule.setCreatTime(LocalDateTime.now());
        } else {
            circleModule.setCreatTime(circleModuleDto.getCreatTime());
        }

        return circleModule;
    }

}
