package github.zlg.socialcircle.module.entity.like;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.command.BaseModel;
import github.zlg.socialcircle.module.entity.base.PrefixTable;
import lombok.Data;

import java.util.LinkedHashMap;

/**
 * @program: social-circle-main
 * @description: 点赞内容记录表，个人只能点一个赞
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-22 20:18
 **/
@Data
@Table
public class ThumbUpContentRecord extends PrefixTable {

    /**
     * 用户id
     */
    @Column(comment = "用户id")
    private String userId;

    /**
     * 内容 id
     */
    @Column(comment = "帖子 id")
    private String moduleContentId;

    /**
     * 忽略父类中非数据库表字段
     */
    public transient int currentPage = 1;

    public transient int pageSize = 10;

    public transient int start;

    public transient LinkedHashMap<String, String> orderBy;
}
