package github.zlg.socialcircle.module.mapper.socialcircle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import github.zlg.socialcircle.module.entity.circlerelease.SocialCircle;

import java.util.List;

/**
 * @program: social-circle-main
 * @description: 社交圈圈数据操作接口类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-18 11:22
 **/
public interface SocialCircleMapper extends BaseMapper<SocialCircle> {

    List<SocialCircle> getDistanceSocialCircleByPage(double longitude, double latitude, double range, int start, int contentNum);

    int getDistanceSocialCircleNum(double longitude, double latitude, double range);

    List<SocialCircle> getAllSocialCircleByPage(int start, int contentNum);

    int getAllSocialCircleNum();

}
