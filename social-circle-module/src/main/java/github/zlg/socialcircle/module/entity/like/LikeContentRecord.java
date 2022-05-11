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
 * @description: 收藏的板块内容
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-22 20:19
 **/
@Data
@Table
public class LikeContentRecord extends PrefixTable {

    /**
     * 用户 id
     */
    @Column(comment = "用户id")
    private String userId;

    /**
     * 内容 id 标识收藏的内容
     */
    @Column(comment = "内容id")
    private String moduleContentId;

    /**
     * 忽略父类中非数据库表字段
     */
    public transient int currentPage = 1;

    public transient int pageSize = 10;

    public transient int start;

    public transient LinkedHashMap<String, String> orderBy;
}
