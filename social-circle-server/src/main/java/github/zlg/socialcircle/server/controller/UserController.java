package github.zlg.socialcircle.server.controller;

import com.alibaba.fastjson.JSON;
import github.zlg.socialcircle.api.define.WeiXinEnum;
import github.zlg.socialcircle.api.vo.WeiXinUserVo;
import github.zlg.socialcircle.api.weixinservice.WeiXinService;
import github.zlg.socialcircle.module.define.selfexception.MyException;
import github.zlg.socialcircle.module.define.ResponseEnum;
import github.zlg.socialcircle.module.dto.modulecontent.ModuleContentDto;
import github.zlg.socialcircle.module.dto.socialcircle.SocialCircleDto;
import github.zlg.socialcircle.module.dto.user.UserDto;
import github.zlg.socialcircle.module.dto.user.WeiXinUserDto;
import github.zlg.socialcircle.module.entity.circlerelease.SocialCircle;
import github.zlg.socialcircle.module.http.ZlgResponse;
import github.zlg.socialcircle.module.util.SecretUtil;
import github.zlg.socialcircle.server.service.SocialCircleService;
import github.zlg.socialcircle.server.service.UserService;
import github.zlg.socialcircle.server.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: social-circle-main
 * @description: 用户基本信息对外接口实现类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-23 12:51
 **/
@RequestMapping("/user")
@RestController
@Api(tags="用户相关 API 接口")
public class UserController {

    /**
     * 微信端交互组件
     */
    WeiXinService weiXinService;

    UserService userServiceImpl;

    @Autowired
    public UserController(WeiXinService weiXinService,
                          UserServiceImpl userServiceImpl,
                          SocialCircleService socialCircleService) {
        this.weiXinService = weiXinService;
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * 根据前端获取的 微信token 验证用户登录信息，记录用户登录状态
     * @param code 微信Token
     * @return
     */
    @GetMapping("/login")
    @ApiOperation(value = "验证用户登录权限", notes = "用户已注册则返回用户信息，否则 data 内容为空")
    @ApiImplicitParams({
            @ApiImplicitParam(name="code", value = "前端获取的验证码", paramType = "query", dataType = "String", required = true)
    })
    public ZlgResponse<UserDto> login(@RequestParam("code") String code) throws MyException {
        // 需要通过 token 获取用户信息，因此先通过微信验证拿到用户 id
        ZlgResponse<UserDto> response = new ZlgResponse<>();
        WeiXinUserVo weiXinUserVo = null;
//        String urlResult = weiXinService.login(code);
//
//        weiXinUserVo = JSON.parseObject(urlResult, WeiXinUserVo.class);
        weiXinUserVo = new WeiXinUserVo();
        weiXinUserVo.setOpenid("123456");
        weiXinUserVo.setUnionid("hehedada");
        weiXinUserVo.setSession_key("有趣的假设");
        if(weiXinUserVo.getErrcode() != WeiXinEnum.ERROR_CODE.SUCCESS.getCode()) {
            throw new MyException(weiXinUserVo.getErrmsg());
        }

        // 对于发送验证码的用户，为其构造空 user、weiXinUser 对象，获取 userId,存储到映射关系中
        String userUUID = userServiceImpl.signInApp(weiXinUserVo);

        Subject subject = SecurityUtils.getSubject();
        // 记录 微信 id 与 sessionId
        UsernamePasswordToken token = new UsernamePasswordToken(userUUID, WeiXinEnum.DEFAULT_PASSWORD);

        // 实际上这里的登录验证没有效果
        subject.login(token);
        token.setRememberMe(true);

        // 判断用户是否首次登录，将用户信息存储到返回对象中
        UserDto userDto = userServiceImpl.isUserInfoExist(userUUID);

        response.setStatusByEnum(ResponseEnum.SUCCESS);
        response.setData(userDto);
        return response;
    }

    /**
     * 该接口在调用登录接口后调用
     * @return
     * @throws MyException
     */
    @GetMapping("/isExist")
    @ApiOperation(value = "判断用户信息是否存在", notes = "也可以每次都尝试获取用户信息并登录，我们可以更新用户微信信息")
    public ZlgResponse<UserDto> isExist() throws MyException {
        // 此处会使用 shiro 进行拦截，不再额外判断是否为空了
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();

        // 懒省事，根据 UUID 获取用户信息，判断用户信息是否为空解决
        UserDto userDto = userServiceImpl.isUserInfoExist(userUUID);

        ZlgResponse<UserDto> response = new ZlgResponse<>();
        response.setStatusByEnum(ResponseEnum.SUCCESS);
        response.setData(userDto);
        return response;
    }

    @PostMapping("/addUserInfo")
    @ApiOperation(value = "将用户微信相关信息提交到后端")
    @ApiImplicitParam(name = "userDto", value = "用户信息结构体", paramType = "body", dataType = "WeiXinUser", required = true)
    public ZlgResponse<UserDto> addUserInfo(@RequestBody WeiXinUserDto userDto) {
        ZlgResponse<UserDto> zlgResponse = new ZlgResponse<>();
        UserDto user = null;
        // 通过 shiro 获取用户 openId
        String openId = (String) SecurityUtils.getSubject().getPrincipal();

        user = userServiceImpl.pullWeiXinUserInfo(userDto, openId);

        zlgResponse.setStatusByEnum(ResponseEnum.SUCCESS);
        zlgResponse.setData(user);
        return zlgResponse;
    }

    /**
     * 获取用户信息，用于特殊情况下在此获取用户信息
     * @return 包含用户信息的 response
     */
    @GetMapping("/getUserInfo")
    @ApiOperation(value = "获取用户信息")
    public ZlgResponse<UserDto> getUserInfo(@RequestParam(defaultValue = "") String userId) {
        ZlgResponse<UserDto> result = new ZlgResponse<>();
        String openid;
        if(userId == null || userId.isEmpty()) {
            openid = (String) SecurityUtils.getSubject().getPrincipal();
        } else {
            openid = SecretUtil.decryptId(userId);
        }
        UserDto userDto = userServiceImpl.getUserInfo(openid);
        result.setStatusByEnum(ResponseEnum.SUCCESS);
        result.setData(userDto);

        return result;
    }

    /**
     * 更新用户信息，提交整个 user 信息，后端根据提交内容更新数据库
     * @param userDto 用户信息
     * @return 成功则返回成功信息，否则提示 失败
     */
    @PostMapping("/updateUserInfo")
    @ApiOperation(value = "更新用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userDto", value = "用户修改后的信息", paramType = "body", dataType = "User", required = true)
    })
    public ZlgResponse<UserDto> updateUserInfo(@RequestBody UserDto userDto) {
        ZlgResponse<UserDto> response = new ZlgResponse<>();
        String userUUID = (String) SecurityUtils.getSubject().getPrincipal();
        UserDto user = userServiceImpl.updateUserInfo(userDto,userUUID);

        response.setData(user);
        response.setStatusByEnum(ResponseEnum.SUCCESS);
        return response;
    }

//    /**
//     * 获取用户收藏的 内容
//     */
//    @GetMapping("/getCollectionModuleContent")
//    @ApiOperation(value = "分页获取用户收藏的内容")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="userId", value = "用户唯一识别Id", paramType = "query", dataType = "String", required = true),
//            @ApiImplicitParam(name="pageNum", value = "页码", paramType = "query", dataType = "Integer", required = true),
//            @ApiImplicitParam(name="contentNum", value = "每页内容数量", paramType = "query", dataType = "Integer", required = true)
//    })
//    public ZlgResponse<List<ModuleContentDto>> getCollectModuleContent(@RequestParam("userId") String userId, @RequestParam("pageNum")Integer pageNum, @RequestParam("contentNum") Integer contentNum) {
//        return new ZlgResponse<>();
//    }

//    /**
//     * 获取用户收藏的内容总数
//     * @param userId 用户id
//     * @return
//     */
//    @GetMapping("/getCollectModuleContentNum")
//    @ApiOperation(value = "获取用户收藏的内容总数")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="userId", value = "用户唯一识别Id", paramType = "query", dataType = "String", required = true)
//    })
//    public ZlgResponse<Integer> getCollectModuleContentNum(@RequestParam("userId") String userId) {
//        return new ZlgResponse<>();
//    }

//    /**
//     * 取消收藏某个内容
//     */
//    @GetMapping("/cancelModuleContent")
//    @ApiOperation(value = "取消收藏某个内容")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="userId", value = "用户唯一识别Id", paramType = "query", dataType = "String", required = true),
//            @ApiImplicitParam(name="moduleId", value = "圈子Id", paramType = "query", dataType = "String", required = true)
//    })
//    public ZlgResponse<Boolean> cancelCollectModuleContent(@RequestParam("userId") String userId, @RequestParam("moduleId") String moduleContentId) {
//        return new ZlgResponse<>();
//    }
}
