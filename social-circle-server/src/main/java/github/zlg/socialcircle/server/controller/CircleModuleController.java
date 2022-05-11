package github.zlg.socialcircle.server.controller;

import com.alibaba.druid.util.StringUtils;
import github.zlg.socialcircle.module.define.selfexception.MyException;
import github.zlg.socialcircle.module.define.ResponseEnum;
import github.zlg.socialcircle.module.dto.socialcircle.CircleModuleDto;
import github.zlg.socialcircle.module.entity.content.CircleModule;
import github.zlg.socialcircle.module.http.ZlgResponse;
import github.zlg.socialcircle.module.mapper.module.CircleModuleMapper;
import github.zlg.socialcircle.module.util.DataChangeUtil;
import github.zlg.socialcircle.module.util.SecretUtil;
import github.zlg.socialcircle.server.service.CircleModuleService;
import github.zlg.socialcircle.server.service.UsualService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: social-circle-main
 * @description: 板块控制器，简单的增删改查逻辑即可
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-13 14:25
 **/
@Slf4j
@RequestMapping("/circleModule")
@RestController
@Api(tags = "圈子板块相关接口")
public class CircleModuleController {

    UsualService usualService;

    CircleModuleService circleModuleService;

    @Autowired
    public CircleModuleController(CircleModuleService circleModuleService,
                                  UsualService usualService) {
        this.circleModuleService = circleModuleService;
        this.usualService = usualService;
    }

    // 只是一个很小的功能点，其实查根本不会用到

    /**
     * 添加新板块
     * @param circleModuleDto
     * @return
     */
    @PostMapping("/addCircleModule")
    @ApiOperation(value = "添加板块")
    @ApiImplicitParams({
            @ApiImplicitParam(name="circleModule", value = "板块信息，一定要带上圈子ID", paramType = "body", dataType = "CircleModule", required = true),
            @ApiImplicitParam(name = "circleId", value = "如果Dto内指定了圈子ID，此处可为空 圈子唯一识别ID", paramType = "query", dataType = "String", required = true)
    })
    public ZlgResponse<List<CircleModuleDto>> addCircleModule(@RequestBody CircleModuleDto circleModuleDto, @RequestParam(name = "circleId", defaultValue = "") String circleId) throws MyException {
        // 获取用户信息，只有 圈主与管理员 可以新建板块
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();
        if(StringUtils.isEmpty(circleId)) {
            circleId = circleModuleDto.getCircleId();
        }
        String socialCircleId = SecretUtil.decryptId(circleId);

        // 权限由 Controller 控制，方便之后复用 Service 模块
        if(!usualService.isUserManagerSocialCircle(socialCircleId, userUUID)) {
            throw new MyException("用户没有修改圈子板块的权限");
        }
        circleModuleDto.setCircleId(socialCircleId);
        List<CircleModuleDto> circleModules = circleModuleService.addCircleModule(circleModuleDto, userUUID);
        return ZlgResponse.<List<CircleModuleDto>>builder().data(circleModules)
                .message(ResponseEnum.SUCCESS.getMessage())
                .code(ResponseEnum.SUCCESS.getCode())
                .build();
    }

    @GetMapping("/updateCircleModule")
    @ApiOperation(value = "更新板块")
    @ApiImplicitParams({
            @ApiImplicitParam(name="circleModule", value = "板块信息，一定要带上圈子ID", paramType = "body", dataType = "CircleModule", required = true),
    })
    public ZlgResponse<List<CircleModuleDto>> updateCircleModule(@RequestBody CircleModuleDto circleModuleDto) throws MyException {
        String userUUID = (String)SecurityUtils.getSubject().getPrincipal();
        String socialCircleID = SecretUtil.decryptId(circleModuleDto.getCircleId());
        if(!usualService.isUserManagerSocialCircle(socialCircleID, userUUID)) {
            throw new MyException("用户没有修改圈子板块的权限");
        }

        List<CircleModuleDto> circleModules = circleModuleService.updateCircleModule(circleModuleDto, userUUID);

        return ZlgResponse.<List<CircleModuleDto>>builder().data(circleModules)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    @GetMapping("queryCircleModuleByCircleId")
    @ApiOperation(value = "查询圈子包含的板块信息，其他接口会返回该信息，不建议直接调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "circleId", value = "圈子唯一识别ID", paramType = "query", dataType = "String", required = true)
    })
    public ZlgResponse<List<CircleModuleDto>> getCircleModuleDtoByCircleId(@RequestParam("circleId") String circleId) {
        String socialCircleId = SecretUtil.decryptId(circleId);
        List<CircleModuleDto> circleModuleDtoList = circleModuleService.queryCircleModule(socialCircleId);
        return ZlgResponse.<List<CircleModuleDto>>builder().data(circleModuleDtoList)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }
}
