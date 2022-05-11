package github.zlg.socialcircle.server.controller;

import github.zlg.socialcircle.module.define.ResponseEnum;
import github.zlg.socialcircle.module.define.selfexception.MyException;
import github.zlg.socialcircle.module.dto.modulecontent.CommentDto;
import github.zlg.socialcircle.module.entity.noticerelease.Comment;
import github.zlg.socialcircle.module.http.ZlgResponse;
import github.zlg.socialcircle.module.util.SecretUtil;
import github.zlg.socialcircle.server.service.CommentService;
import github.zlg.socialcircle.server.service.UsualService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: social-circle-main
 * @description: 消息控制器，消息相关接口
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-26 16:35
 **/
@RequestMapping("/comment")
@RestController
@Api(tags = "消息相关接口")
public class CommentController {

    CommentService commentService;

    UsualService usualService;

    @Autowired
    public CommentController(CommentService commentService,
                             UsualService usualService) {
        this.commentService = commentService;
        this.usualService = usualService;
    }

    @PostMapping("/addMessage")
    @ApiOperation(value = "发送消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="noticeMessage", value = "NoticeMessage", paramType = "body", dataType = "NoticeMessage", required = true),
            @ApiImplicitParam(name="fatherId", value = "父消息唯一识别Id", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name="circleId", value = "圈子唯一识别Id", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name="moduleId", value = "板块唯一识别Id", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name="userUUID", value = "用户唯一识别Id", paramType = "query", dataType = "String", required = true)
    })
    public ZlgResponse<CommentDto> addMessage(@RequestBody CommentDto commentDto,
                                              @RequestParam("fatherId") String fatherId,
                                              @RequestParam("circleId") String circleId,
                                              @RequestParam("moduleId") String moduleId,
                                              @RequestParam("userUUID") String userUUID) {
        // 对字段进行解密，并写入数据库
        fatherId = SecretUtil.decryptId(fatherId);
        circleId = SecretUtil.decryptId(circleId);
        moduleId = SecretUtil.decryptId(moduleId);
        userUUID = SecretUtil.decryptId(userUUID);

        commentDto = commentService.addComment(commentDto, fatherId, circleId, moduleId, userUUID);

        return ZlgResponse.<CommentDto>builder().data(commentDto)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    @GetMapping("/cancelMessage")
    @ApiOperation(value = "撤回消息，必须自己的消息才可撤回")
    @ApiImplicitParams({
            @ApiImplicitParam(name="noticeId", value = "用户消息Id", paramType = "query", dataType = "String", required = true),
    })
    public ZlgResponse<Boolean> cancelMessage(String noticeId) throws MyException {
        // 判断消息是否超出撤回时间，符合条件才允许撤回
        noticeId = SecretUtil.decryptId(noticeId);
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();
        if(!usualService.isUserManagerNoticeMessage(noticeId, userUUID)) {
            throw new MyException("用户没有撤回该消息的权限");
        }
        if(usualService.isMessageOutOfTime(noticeId)) {
            throw new MyException("消息超出撤回时间");
        }
        boolean result = commentService.cancelMessage(noticeId, userUUID);
        return ZlgResponse.<Boolean>builder().data(result)
                .message(ResponseEnum.SUCCESS.getMessage())
                .code(ResponseEnum.SUCCESS.getCode())
                .build();
    }

    @GetMapping("/getMessageByUserId")
    @ApiOperation(value = "根据用户id 获取其相关的消息，按时间顺序排布")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum", value = "页码", paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name="contentNum", value = "每页展示内容数量", paramType = "query", dataType = "int", required = true)
    })
    public ZlgResponse<List<CommentDto>> getMessageByUserId(int pageNum, int contentNum) {
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();
        List<CommentDto> commentDtoList = commentService.getCommentByUserId(userUUID, pageNum, contentNum);
        return ZlgResponse.<List<CommentDto>>builder().data(commentDtoList)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    @GetMapping("/getMessageByModuleContentId")
    @ApiOperation(value = "根据内容获取消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="contentModuleId", value = "用户消息Id", paramType = "query", dataType = "String", required = true),
    })
    public ZlgResponse<List<CommentDto>> getMessageByModuleContentId(String contentModuleId) {
        contentModuleId = SecretUtil.decryptId(contentModuleId);
        List<CommentDto> commentDtoList = commentService.getCommentByModuleContentId(contentModuleId);
        return ZlgResponse.<List<CommentDto>>builder().data(commentDtoList)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    @GetMapping("/getMessageBySocialCircleId")
    @ApiOperation(value = "根据圈子id获取消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="circleId", value = "圈子Id", paramType = "query", dataType = "String", required = true),
    })
    public ZlgResponse<List<CommentDto>> getMessageBySocialCircleId(@RequestParam String circleId) {
        circleId = SecretUtil.decryptId(circleId);
        List<CommentDto> commentDtoList = commentService.getCommentByCircleId(circleId);
        return ZlgResponse.<List<CommentDto>>builder().data(commentDtoList)
                .message(ResponseEnum.SUCCESS.getMessage())
                .code(ResponseEnum.SUCCESS.getCode())
                .build();
    }

    @GetMapping("/getMessageNumByUserId")
    @ApiOperation(value = "根据用户id 获取其相关的消息，按时间顺序排布")
    @ApiImplicitParams({
    })
    public ZlgResponse<Integer> getMessageNumByUserId() {
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();
        int commentNum = commentService.getCommentNumByUserId(userUUID);
        return ZlgResponse.<Integer>builder().data(commentNum)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    @GetMapping("/getMessageNumByModuleContentId")
    @ApiOperation(value = "根据内容获取消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="contentModuleId", value = "用户消息Id", paramType = "query", dataType = "String", required = true),
    })
    public ZlgResponse<Integer> getMessageNumByModuleContentId(String contentModuleId) {

        int commentNum = commentService.getCommentNumByModuleContentId(contentModuleId);
        return ZlgResponse.<Integer>builder().data(commentNum)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    @GetMapping("/getMessageNumByCircleId")
    @ApiOperation(value = "根据圈子id获取消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="circleId", value = "圈子Id", paramType = "query", dataType = "String", required = true),
    })
    public ZlgResponse<Integer> getMessageNumBySocialCircleId(@RequestParam("circleId") String circleId) {
        circleId = SecretUtil.decryptId(circleId);
        int commentNum = commentService.getCommentNumByCircleId(circleId);
        return ZlgResponse.<Integer>builder().data(commentNum)
                .message(ResponseEnum.SUCCESS.getMessage())
                .code(ResponseEnum.SUCCESS.getCode())
                .build();
    }

    /**
     * TODO 已读未读等之类的操作，我认为使用 netty 长连接来做更合理，否则频繁的调用一定会导致问题
     */
    // 当前版本不支持复杂的消息类型
    // TODO 提醒型消息当前暂时搁置
}
