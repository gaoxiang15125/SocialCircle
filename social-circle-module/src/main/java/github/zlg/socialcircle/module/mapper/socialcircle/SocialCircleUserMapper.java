package github.zlg.socialcircle.module.mapper.socialcircle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import github.zlg.socialcircle.module.entity.circlerelease.SocialCircle;
import github.zlg.socialcircle.module.entity.circlerelease.SocialCircleUser;

import java.util.List;

/**
 * @program: social-circle-main
 * @description: 社交圈——用户关联表
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-18 11:23
 **/
public interface SocialCircleUserMapper extends BaseMapper<SocialCircleUser> {

    List<SocialCircleUser> getUserJoinSocialCircleByPage(String userUUID, int memberType, int start, int contentNum);

    List<SocialCircleUser> getUserManagerSocialCircleByPage(String userUUID, int memberType, int start, int contentNum);

}
