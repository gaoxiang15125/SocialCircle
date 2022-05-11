package github.zlg.socialcircle.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import github.zlg.socialcircle.module.define.DatabaseEnum;
import github.zlg.socialcircle.module.define.selfexception.MyException;
import github.zlg.socialcircle.module.dto.socialcircle.SocialCircleDto;
import github.zlg.socialcircle.module.dto.user.UserDto;
import github.zlg.socialcircle.module.entity.circlerelease.SocialCircle;
import github.zlg.socialcircle.module.entity.circlerelease.SocialCircleUser;
import github.zlg.socialcircle.module.entity.userrelease.User;
import github.zlg.socialcircle.module.mapper.module.ModuleContentMapper;
import github.zlg.socialcircle.module.mapper.socialcircle.SocialCircleMapper;
import github.zlg.socialcircle.module.mapper.socialcircle.SocialCircleUserMapper;
import github.zlg.socialcircle.module.mapper.user.UserMapper;
import github.zlg.socialcircle.module.util.DataChangeUtil;
import github.zlg.socialcircle.module.util.DistanceUtil;
import github.zlg.socialcircle.module.util.SecretUtil;
import github.zlg.socialcircle.server.service.SocialCircleService;
import github.zlg.socialcircle.server.service.UsualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: social-circle-main
 * @description: 圈子相关服务实现类 TODO 有些命令应该使用 批处理实现
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-02 17:04
 **/
@Service
public class SocialCircleServiceImpl implements SocialCircleService {

    SocialCircleUserMapper socialCircleUserMapper;

    SocialCircleMapper socialCircleMapper;

    ModuleContentMapper moduleContentMapper;

    UserMapper userMapper;

    UsualService usualService;

    @Autowired
    public SocialCircleServiceImpl(SocialCircleUserMapper socialCircleUserMapper,
                                   SocialCircleMapper socialCircleMapper,
                                   ModuleContentMapper moduleContentMapper,
                                   UserMapper userMapper,
                                   UsualService usualService) {
        this.socialCircleMapper = socialCircleMapper;
        this.socialCircleUserMapper = socialCircleUserMapper;
        this.moduleContentMapper = moduleContentMapper;
        this.userMapper = userMapper;
        this.usualService = usualService;
    }

    @Override
    public int getUserJoinSocialCircleNum(String userUUID) {
        QueryWrapper<SocialCircleUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SocialCircleUser::getUserId, userUUID)
                .le(SocialCircleUser::getMemberType, DatabaseEnum.MEMBER_TYPE.QManager.getCode());

        return socialCircleUserMapper.selectCount(queryWrapper);
    }

    @Override
    public int getUserCreatSocialCircleNum(String userUUID) {
        QueryWrapper<SocialCircleUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SocialCircleUser::getUserId, userUUID)
                .eq(SocialCircleUser::getMemberType, DatabaseEnum.MEMBER_TYPE.QLeader.getCode());

        return socialCircleUserMapper.selectCount(queryWrapper);
    }

    /**
     * 根据用户 openid 获取该用户收藏的圈子
     * @param userUUID
     * @return
     */
    @Override
    public List<SocialCircleDto> getUserJoinSocialCircle(String userUUID, int pageNum, int contentNum){
        // 根据 用户-圈子表 获取用户加入的圈子
        int start = pageNum * contentNum;
        List<SocialCircleUser> socialCircleUserList = socialCircleUserMapper.getUserJoinSocialCircleByPage(userUUID, DatabaseEnum.MEMBER_TYPE.QManager.getCode(), start, contentNum);
        return getSocialCircleBySocialUser(socialCircleUserList);
    }

    /**
     * 根据用户id 获取用户创建的圈子
     */
    public List<SocialCircleDto> getUserCreatSocialCircle(String userUUID, int pageNum, int contentNum) {
        int start = pageNum * contentNum;
        List<SocialCircleUser> socialCircleUserList = socialCircleUserMapper.getUserManagerSocialCircleByPage(userUUID, DatabaseEnum.MEMBER_TYPE.QLeader.getCode(), start, contentNum);
        return getSocialCircleBySocialUser(socialCircleUserList);
    }

    @Override
    public boolean quitTargetSocialCircle(String socialCircleId, String userUUID) {
        QueryWrapper<SocialCircleUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SocialCircleUser::getUserId, userUUID)
                .eq(SocialCircleUser::getSocialCircleId, socialCircleId);
        return socialCircleUserMapper.delete(queryWrapper) > 0;
    }


    /**
     * 根据 圈子id 删除指定 圈子
     * @param socialCircleId 圈子id
     * @return
     */
    @Transactional
    @Override
    public boolean deleteSocialCircle(String socialCircleId, String userUUID) {

        // 删除用户——圈子关系表组件
        QueryWrapper<SocialCircleUser> circleUserWrapper = new QueryWrapper<>();
        circleUserWrapper.lambda().eq(SocialCircleUser::getSocialCircleId, socialCircleId);

        // 删除圈子信息
        QueryWrapper<SocialCircle> socialWrapper = new QueryWrapper<>();
        socialWrapper.lambda().eq(SocialCircle::getId, socialCircleId);

        int deleteUser = socialCircleUserMapper.delete(circleUserWrapper);
        int deleteCircle = socialCircleMapper.delete(socialWrapper);

        if(deleteCircle==0 || deleteUser==0) {
            return false;
        }
        return true;
    }

    /**
     * 新建 社交圈子
     * @param socialCircleDto 社交圈子 Dto
     * @param userUUID 用户唯一识别 id
     * @return
     */
    @Transactional
    @Override
    public SocialCircleDto creatSocialCircle(SocialCircleDto socialCircleDto, String userUUID) {
        SocialCircle socialCircle = DataChangeUtil.changeDtoToEntity(socialCircleDto);
        socialCircleMapper.insert(socialCircle);

        // TODO 确定 mybatis-plus 插入数据后，会不会自动赋值
        SocialCircleUser socialCircleUser = new SocialCircleUser();
        socialCircleUser.setSocialCircleId(socialCircle.getId());
        socialCircleUser.setUserId(userUUID);
        socialCircleUser.setMemberType(DatabaseEnum.MEMBER_TYPE.QLeader.getCode());
        socialCircleUserMapper.insert(socialCircleUser);

        socialCircleDto.setId(SecretUtil.encryptId(socialCircle.getId()));
        return socialCircleDto;
    }

    /**
     * 将目标圈子加入用户的收藏
     * @param socialCircleId 加密的目标圈子 id
     * @param userUUID 用户唯一识别 id
     * @return
     * @throws Exception
     */
    @Override
    public Boolean joinSocialCircle(String socialCircleId, String userUUID) throws MyException {

        SocialCircleUser socialCircleUser = new SocialCircleUser();
        socialCircleUser.setUserId(userUUID);
        socialCircleUser.setSocialCircleId(socialCircleId);
        socialCircleUser.setMemberType(DatabaseEnum.MEMBER_TYPE.member.getCode());
        socialCircleUserMapper.insert(socialCircleUser);
        return true;
    }

    /**
     * 判断用户是否收藏了目标圈子 加解密操作由 Controller 层控制
     * @param socialCircleId 圈子id
     * @param openId 用户唯一识别 id
     * @return
     * @throws Exception
     */
    @Override
    public Boolean isUserInSocialCircle(String socialCircleId, String openId) {
        return usualService.isUserInCircle(socialCircleId, openId);
    }

    /**
     * 分页获取所有圈子
     * @param pageNum 页码
     * @param contentNum 每页内容数量
     * @return
     */
    @Override
    public List<SocialCircleDto> getAllSocialCircleByPage(double longitude, double latitude, int pageNum, int contentNum) {
        int start = pageNum * contentNum;
        List<SocialCircle> circles = socialCircleMapper.getAllSocialCircleByPage(start, contentNum);

        return changeSocialCircleList(circles, longitude, latitude);
    }

    /**
     * 分页获取附近的圈子
     * @param range 搜索范围，单位为米
     * @param longitude 经度值
     * @param latitude 纬度值
     * @param pageNum 页码
     * @param contentNum 每页数量
     * @return 附近的圈子链表
     */
    @Override
    public List<SocialCircleDto> getRangeSocialCircleByPage(double range, double longitude, double latitude, int pageNum, int contentNum) {
        List<SocialCircle> circles = socialCircleMapper.getDistanceSocialCircleByPage(longitude, latitude, range, pageNum * contentNum, contentNum);

        return changeSocialCircleList(circles, longitude, latitude);
    }

    /**
     * 获取 范围内圈子数量
     * @param longitude
     * @param latitude
     * @param range
     * @return
     */
    @Override
    public int getRangeSocialCircleNum(double longitude, double latitude, double range) {

        return socialCircleMapper.getDistanceSocialCircleNum(longitude, latitude, range);
    }

    /**
     * 获取全部圈子的数量
     * @return
     */
    @Override
    public int getAllSocialCircleNum() {
        return socialCircleMapper.getAllSocialCircleNum();
    }

    /**
     * TODO 第一版本仅组装： 圈主 板块信息
     * @param socialCircleId 圈子 id
     * @return
     */
    @Override
    public SocialCircleDto getDetailedSocialCircleInfo(String socialCircleId, double longitude, double latitude) {
        SocialCircle socialCircle = socialCircleMapper.selectById(socialCircleId);
        SocialCircleDto socialCircleDto = DataChangeUtil.changeEntityToDto(socialCircle);

        // 装载 距离、圈主、圈子总人数、圈子板块 信息
        socialCircleDto.setDistance(DistanceUtil.getDistance(longitude, latitude, socialCircle.getLongitude(), socialCircle.getLatitude()));

        // 获取圈子圈主绑定关系，绑定圈主信息到实体类
        SocialCircleUser owner = usualService.getCircleOwnerInfo(socialCircleId);
        User user = usualService.selectUserByUserId(owner.getUserId());
        UserDto userDto = DataChangeUtil.changeEntityToDto(user);
        socialCircleDto.setLeader(userDto);

        socialCircleDto.setPeopleNum(usualService.getPeopleNumInCircle(socialCircleId));

        // 装载圈子板块信息
        socialCircleDto.setCircleModules(usualService.getCircleModuleInfo(socialCircleId));
        return socialCircleDto;
    }


    private List<SocialCircleDto> getSocialCircleBySocialUser(List<SocialCircleUser> socialCircleUsers) {
        // 获取该用户参与的所有圈子
        List<SocialCircleDto> circles = new ArrayList<>();
        for(SocialCircleUser userCircle: socialCircleUsers) {
            // 根据 圈子用户关系表 组装用户关注的圈子信息
            // TODO 修改为批处理操作
            SocialCircle socialCircle = socialCircleMapper.selectById(userCircle.getSocialCircleId());
            SocialCircleDto socialCircleDto = DataChangeUtil.changeEntityToDto(socialCircle);
            circles.add(socialCircleDto);
        }
        return circles;
    }

    private List<SocialCircleDto> changeSocialCircleList(List<SocialCircle> circleList, double longitude, double latitude) {
        // TODO 个人认为使用 mysql 的距离计算会更准确，但是因为这样那样的原因，暂时使用 数学算法进行计算
        List<SocialCircleDto> socialCircleDtoList = new ArrayList<>();

        for(SocialCircle socialCircle: circleList) {
            SocialCircleDto circleDto = DataChangeUtil.changeEntityToDto(socialCircle);
            // 计算距离，作为参数返回
            circleDto.setDistance(DistanceUtil.getDistance(circleDto.getLongitude(), circleDto.getLatitude(),
                    longitude, latitude));
            socialCircleDtoList.add(circleDto);
        }
        return socialCircleDtoList;
    }

}
