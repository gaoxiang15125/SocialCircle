package github.zlg.socialcircle.server.service;

import github.zlg.socialcircle.module.define.DatabaseEnum;
import github.zlg.socialcircle.module.define.selfexception.MyException;
import github.zlg.socialcircle.module.dto.modulecontent.ModuleContentDto;
import github.zlg.socialcircle.module.entity.content.ModuleContent;

import java.util.List;

/**
 * @program: social-circle-main
 * @description: 板块内容服务接口定义类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-14 17:33
 **/
// TODO 板块与回复放置到一个 controller 评论信息不会脱离板块单独存在
public interface ModuleContentService {

    /**
     * 简单的增删改查，只有作者有权利对内容进行操作
     * 查询逻辑相对复杂，需要加载评论、点赞数、收藏数等信息
     */

    /**
     * 分页获取 板块内容
     * @param moduleId 模块Id
     * @param pageNum 页码
     * @param contentNum 每页信息数量
     * @return 当页可展示的板块信息
     */
    List<ModuleContentDto> queryModuleContentByPageWithModuleId(String moduleId, int pageNum, int contentNum, DatabaseEnum.ORDER_TYPE orderType);

    /**
     * 根据 板块Id 获取包含的 帖子/内容 总数
     * @return
     */
    int getModuleContentNumByModuleId(String circleModuleId);

    /**
     * 根据 圈子Id 获取圈子包含的 帖子/内容 总数
     * @return
     */
    int getModuleContentNumByCircleId(String circleId);

    /**
     * 根据用户ID 圈子ID 模块ID 发布帖子
     * @param moduleContentDto
     * @param circleId
     * @param userUUID
     * @param moduleId
     * @return
     */
    ModuleContentDto addModuleContent(ModuleContentDto moduleContentDto, String circleId, String userUUID, String moduleId);

    /**
     * 根据 moduleContentId 修改帖子信息，不修改帖子所属圈子、板块等
     * @param moduleContentDto
     * @return
     */
    boolean updateModuleContent(ModuleContentDto moduleContentDto);

    /**
     * 根据 moduleContentId 删除指定帖子
     * @param moduleContentId 帖子唯一识别ID
     * @return
     */
    boolean deleteModuleContent(String moduleContentId);

    /**
     * 对帖子内容进行点赞
     * @param moduleContentId 帖子编号
     * @param userUUID 用户编号
     * @return 点赞后帖子信息
     */
    ModuleContentDto thumbUpModuleContent(String moduleContentId, String userUUID) throws MyException;

    /**
     * 对帖子进行收藏操作
     * @param moduleContentId 帖子 Id
     * @param userUUID 用户编号
     * @return 收藏后的帖子内容
     */
    ModuleContentDto collectionModuleContent(String moduleContentId, String userUUID) throws MyException;

    boolean isUserManagerModuleContent(String moduleContentId, String userUUID);
}
