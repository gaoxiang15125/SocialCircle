package github.zlg.socialcircle.module.dto.user;

import ch.qos.logback.classic.pattern.LineSeparatorConverter;
import com.baomidou.mybatisplus.annotation.TableId;
import github.zlg.socialcircle.module.dto.base.KeyPrefix;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: socialcircle
 * @description: 用户相关信息 Dto
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-15 18:49
 **/
@Data
@ApiModel(value = "User", description = "用户信息")
public class UserDto extends KeyPrefix {

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称", required = true, example = "呵呵哒哒")
    private String userName;

    /**
     * 个人签名
     */
    @ApiModelProperty(value = "用户个性签名", required = true, example = "这位用户很懒，什么都没留下")
    private String description;

    /**
     * 用户性别
     */
    @ApiModelProperty(value = "用户性别", required = true, example = "男")
    private String sex;

    /**
     * 头像
     */
    @ApiModelProperty(value = "用户头像", required = true, example = "https://gitee.com/gaoxiang15125/pictureBed/raw/master/img/月下三兄贵.jpg")
    private String headImg;

}
