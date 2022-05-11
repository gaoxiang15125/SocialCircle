package github.zlg.socialcircle.module.dto.socialcircle;


import github.zlg.socialcircle.module.dto.base.KeyPrefix;
import github.zlg.socialcircle.module.dto.modulecontent.ModuleContentDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: socialcircle
 * @description: 圈子板块信息 Dto
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-15 19:04
 **/
@Data
@ApiModel(value = "CircleModule", description = "圈子板块信息")
public class CircleModuleDto extends KeyPrefix {

    /**
     * 板块名称
     */
    @ApiModelProperty(value = "板块名称")
    private String moduleName;

    /**
     * 板块描述
     */
    @ApiModelProperty(value = "板块描述")
    private String moduleDescription;

    /**
     * 板块所属圈子
     */
    @ApiModelProperty(value = "板块所属圈子Id")
    private String circleId;
}
