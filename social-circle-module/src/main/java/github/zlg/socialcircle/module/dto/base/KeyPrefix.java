package github.zlg.socialcircle.module.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @program: social-circle-main
 * @description: dto 统一索引定义类，为了在形式转化中更方便的加入 加解密操作
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-14 12:51
 **/
@Data
public abstract class KeyPrefix {

    @ApiModelProperty(value = "当前 dto 唯一识别 id，新建时为空")
    private String id;

    /**
     * 创建时间，发现问题就立刻改正，拖来拖去 难道就不用改了吗？
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime creatTime;
}
