package github.zlg.socialcircle.server.controller;

import github.zlg.socialcircle.module.define.DatabaseEnum;
import github.zlg.socialcircle.module.define.ResponseEnum;
import github.zlg.socialcircle.module.define.selfexception.MyException;
import github.zlg.socialcircle.module.dto.socialcircle.SocialCircleDto;
import github.zlg.socialcircle.module.entity.circlerelease.SocialCircle;
import github.zlg.socialcircle.module.http.ZlgResponse;
import github.zlg.socialcircle.module.util.SecretUtil;
import github.zlg.socialcircle.server.service.SocialCircleService;
import github.zlg.socialcircle.server.service.UsualService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.iterators.ArrayListIterator;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @program: social-circle-main
 * @description: 社交圈子相关服务控制器
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-24 14:49
 **/
@Slf4j
@RequestMapping("/socialCircle")
@RestController
@Api(tags="圈子相关 API 接口")
public class SocialCircleController {

    SocialCircleService socialCircleService;

    UsualService usualService;

    @Autowired
    public SocialCircleController(SocialCircleService socialCircleService,
                                  UsualService usualService) {
        this.socialCircleService = socialCircleService;
        this.usualService = usualService;
    }

    /**
     * 获取用户收藏的圈子 分页
     */
    @GetMapping("/getJoinedSocialCircle")
    @ApiOperation(value = "分页的方式获取用户加入的圈子")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum", value = "页码", paramType = "query", dataType = "Integer", required = true),
            @ApiImplicitParam(name="contentNum", value = "每页内容数量", paramType = "query", dataType = "Integer", required = true)
    })
    public ZlgResponse<List<SocialCircleDto>> getUserJoinedSocialCircle(@RequestParam("pageNum") Integer page, @RequestParam("contentNum") Integer contentNum) {
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();

        List<SocialCircleDto> socialCircleDtoList = socialCircleService.getUserJoinSocialCircle(userUUID, page, contentNum);

        ZlgResponse<List<SocialCircleDto>> response = new ZlgResponse<>();
        response.setStatusByEnum(ResponseEnum.SUCCESS);
        response.setData(socialCircleDtoList);
        return response;
    }

    /**
     * 获取用户加入的圈子数量
     * @return
     */
    @GetMapping("/getJoinedSocialCircleNum")
    @ApiOperation(value = "获取用户加入的圈子总数")
    @ApiImplicitParams({
    })
    public ZlgResponse<Integer> getUserJoinedSocialCircleNum() {
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();

        int socialCircleNum = socialCircleService.getUserJoinSocialCircleNum(userUUID);

        ZlgResponse<Integer> response = new ZlgResponse<>();
        response.setStatusByEnum(ResponseEnum.SUCCESS);
        response.setData(socialCircleNum);
        return response;
    }

    /**
     * 获取用户创建的圈子
     */
    @GetMapping("/getCreatedSocialCircle")
    @ApiOperation(value = "分页的方式获取用户创建的圈子")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum", value = "页码", paramType = "query", dataType = "Integer", required = true),
            @ApiImplicitParam(name="contentNum", value = "每页内容数量", paramType = "query", dataType = "Integer", required = true)
    })
    public ZlgResponse<List<SocialCircleDto>> getCreatSocialCircle(int pageNum, int contentNum) {

        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();

        List<SocialCircleDto> socialCircleDtoList = socialCircleService.getUserCreatSocialCircle(userUUID, pageNum, contentNum);

        ZlgResponse<List<SocialCircleDto>> response = new ZlgResponse<>();
        response.setStatusByEnum(ResponseEnum.SUCCESS);
        response.setData(socialCircleDtoList);
        return response;
    }

    /**
     * 获取用户创建的圈子数量
     * @return
     */
    @GetMapping("/getCreatedSocialCircleNum")
    @ApiOperation(value = "获取用户加入的圈子总数")
    @ApiImplicitParams({
    })
    public ZlgResponse<Integer> getCreatSocialCircle() {

        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();
        int socialCircleNum = socialCircleService.getUserCreatSocialCircleNum(userUUID);

        ZlgResponse<Integer> response = new ZlgResponse<>();
        response.setStatusByEnum(ResponseEnum.SUCCESS);
        response.setData(socialCircleNum);
        return response;
    }

    /**
     * 退出某个圈子
     */
    @GetMapping("/quitSocialCircle")
    @ApiOperation(value = "退出某个圈子")
    @ApiImplicitParams({
            @ApiImplicitParam(name="circleId", value = "圈子Id", paramType = "query", dataType = "String", required = true)
    })
    public ZlgResponse<Boolean> cancelCollectSocialCircle(@RequestParam("circleId") String circleId) {
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();

        //对圈子 Id 进行解密操作
        String socialCircleId = SecretUtil.decryptId(circleId);
        boolean result = socialCircleService.quitTargetSocialCircle(userUUID, socialCircleId);
        ZlgResponse<Boolean> zlgResponse = ZlgResponse.<Boolean>builder()
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .data(result)
                .build();
        return zlgResponse;
    }

    /**
     * 获取附近一定范围内的 社交圈子
     */
    @GetMapping("/getNearBySocialCircle")
    @ApiOperation(value = "获取附近一定范围内的社交圈子")
    @ApiImplicitParams({
            @ApiImplicitParam(name="range", value = "距离 单位：米", paramType = "query", dataType = "Double", required = true),
            @ApiImplicitParam(name="longitude", value = "经度值 - 浮点数类型", paramType = "query", dataType = "Double", required = true),
            @ApiImplicitParam(name="latitude", value = "纬度值 - 浮点数类型", paramType = "query", dataType = "Double", required = true),
            @ApiImplicitParam(name="pageNum", value = "页码 - 整数类型", paramType = "query", dataType = "Double", required = true),
            @ApiImplicitParam(name="contentNum", value = "每页展示内容数量 - 整数类型", paramType = "query", dataType = "Double", required = true)
    })
    public ZlgResponse<List<SocialCircleDto>> getNearBySocialCircle(@RequestParam("range") Double range,
                                                            @RequestParam("longitude") Double longitude,
                                                            @RequestParam("latitude") Double latitude,
                                                            @RequestParam("pageNum") Integer pageNum,
                                                            @RequestParam("contentNum") Integer contentNum) {
        List<SocialCircleDto> result = socialCircleService.getRangeSocialCircleByPage(range, longitude, latitude, pageNum, contentNum);

        return ZlgResponse.<List<SocialCircleDto>>builder().data(result)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    /**
     * 获取附近的圈子总数
     */
    @GetMapping("/getNearBySocialCircleNum")
    @ApiOperation(value = "获取附近圈子总数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "range", value = "距离 单位: 米", paramType = "query", dataType = "Double", required = true),
            @ApiImplicitParam(name="longitude", value = "经度值", paramType = "query", dataType = "Double", required = true),
            @ApiImplicitParam(name="latitude", value = "纬度值", paramType = "query", dataType = "Double", required = true),
    })
    public ZlgResponse<Integer> getNearBySocialCircleNum(@RequestParam("range") Double range,
                                                         @RequestParam("longitude") Double longitude,
                                                         @RequestParam("latitude") Double latitude) {
        int nearCircleNum = socialCircleService.getRangeSocialCircleNum(longitude, latitude, range);

        return ZlgResponse.<Integer>builder().data(nearCircleNum)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    /**
     * 分页获取 全部的社交圈子
     */
    @GetMapping("/getAllSocialCircle")
    @ApiOperation(value = "分页获取全部社交圈子")
    @ApiImplicitParams({
            @ApiImplicitParam(name="longitude", value = "经度值 - 浮点数类型", paramType = "query", dataType = "Double", required = true),
            @ApiImplicitParam(name="latitude", value = "纬度值 - 浮点数类型", paramType = "query", dataType = "Double", required = true),
            @ApiImplicitParam(name="pageNum", value = "页码", paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name="contentNum", value = "每页展示内容数量", paramType = "query", dataType = "int", required = true)
    })
    public ZlgResponse<List<SocialCircleDto>> getAllSocialCircle(@RequestParam("longitude") Double longitude,
                                                        @RequestParam("latitude") Double latitude,
                                                        @RequestParam("pageNum") Integer pageNum,
                                                        @RequestParam("contentNum") Integer contentNum) {
        List<SocialCircleDto> socialCircleDtoList = socialCircleService.getAllSocialCircleByPage(longitude, latitude, pageNum, contentNum);
        return ZlgResponse.<List<SocialCircleDto>>builder().data(socialCircleDtoList)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    /**
     * 根据id 获取圈子详细信息（暂无圈子成员，仅包含创建者信息）,通过其他接口获取的圈子均为简略信息
     */
    @GetMapping("/getDetailedSocialCircleInfo")
    @ApiOperation(value = "根据 id 获取圈子详细信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "circleId", value = "圈子id", paramType = "query", dataType = "String", required = true),
        @ApiImplicitParam(name="longitude", value = "用户经度值 - 浮点数类型", paramType = "query", dataType = "Double", required = true),
        @ApiImplicitParam(name="latitude", value = "用户纬度值 - 浮点数类型", paramType = "query", dataType = "Double", required = true)
    })
    public ZlgResponse<SocialCircleDto> getDetailedSocialCircleInfo(@RequestParam("circleId") String circleId,
                                                                    @RequestParam("longitude") Double longitude,
                                                                    @RequestParam("latitude") Double latitude){
        String socialCircleId = SecretUtil.decryptId(circleId);
        SocialCircleDto socialCircleDto = socialCircleService.getDetailedSocialCircleInfo(socialCircleId, longitude, latitude);
        return ZlgResponse.<SocialCircleDto>builder().data(socialCircleDto)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    /**
     * 判断 当前用户是否收藏了 当前圈子
     */
    @GetMapping("/isUserInSocialCircle")
    @ApiOperation(value = "判断当前用户是否收藏了目标圈子")
    @ApiImplicitParams({
            @ApiImplicitParam(name="circleId", value = "圈子id", paramType = "query", dataType = "String", required = true),
     })
    public ZlgResponse<Boolean> isUserInSocialCircle(@RequestParam("circleId") String circleId) {
        String openId = (String) SecurityUtils.getSubject().getPrincipal();
        String socialCircleId = SecretUtil.decryptId(circleId);

        Boolean boolResult = socialCircleService.isUserInSocialCircle(socialCircleId, openId);

        return ZlgResponse.<Boolean>builder().data(boolResult)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    /**
     * 用户 收藏 圈子，确定两者的绑定关系
     */
    @GetMapping("/joinSocialCircle")
    @ApiOperation(value = "用户收藏圈子，可以通过界面找到目标圈子")
    @ApiImplicitParams({
            @ApiImplicitParam(name="circleId", value = "圈子id", paramType = "query", dataType = "String", required = true)
    })
    public ZlgResponse<Boolean> joinSocialCircleByUserId(@RequestParam("circleId") String circleId) throws MyException {
        ZlgResponse<Boolean> result = new ZlgResponse<>();

        String socialCircleId = SecretUtil.decryptId(circleId);
        String openId = (String)SecurityUtils.getSubject().getPrincipal();

        boolean joinResult = false;

        // 已收藏或圈主，禁止再次收藏圈子
        if(usualService.isUserInCircle(socialCircleId, openId)) {
            throw new MyException("该圈子已在收藏夹");
        } else {
            joinResult = socialCircleService.joinSocialCircle(socialCircleId, openId);
        }

        result.setData(joinResult);
        result.setStatusByEnum(ResponseEnum.SUCCESS);
        return result;
    }

    /**
     * 创建社交圈子
     */
    @PostMapping("/creatSocialCircle")
    @ApiOperation(value = "创建社交圈子")
    @ApiImplicitParams({
            @ApiImplicitParam(name="socialCircle", value = "创建社交圈子", paramType = "body", dataType = "SocialCircle", required = true),
            @ApiImplicitParam(name="longitude", value = "经度值 - 浮点数类型", paramType = "query", dataType = "Double", required = true),
            @ApiImplicitParam(name="latitude", value = "纬度值 - 浮点数类型", paramType = "query", dataType = "Double", required = true)
    })
    public ZlgResponse<SocialCircleDto> creatSocialCircle(@RequestBody SocialCircleDto socialCircle,@RequestParam Double longitude,@RequestParam Double latitude) {

        String userUUID = (String)SecurityUtils.getSubject().getPrincipal();

        socialCircle.setLongitude(longitude);
        socialCircle.setLatitude(latitude);

        SocialCircleDto socialCircleDto = socialCircleService.creatSocialCircle(socialCircle, userUUID);

        return ZlgResponse.<SocialCircleDto>builder().data(socialCircleDto)
                .code(ResponseEnum.SUCCESS.getCode())
                .message(ResponseEnum.SUCCESS.getMessage())
                .build();
    }

    /**
     * 解散圈子
     */
    @GetMapping("/deleteSocialCircle")
    @ApiOperation(value = "解散社交圈子")
    @ApiImplicitParams({
            @ApiImplicitParam(name="socialCircleId", value = "解散社交圈子", paramType = "query", dataType = "String", required = true),
    })
    public ZlgResponse<Boolean> deleteSocialCircle(String circleId) throws MyException {
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();
        String socialCircleId = SecretUtil.decryptId(circleId);

        ZlgResponse<Boolean> result = new ZlgResponse<>();
        boolean deleteResult = false;
        // 判断用户是否有权限
        if(!usualService.isUserManagerSocialCircle(socialCircleId, userUUID)) {
            throw new MyException("用户没有删除圈子的权限");
        } else {
            deleteResult = socialCircleService.deleteSocialCircle(socialCircleId, userUUID);
        }

        if(deleteResult) {
            result.setStatusByEnum(ResponseEnum.SUCCESS);
            result.setData(true);
        } else {
            result.setStatusByEnum(ResponseEnum.PARAM_ERROR);
            result.setMessage("目标圈子已删除或不存在");
            result.setData(false);
        }
        return result;
    }

//    /**
//     * 修改用户身份（只有圈子的创建者才有资格) // 先不考虑身份校验，毕竟有 shiro
//     */
//    @GetMapping("/changeIdentityInSocialCircle")
//    @ApiOperation(value = "修改用户身份，只有圈主才有资格")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="userId", value = "用户id,自己的ID", paramType = "query", dataType = "String", required = true),
//            @ApiImplicitParam(name="targetUserId", value = "被操作的用户的id", paramType = "query", dataType = "String", required = true),
//            @ApiImplicitParam(name="socialCircleId", value = "圈子id", paramType = "query", dataType = "String", required = true),
//            @ApiImplicitParam(name="memberType", value = "修改后用户类型", paramType = "query", dataType = "Integer", required = true),
//    })
//    public ZlgResponse<Boolean> changeIdentityInCircleByUserId(String userId, String targetUserId, String socialCircleId, Integer memberType ) {
//        return new ZlgResponse<>();
//    }

//    /**
//     * 将用户从圈子内移除,返回新的成员表
//     */
//    @GetMapping("/removeUserFromSocialCircle")
//    @ApiOperation(value = "修改用户身份，只有圈主才有资格")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="userId", value = "用户id,自己的ID", paramType = "query", dataType = "String", required = true),
//            @ApiImplicitParam(name="targetUserId", value = "被操作的用户的id", paramType = "query", dataType = "String", required = true),
//            @ApiImplicitParam(name="socialCircleId", value = "圈子id", paramType = "query", dataType = "String", required = true),
//            @ApiImplicitParam(name="memberType", value = "修改后用户类型", paramType = "query", dataType = "Integer", required = true),
//    })
//    public ZlgResponse<SocialCircleDto> removeUserFromSocialCircle(String userId, String targetUserId, String socialCircleId) {
//        return new ZlgResponse<>();
//    }

}
