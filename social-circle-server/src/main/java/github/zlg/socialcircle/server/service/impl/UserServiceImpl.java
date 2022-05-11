package github.zlg.socialcircle.server.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import github.zlg.socialcircle.api.vo.WeiXinUserVo;
import github.zlg.socialcircle.module.define.selfexception.MyException;
import github.zlg.socialcircle.module.dto.user.UserDto;
import github.zlg.socialcircle.module.dto.user.WeiXinUserDto;
import github.zlg.socialcircle.module.entity.userrelease.AppUserSecret;
import github.zlg.socialcircle.module.entity.userrelease.User;
import github.zlg.socialcircle.module.entity.userrelease.WeiXinUser;
import github.zlg.socialcircle.module.mapper.socialcircle.SocialCircleMapper;
import github.zlg.socialcircle.module.mapper.user.UserMapper;
import github.zlg.socialcircle.module.mapper.user.AppUserSecretMapper;
import github.zlg.socialcircle.module.mapper.user.WeiXinUserMapper;
import github.zlg.socialcircle.module.util.DataChangeUtil;
import github.zlg.socialcircle.module.util.EntityChangeUtil;
import github.zlg.socialcircle.server.service.UserService;
import github.zlg.socialcircle.server.service.UsualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @program: social-circle-main
 * @description: 用户数据库操作类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-29 18:41
 **/
@Service
public class UserServiceImpl implements UserService {

    UserMapper userMapper;

    WeiXinUserMapper weiXinUserMapper;

    AppUserSecretMapper appUserSecretMapper;

    SocialCircleMapper socialCircleMapper;

    UsualService usualService;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, WeiXinUserMapper weiXinUserMapper,
                           AppUserSecretMapper appUserSecretMapper,
                           SocialCircleMapper socialCircleMapper,
                           UsualService usualService) {
        this.userMapper = userMapper;
        this.weiXinUserMapper = weiXinUserMapper;
        this.appUserSecretMapper = appUserSecretMapper;
        this.socialCircleMapper = socialCircleMapper;
        this.usualService = usualService;
    }

    /**
     * 注册新用户, 为用户分配 UUID
     * @param weiXinUserVo 微信端验证结果
     * @return 如果用户成功注册过账户，则返回账户信息，否则返回 null
     * @throws MyException 数据库分配客户号失败
     */
    @Override
    @Transactional
    public String signInApp(WeiXinUserVo weiXinUserVo) throws MyException {

        // 第一次登录，分配 UUID 否则构造 UUID 并返回
        String UUID = isOpenIdExistInSecretTable(weiXinUserVo.getOpenid());
        if(!StringUtils.isEmpty(UUID)) {
            return UUID;
        }

        // 新用户：1.构建空 User 对象，为其分配客户号 2.构建绑定关系，存储openId 与 User 映射关系
        User user = new User();
        userMapper.insert(user);
        if(StringUtils.isEmpty(user.getId())) {
            throw new MyException("数据库分配 UserId 失败");
        }

        // 构建绑定关系对象
        AppUserSecret userSecret = new AppUserSecret();
        userSecret.setUserId(user.getId());
        userSecret.setOpenId(weiXinUserVo.getOpenid());
        userSecret.setUnionid(weiXinUserVo.getUnionid());
        userSecret.setCreatTime(LocalDateTime.now());

        // 插入只拥有 openId unionId 的实体类
        if(appUserSecretMapper.insert(userSecret) > 0 ) {
            // 对于新注册的用户，返回结果为 null
            return user.getId();
        }
        throw new MyException("数据库写入 AppUserSecret 表失败");
    }

    /**
     * 判断用户信息是否存在(根据 UUID 获取 微信用户信息)
     * @return true 存在； false 不存在
     */
    @Override
    public UserDto isUserInfoExist(String UUID) throws MyException {

        AppUserSecret secret = getAppUserSecretByUUID(UUID);

        // 根据 OpenId 尝试获取用户微信信息，判断用户是否完成注册
        QueryWrapper<WeiXinUser> weiXinQuery = new QueryWrapper<>();
        weiXinQuery.lambda().eq(WeiXinUser::getOpenId, secret.getOpenId());

        if(weiXinUserMapper.selectCount(weiXinQuery) > 0) {
            // 用户提交过微信信息，读取用户信息并返回
            User user = usualService.selectUserByUserId(secret.getUserId());
            return DataChangeUtil.changeEntityToDto(user);
        }
        return null;
    }

    /**
     * 提交微信用户信息，用来初始化我方用户信息
     * @param weiXinUserDto
     * @param userUUID
     * @return
     */
    @Override
    @Transactional
    public UserDto pullWeiXinUserInfo(WeiXinUserDto weiXinUserDto, String userUUID) {

        // 从 微信——用户 关系映射表里获取目标 OpenId
        AppUserSecret appUserSecret = getAppUserSecretByUUID(userUUID);

        WeiXinUser weiXinUser = DataChangeUtil.changeDtoToEntity(weiXinUserDto);
        weiXinUser.setOpenId(appUserSecret.getOpenId());

        if(usualService.isOpenIdExistInUserTable(appUserSecret.getOpenId())) {
            // 已存在用户信息，直接更新微信用户信息，并读取我方 app 信息返回
            updateWeiUserByOpenId(weiXinUser);
        } else {
            // 插入微信新用户
            weiXinUserMapper.insert(weiXinUser);

            // 为我方 app 用户赋值
            User user = EntityChangeUtil.changeWeiXinUserToUser(weiXinUser);
            user.setId(appUserSecret.getUserId());

            userMapper.updateById(user);
        }
        // 读取我方用户信息，作为结果返回 为了 userId 必须重读
        User user = usualService.selectUserByUserId(appUserSecret.getUserId());

        return DataChangeUtil.changeEntityToDto(user);
    }

    /**
     * 根据 openId 获取用户信息
     * @param userUUID
     * @return
     */
    @Override
    public UserDto getUserInfo(String userUUID) {
        // 读取我方用户信息，作为结果返回 为了 userId 必须重读
        User user = usualService.selectUserByUserId(userUUID);
        return DataChangeUtil.changeEntityToDto(user);
    }

    /**
     * 更新用户信息
     * @param userDto
     * @param userUUID app内用户唯一识别 ID
     * @return
     */
    @Override
    public UserDto updateUserInfo(UserDto userDto, String userUUID) {
        // 比较简单，不再拆成方法了
        User user = DataChangeUtil.changeDtoToEntity(userDto);
        // 虽然已经加密，但是还是通过 Shiro 获取 UUID 更为保险
        user.setId(userUUID);

        userMapper.updateById(user);
        return userDto;
    }

    /**
     * 判断用户是否登录过 app
     * @param openid 微信端返回的用户识别 Id
     * @return 我方 App 客户号
     */
    private String isOpenIdExistInSecretTable(String openid) {
        QueryWrapper<AppUserSecret> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AppUserSecret::getOpenId, openid);
        AppUserSecret appUserSecret = appUserSecretMapper.selectOne(queryWrapper);
        if(appUserSecret == null) {
            return null;
        }
        return appUserSecret.getUserId();
    }

    /**
     * 根据 openId 更新 微信用户信息
     * 如果存在则更新，否则插入  TODO 修改为 saveOrUpdate
     * @param user
     */
    private void updateWeiUserByOpenId(WeiXinUser user) {
        // 判断数据库中是否存在当前数据
        UpdateWrapper<WeiXinUser> weiXinUserWrapper = new UpdateWrapper<>();
        weiXinUserWrapper.lambda().eq(WeiXinUser::getOpenId, user.getOpenId());
        weiXinUserMapper.update(user, weiXinUserWrapper);
    }

    /**
     * 根据 UUID 获取 用户相关 key 关系表
     * @param UUID
     * @return
     */
    private AppUserSecret getAppUserSecretByUUID(String UUID) {
        QueryWrapper<AppUserSecret> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AppUserSecret::getUserId, UUID);
        return appUserSecretMapper.selectOne(queryWrapper);
    }
}
