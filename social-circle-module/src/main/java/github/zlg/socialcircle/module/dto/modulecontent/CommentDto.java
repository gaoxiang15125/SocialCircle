package github.zlg.socialcircle.module.dto.modulecontent;

import github.zlg.socialcircle.module.dto.base.KeyPrefix;
import github.zlg.socialcircle.module.entity.noticerelease.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: socialcircle
 * @description: 通知消息 Dto
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-15 21:58
 **/
@Data
@ApiModel(value = "NoticeMessage", description = "通知消息描述信息")
public class CommentDto extends KeyPrefix {

    /**
     * 消息来源用户信息 id , TODO 消息实体类中一定不能存在对象，必定会浪费很多空间
     * 父级目录不可见，由业务层处理后返回给前端
     */
    @ApiModelProperty(value = "消息发送者用户识别id")
    private String userId;

    @ApiModelProperty(value = "消息所属圈子索引Id")
    private String circleId;

    @ApiModelProperty(value = "板块子项(内容) id")
    private String moduleId;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息文本内容")
    private String message;

    /**
     * 消息状态: 0 未读，1已读
     */
    @ApiModelProperty(value = "消息状态，已读未读")
    int messageStatus;

    @ApiModelProperty(value = "消息子结构")
    List<CommentDto> childMessage;

    // 构造方法内对数据结构进行初始化
    public CommentDto() {
        childMessage = new ArrayList<>();
    }
}
