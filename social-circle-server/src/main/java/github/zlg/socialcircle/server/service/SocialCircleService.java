package github.zlg.socialcircle.server.service;

import github.zlg.socialcircle.module.define.selfexception.MyException;
import github.zlg.socialcircle.module.dto.socialcircle.SocialCircleDto;

import java.util.List;

/**
 * @program: social-circle-main
 * @description: 圈子相关对外接口类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-02 17:03
 **/
public interface SocialCircleService {

    /**
     * 获取用户参加的圈子数量
     * @param userUUID 用户唯一识别 Id
     * @return
     */
    int getUserJoinSocialCircleNum(String userUUID);

    /**
     * 获取用户创建的圈子数量
     * @param userUUID 用户唯一识别 Id
     * @return
     */
    int getUserCreatSocialCircleNum(String userUUID);

    /**
     * 根据用户 openId 获取该用户收藏的圈子
     * @param userUUID
     * @return
     */
    List<SocialCircleDto> getUserJoinSocialCircle(String userUUID, int pageNum, int contentNum);

    /**
     * 根据用户 openId 获取用户创建的圈子
     * @param userUUID
     * @return
     */
    List<SocialCircleDto> getUserCreatSocialCircle(String userUUID, int pageNum, int contentNum);

    /**
     * 退出目标圈子
     * @param socialCircleId 目标圈子 Id
     * @return 成功返回 true 否则返回 false
     */
    boolean quitTargetSocialCircle(String socialCircleId, String userUUID);

    /**
     * 删除指定 id 的圈子
     * @param circleId 圈子 id
     * @return
     */
    boolean deleteSocialCircle(String circleId, String userUUID);

    /**
     * 创建指定圈子
     * @param socialCircleDto 圈子相关内容
     * @param userUUID 用户唯一识别 id
     * @return
     */
    SocialCircleDto creatSocialCircle(SocialCircleDto socialCircleDto, String userUUID);

    /**
     * 收藏指定圈子
     * @param socialCircleId 加密的目标圈子 id
     * @param openid 用户唯一识别码
     * @return 收藏结果
     */
    Boolean joinSocialCircle(String socialCircleId, String openid) throws MyException;

    /**
     * 判断用户是否属于某圈子
     * @param socialCircleId 圈子 id
     * @param openid 用户 id
     * @return 属于返回 true 否则返回 false
     */
    Boolean isUserInSocialCircle(String socialCircleId, String openid);

    /**
     * 分页返回所有的圈子信息
     * @param longitude
     * @param latitude
     * @param pageNum
     * @param contentNum
     * @return
     */
    List<SocialCircleDto> getAllSocialCircleByPage(double longitude, double latitude, int pageNum, int contentNum);

    /**
     * 分页返回一定范围内的社交圈子
     * @param range
     * @param longitude
     * @param latitude
     * @param pageNum
     * @param contentNum
     * @return
     */
    List<SocialCircleDto> getRangeSocialCircleByPage(double range, double longitude, double latitude, int pageNum, int contentNum);

    /**
     * 返回一定范围内社交圈子的总数，这个明显会有很大的问题 TODO
     */
    int getRangeSocialCircleNum(double longitude, double latitude, double range);

    /**
     * 获取所有的圈子数量
     * @return 圈子数量
     */
    int getAllSocialCircleNum();

    /**
     * 根据id 获取圈子详细信息(包括成员、板块等信息）
     */
    SocialCircleDto getDetailedSocialCircleInfo(String socialCircleId, double longitude, double latitude);

}
