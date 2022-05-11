package github.zlg.socialcircle.server.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.zlg.socialcircle.module.dto.modulecontent.CommentDto;
import github.zlg.socialcircle.module.entity.noticerelease.Comment;
import github.zlg.socialcircle.module.mapper.message.CommentMapper;
import github.zlg.socialcircle.module.util.DataChangeUtil;
import github.zlg.socialcircle.server.service.CommentService;
import github.zlg.socialcircle.server.service.UsualService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: social-circle-main
 * @description: 消息模块相关业务实现类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-14 18:02
 **/
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    CommentMapper commentMapper;

    UsualService usualService;

    @Autowired
    public CommentServiceImpl(UsualService usualService,
                              CommentMapper commentMapper) {
        this.usualService = usualService;
        this.commentMapper = commentMapper;
    }


    @Override
    public CommentDto addComment(CommentDto commentDto, String fatherId, String circleId, String moduleId, String userUUID) {
        Comment comment = DataChangeUtil.changeDtoToEntity(commentDto);

        comment.setFatherMessageId(fatherId);
        comment.setCircleId(circleId);
        comment.setModuleId(moduleId);
        comment.setUserId(userUUID);

        // 返回新增评论，支持之后对该评论的后续操作
        commentMapper.insert(comment);
        return DataChangeUtil.changeEntityToDto(comment);
    }

    @Override
    public boolean cancelMessage(String noticeId, String userOpenId) {
        // 超时操作交给 Controller 解决
        // 权限判断也交给 上层

        return commentMapper.deleteById(noticeId) > 0;
    }

    /**
     * 用户消息可能很多，该方法将来到底怎么用，还要从长计议
     * @param userOpenId
     * @param pageNum
     * @param contentNum
     * @return
     */
    @Override
    public List<CommentDto> getCommentByUserId(String userOpenId, int pageNum, int contentNum) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Comment::getUserId, userOpenId);

        List<Comment> commentList = commentMapper.selectList(queryWrapper);

        // 对消息进行组装，这里因为是用户自己的消息，不再维护 父子结构
        List<CommentDto> commentDtoList = new ArrayList<>();
        for(Comment comment: commentList) {
            commentDtoList.add(DataChangeUtil.changeEntityToDto(comment));
        }
        return commentDtoList;
    }

    /**
     * 根据板块内容获取对话，已经组装为树状结构
     * @param moduleContentId 加密的板块内容唯一识别 id
     * @return
     */
    @Override
    public List<CommentDto> getCommentByModuleContentId(String moduleContentId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Comment::getModuleId, moduleContentId)
                            .orderByAsc(Comment::getCreatTime);
        List<Comment> comments = commentMapper.selectList(queryWrapper);

        return setCommentInTreeNode(comments);
    }

    @Override
    public List<CommentDto> getCommentByCircleId(String circleId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Comment::getCircleId, circleId)
                .orderByAsc(Comment::getCreatTime);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        return setCommentInTreeNode(comments);
    }

    @Override
    public int getCommentNumByUserId(String userUUID) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Comment::getUserId, userUUID);
        return commentMapper.selectCount(queryWrapper);
    }

    @Override
    public int getCommentNumByModuleContentId(String moduleContentId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Comment::getModuleId, moduleContentId);
        return commentMapper.selectCount(queryWrapper);
    }

    @Override
    public int getCommentNumByCircleId(String circleId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Comment::getCircleId, circleId);
        return commentMapper.selectCount(queryWrapper);
    }

    /**
     * 将评论信息解析为树结构并返回
     * @param comments 从数据库读出的评论信息
     * @return
     */
    private List<CommentDto> setCommentInTreeNode(List<Comment> comments) {
        // 使用 hash + List 组装对话树状结构，very easy
        // 用来快速组装父子评论，支持多层嵌套
        Map<String, CommentDto> tagMap = new HashMap<>();
        List<CommentDto> resultList = new ArrayList<>();

        for(Comment comment:comments) {
            // 遍历列表，将内容放入 map，为避免出现不符合顺序的数据，两遍遍历组装树结构
            CommentDto commentDto = DataChangeUtil.changeEntityToDto(comment);
            tagMap.put(comment.getId(), commentDto);
        }

        // 第二遍遍历，遍历 哈希结构，将所有 comment 树结构组装完成
        for(Comment comment: comments) {
            // 获取自身 Dto 对象，用来执行后续操作
            CommentDto buffComment = tagMap.get(comment.getId());

            // 没有父消息的内容直接存放到 resultList
            if(StringUtils.isEmpty(comment.getFatherMessageId())) {
                resultList.add(buffComment);
            } else {
                // 作为子节点与父节点进行组装
                if(tagMap.containsKey(comment.getFatherMessageId())) {
                    tagMap.get(comment.getFatherMessageId()).getChildMessage().add(buffComment);
                } else {
                    log.error("出现了错误父子关系的评论:" + JSON.toJSONString(comment));
                    resultList.add(buffComment);
                }
            }
        }
        return resultList;
    }
}
