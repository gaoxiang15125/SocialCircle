package github.zlg.socialcircle.server.controller;

import github.zlg.socialcircle.module.define.DatabaseEnum;
import github.zlg.socialcircle.module.define.ResponseEnum;
import github.zlg.socialcircle.module.define.selfexception.MyException;
import github.zlg.socialcircle.module.dto.modulecontent.ModuleContentDto;
import github.zlg.socialcircle.module.http.ZlgResponse;
import github.zlg.socialcircle.module.util.SecretUtil;
import github.zlg.socialcircle.server.service.ModuleContentService;
import github.zlg.socialcircle.server.service.UsualService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @program: social-circle-main
 * @description: 帖子控制器
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-13 14:24
 **/
@RequestMapping("/ModuleContent")
@RestController
@Api(tags = "帖子相关 API 接口")
public class ModuleContentController {

    ModuleContentService moduleContentService;

    UsualService usualService;

    @Autowired
    public ModuleContentController(ModuleContentService moduleContentService,
                                   UsualService usualService) {
        this.moduleContentService = moduleContentService;
        this.usualService = usualService;
    }

    @GetMapping("/getModuleContentByModuleId")
    @ApiOperation(value = "创建新帖子,调用前请判断用户是否加入了圈子")
    @ApiImplicitParams({
            @ApiImplicitParam(name="moduleId", value = "板块ID", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name="pageNum", value = "页码", paramType = "query", dataType = "Integer", required = true),
            @ApiImplicitParam(name="contentNum", value = "每页内容数量", paramType = "query", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "orderType", value = "结果集排序方式, 0 时间倒叙 1 时间正序 默认为 0", paramType = "query", dataType = "Integer", required = true)
    })
    public ZlgResponse<List<ModuleContentDto>> queryModuleContentByPageWithModuleId(@RequestParam("moduleId") String moduleId,@RequestParam("pageNum") int pageNum,@RequestParam("contentNum") int contentNum,@RequestParam(name = "orderType", defaultValue = "0") int orderType) {
        DatabaseEnum.ORDER_TYPE orderEnum = DatabaseEnum.ORDER_TYPE.getTypeByCode(orderType);
        List<ModuleContentDto> moduleContentDtoList = moduleContentService.queryModuleContentByPageWithModuleId(moduleId, pageNum, contentNum, orderEnum);
        return ZlgResponse.<List<ModuleContentDto>>builder().data(moduleContentDtoList)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    @GetMapping("/getModuleContentNumByModuleId")
    @ApiOperation(value = "获取指定板块的帖子总数")
    @ApiImplicitParams({
            @ApiImplicitParam(name="moduleId", value = "板块ID", paramType = "query", dataType = "String", required = true)
    })
    public ZlgResponse<Integer> getModuleContentNumByModuleId(@RequestParam("moduleId") String moduleId) {
        int moduleContentNum = moduleContentService.getModuleContentNumByModuleId(moduleId);

        return ZlgResponse.<Integer>builder().data(moduleContentNum)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    @GetMapping("/getModuleContentNumByCircleId")
    @ApiOperation(value = "获取指定圈子的帖子总数")
    @ApiImplicitParams({
            @ApiImplicitParam(name="circleId", value = "圈子ID", paramType = "query", dataType = "String", required = true)
    })
    public ZlgResponse<Integer> getModuleContentNumByCircleId(@RequestParam("circleId") String circleId) {
        int contentNum = moduleContentService.getModuleContentNumByCircleId(circleId);
        return ZlgResponse.<Integer>builder().data(contentNum)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    @PostMapping("/creatModuleContent")
    @ApiOperation(value = "创建新帖子,调用前请判断用户是否加入了圈子")
    @ApiImplicitParams({
            @ApiImplicitParam(name="moduleContent", value = "帖子内容描述类", paramType = "body", dataType = "ModuleContent", required = true),
            @ApiImplicitParam(name="circleId", value = "圈子Id", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name="moduleId", value = "模块Id", paramType = "query", dataType = "String", required = true)
    })
    public ZlgResponse<ModuleContentDto> creatModuleContent(@RequestBody ModuleContentDto moduleContent, @RequestParam("circleId") String circleId,@RequestParam("moduleId") String moduleId) throws MyException {
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();
        String socialCircleId = SecretUtil.decryptId(circleId);
        String circleModuleId = SecretUtil.decryptId(moduleId);

        if(!usualService.isUserInCircle(socialCircleId,userUUID)) {
            throw new MyException("请加入圈子后在发布帖子");
        }

        ModuleContentDto moduleContentDto = moduleContentService.addModuleContent(moduleContent, socialCircleId, userUUID, circleModuleId);
        return ZlgResponse.<ModuleContentDto>builder().data(moduleContentDto)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    @PostMapping("/updateModuleContent")
    @ApiOperation(value = "更新帖子，只有作者有权限更新,多线程更新失败会返回 false，建议返回前端提醒用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="moduleContent", value = "帖子内容描述类", paramType = "body", dataType = "ModuleContent", required = true)
    })
    public ZlgResponse<Boolean> updateModuleContent(ModuleContentDto moduleContent) throws MyException {
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();
        String moduleContentId = SecretUtil.decryptId(moduleContent.getId());

        if(!moduleContentService.isUserManagerModuleContent(moduleContentId, userUUID)) {
            throw new MyException("用户没有修改帖子的权限");
        }

        boolean result = moduleContentService.updateModuleContent(moduleContent);

        if(result) {
            return ZlgResponse.<Boolean>builder().data(true)
                    .code(ResponseEnum.SUCCESS.getCode())
                    .message(ResponseEnum.SUCCESS.getMessage())
                    .build();
        }
        return ZlgResponse.<Boolean>builder().data(false)
                .code(ResponseEnum.HIGH_CALLBACK_ERROR.getCode())
                .message(ResponseEnum.HIGH_CALLBACK_ERROR.getMessage())
                .build();
    }

    @GetMapping("/deleteModuleContent")
    @ApiOperation(value = "删除帖子，只有作者有权限删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name="moduleContentId", value = "帖子唯一识别Id", paramType = "query", dataType = "String", required = true)
    })
    public ZlgResponse<Boolean> deleteModuleContent(@RequestParam("moduleContentId") String moduleContentId) throws MyException{
        String userUUID = (String)SecurityUtils.getSubject().getPrincipal();
        String moduleContentID = SecretUtil.decryptId(moduleContentId);

        // 检查用户权限
        if(moduleContentService.isUserManagerModuleContent(moduleContentID, userUUID)) {
            throw new MyException("用户没有删除帖子的权限");
        }

        boolean result = moduleContentService.deleteModuleContent(moduleContentID);
        if(result) {
            return ZlgResponse.<Boolean>builder().data(true)
                    .message(ResponseEnum.SUCCESS.getMessage())
                    .code(ResponseEnum.SUCCESS.getCode())
                    .build();
        } else {
            return ZlgResponse.<Boolean>builder().data(false)
                    .message(ResponseEnum.HIGH_CALLBACK_ERROR.getMessage())
                    .code(ResponseEnum.HIGH_CALLBACK_ERROR.getCode())
                    .build();
        }
    }

    @GetMapping("/thumbUpModuleContent")
    @ApiOperation(value = "为帖子点赞，返回更新点赞数后的帖子信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="moduleContentId", value = "帖子唯一识别Id", paramType = "query", dataType = "String", required = true)
    })
    public ZlgResponse<ModuleContentDto> thumbUpModuleContent(@RequestParam("moduleContentId") String moduleContentId) throws MyException {
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();
        String moduleContentID = SecretUtil.decryptId(moduleContentId);

        ModuleContentDto moduleContentDto = moduleContentService.thumbUpModuleContent(moduleContentID, userUUID);

        return ZlgResponse.<ModuleContentDto>builder().data(moduleContentDto)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    @GetMapping("/collectionModuleContent")
    @ApiOperation(value = "收藏帖子，返回更新收藏数后的帖子信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="moduleContentId", value = "帖子唯一识别Id", paramType = "query", dataType = "String", required = true)
    })
    public ZlgResponse<ModuleContentDto> collectionModuleContent(@RequestParam("moduleContentId") String moduleContentId) throws MyException {
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();
        String moduleContentID = SecretUtil.decryptId(moduleContentId);

        ModuleContentDto moduleContentDto = moduleContentService.collectionModuleContent(moduleContentID, userUUID);

        return ZlgResponse.<ModuleContentDto>builder().data(moduleContentDto)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }
}

