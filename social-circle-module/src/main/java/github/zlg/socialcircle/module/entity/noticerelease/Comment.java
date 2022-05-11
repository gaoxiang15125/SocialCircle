package github.zlg.socialcircle.module.entity.noticerelease;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.command.BaseModel;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import github.zlg.socialcircle.module.entity.base.PrefixTable;
import lombok.Data;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @program: socialcircle
 * @description: 通知信息存储类，当全成员发起 @ 后，会通知指定成员
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-14 12:04
 **/
@Data
@Table
public class Comment extends PrefixTable {

    /**
     * 消息所属圈子
     */
    @Column(comment = "消息所属圈子")
    private String circleId;

    /**
     * 消息来源用户
     */
    @Column(comment = "消息来源用户 openId")
    private String userId;

    /**
     * 目标用户
     */
    @Column(comment = "消息目标用户 id, 非私密发送则当前字段无意义")
    private String targetUserId;

    /**
     * 消息所属内容（板块子项）
     */
    @Column(comment = "消息所属内容id，即板块子项 id")
    private String moduleId;

    /**
     * 父级消息 (维护两层层级关系)
     */
    @Column(comment = "父级消息存储id")
    private String fatherMessageId;

    /**
     * 消息内容 支持图文信息，这里需要前端进行配合，再讨论相关消息怎么传递
     * TODO
     * 计划存储 json 字符串，不再进行分表操作
     */
    @Column(comment = "消息内容存储字符串", type = MySqlTypeConstant.TEXT)
    String talkInfo;

    /**
     * 消息状态：0未读，1 已读
     */
    @Column(comment = "消息状态，已读/未读")
    int messageStatus;

    /**
     * 忽略父类中非数据库表字段
     */
    public transient int currentPage = 1;

    public transient int pageSize = 10;

    public transient int start;

    public transient LinkedHashMap<String, String> orderBy;
}
