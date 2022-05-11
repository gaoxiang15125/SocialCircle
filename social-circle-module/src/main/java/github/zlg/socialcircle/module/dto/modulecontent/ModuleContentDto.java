package github.zlg.socialcircle.module.dto.modulecontent;

import github.zlg.socialcircle.module.dto.base.KeyPrefix;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: socialcircle
 * @description: 板块子项描述信息
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-15 19:09
 **/
@Data
@ApiModel(value = "ModuleContent", description = "板块子项描述信息")
public class ModuleContentDto extends KeyPrefix {

    // 也应该包含圈子与模块，后期扩展后可能需要这些条件检索
    /**
     * 圈子 ID
     */
    @ApiModelProperty(value = "帖子所属圈子唯一识别ID")
    private String circleId;

    /**
     * 板块 ID
     */
    @ApiModelProperty(value = "帖子所属板块唯一识别ID")
    private String moduleId;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "板块创建者相关信息")
    private String creatorOpenId;

    /**
     * 板块子项描述信息
     */
    @ApiModelProperty(value = "内容主题富文本内容")
    private String description;

    /**
     * 点赞数
     */
    @ApiModelProperty(value = "当前内容点赞数量")
    private int thumbUpNum;

    /**
     * 收藏数
     */
    @ApiModelProperty(value = "当前内容被收藏数量")
    private int collectionNum;

    /**
     * 按照时间顺序、回复顺序排布评论信息
     */
    @ApiModelProperty(value = "当前内容下评论信息，结构为 List + Tree")
    private List<CommentDto> talkRecordsList;
}
