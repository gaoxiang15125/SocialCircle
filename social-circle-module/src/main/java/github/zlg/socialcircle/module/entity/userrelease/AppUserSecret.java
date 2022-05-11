package github.zlg.socialcircle.module.entity.userrelease;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @description: 用户私密信息存储类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-15 18:47
 **/
@Data
@Table
public class AppUserSecret extends PrefixTable {

    /**
     * App 内用户唯一识别 Id
     */
    @Column(comment = "App 内用户唯一识别Id")
    private String userId;

    /**
     * 微信唯一标识
     */
    @Column(comment = "微信为 app 用户维护的 id")
    private String openId;

    /**
     * 微信开放平台唯一标识
     */
    @Column(comment = "不同app间，该标识是相同的")
    String unionid;

    /**
     * 忽略父类中非数据库表字段
     */
    public transient int currentPage = 1;

    public transient int pageSize = 10;

    public transient int start;

    public transient LinkedHashMap<String, String> orderBy;
}
