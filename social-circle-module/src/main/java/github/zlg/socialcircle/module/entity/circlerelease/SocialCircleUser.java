package github.zlg.socialcircle.module.entity.circlerelease;

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
 * @program: socialcircle
 * @description: 圈子相关成员信息存储表
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-14 11:29
 **/
@Data
@Table
public class SocialCircleUser extends PrefixTable {

    /**
     * 圈子 id
     */
    @Column(comment = "用户对应的圈子 id")
    private String socialCircleId;

    /**
     * 用户 id
     */
    @Column(comment = "用户id")
    private String userId;

    /**
     * 用户角色类型，参见角色枚举类
     */
    @Column(comment = "用户角色类型")
    private int memberType;

    /**
     * 忽略父类中非数据库表字段
     */
    public transient int currentPage = 1;

    public transient int pageSize = 10;

    public transient int start;

    public transient LinkedHashMap<String, String> orderBy;
}
