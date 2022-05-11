package github.zlg.socialcircle.module.dto.socialcircle;

import github.zlg.socialcircle.module.dto.base.KeyPrefix;
import github.zlg.socialcircle.module.dto.user.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: socialcircle
 * @description: 圈子相关信息 Dto
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-15 18:54
 **/
@Data
@ApiModel(value = "SocialCircle", description = "圈子相关信息")
public class SocialCircleDto extends KeyPrefix {

    /**
     * 圈子名称
     */
    @ApiModelProperty(value = "圈子名称")
    private String circleName;

    /**
     * 圈子介绍
     */
    @ApiModelProperty(value = "圈子介绍")
    private String circleDescription;

    /**
     * 圈子 Logo
     */
    @ApiModelProperty(value = "圈子图片")
    private String circleLogo;

    /**
     * 经度值
     */
    @ApiModelProperty(value = "经度值")
    private double longitude;

    /**
     * 纬度值
     */
    @ApiModelProperty(value = "纬度值")
    private double latitude;

    /**
     * 距离
     */
    @ApiModelProperty(value = "圈子左边距离当前位置的距离 单位： m")
    private double distance;

    /**
     * 圈子总人数
     */
    @ApiModelProperty(value = "圈子总人数")
    private int peopleNum;

    /**
     * 圈主信息
     */
    @ApiModelProperty(value = "圈主相关信息")
    private UserDto leader;

    /**
     * 管理员信息
     */
    @ApiModelProperty(value = "管理员信息")
    private List<UserDto> managers;

    /**
     * 正常成员信息
     */
    @ApiModelProperty(value = "成员相关信息")
    private List<UserDto> members;

    /**
     * 圈子板块信息 数量不会太多，因此存储到数据库中
     */
    @ApiModelProperty(value = "可选子版块")
    private List<CircleModuleDto> circleModules;

}
