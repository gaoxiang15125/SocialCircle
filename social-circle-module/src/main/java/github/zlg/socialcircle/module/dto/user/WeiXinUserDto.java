package github.zlg.socialcircle.module.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: social-circle-main
 * @description: 微信用户信息存储类,仅用于接受微信数据，不与数据库实体类关联
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-30 16:48
 **/
@Data
@ApiModel(value = "WeiXinUser", description = "微信用户信息存储类")
public class WeiXinUserDto implements Serializable {

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称", example = "呵呵哒哒")
    String nickName;

    /**
     * 用户头像图片 Url
     */
    @ApiModelProperty(value = "用户头像 Url")
    String avatarUrl;

    /**
     * 用户性别
     */
    @ApiModelProperty(value = "用户性别")
    int gender;

    /**
     * 用户所在国家
     */
    @ApiModelProperty(value = "用户所在国家")
    String country;

    /**
     * 用户所在省份
     */
    @ApiModelProperty(value = "用户所在省份")
    String province;

    /**
     * 用户所在城市
     */
    @ApiModelProperty(value = "用户所在城市")
    String city;

    /**
     * 属性语言 zh_CN en
     */
    @ApiModelProperty(value = "用户语言")
    String language;

}
