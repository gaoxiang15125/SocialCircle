package github.zlg.socialcircle.module.entity.content;

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
 * @description: 板块子项内容
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-14 11:53
 **/
@Data
@Table
public class ModuleContent extends PrefixTable {

    /**
     * 所属圈子
     */
    @Column(comment = "所属圈子 id")
    private String socialCircleId;

    /**
     * 所属板块
     */
    @Column(comment = "所属板块 id")
    private String circleModuleId;

    /**
     * 创建者
     */
    @Column(comment = "创建者 id")
    private String creatorId;

    /**
     * 模块内容 富文本消息; 包含@的用户
     */
    @Column(comment = "消息文本内容", type = MySqlTypeConstant.TEXT)
    private String contentInfo;

    /**
     * 点赞数
     */
    @Column(comment = "点赞数")
    private int thumbUpNum;

    /**
     * 收藏数
     */
    @Column(comment = "收藏数")
    private int collectionNum;

    /**
     * 忽略父类中非数据库表字段
     */
    public transient int currentPage = 1;

    public transient int pageSize = 10;

    public transient int start;

    public transient LinkedHashMap<String, String> orderBy;
}
