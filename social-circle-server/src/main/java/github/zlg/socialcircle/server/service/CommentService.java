package github.zlg.socialcircle.server.service;

import github.zlg.socialcircle.module.dto.modulecontent.CommentDto;
import github.zlg.socialcircle.module.entity.noticerelease.Comment;

import java.util.List;

/**
 * @program: social-circle-main
 * @description: 消息交流模块接口定义类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-14 17:33
 **/
public interface CommentService {

    /**
     * 添加消息内容
     * @param commentDto 消息实体相关信息
     * @param fatherId 消息的父级消息，可为空
     * @return
     */
    CommentDto addComment(CommentDto commentDto, String fatherId, String circleId, String moduleId, String userUUID);

    /**
     * 撤回指定消息
     * @param noticeId 消息 id
     * @param userOpenId 用户 id 用来判断用户权限
     * @return
     */
    boolean cancelMessage(String noticeId, String userOpenId);

    /**
     * 根据 userId 获取相关消息，全部分页处理
     * @param userOpenId 加密的用户唯一识别 id
     * @return
     */
    List<CommentDto> getCommentByUserId(String userOpenId, int pageNum, int contentNum);

    /**
     * 根据 moduleContentId 获取相关消息
     * @param moduleContentId 加密的板块内容唯一识别 id
     * @return
     */
    List<CommentDto> getCommentByModuleContentId(String moduleContentId);

    /**
     * 根据 圈子Id 获取相关消息
     * @param circleId 圈子id
     * @return
     */
    List<CommentDto> getCommentByCircleId(String circleId);

    /**
     * 根据 userUUID 获取用户应该接收的消息数量
     * @param userUUID
     * @return
     */
    int getCommentNumByUserId(String userUUID);

    /**
     * 根据 moduleContentID 获取帖子下评论数量
     * @param moduleContentId
     * @return
     */
    int getCommentNumByModuleContentId(String moduleContentId);

    /**
     * 根据 circleId 获取圈子内评论数量
     * @param circleId
     * @return
     */
    int getCommentNumByCircleId(String circleId);
}
