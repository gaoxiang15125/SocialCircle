package github.zlg.socialcircle.module.entity.userrelease;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsAutoIncrement;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.command.BaseModel;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import github.zlg.socialcircle.module.entity.base.PrefixTable;
import lombok.Data;

import java.util.LinkedHashMap;

/**
 * @program: socialcircle
 * @description: 用户信息实体类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-14 10:59
 **/
@Data
@Table
public class User extends PrefixTable {

    /**
     * 用户昵称
     */
    @Column(comment = "用户昵称")
    private String userName;

    /**
     * 个人签名
     */
    @Column(comment = "用户个人签名", type = MySqlTypeConstant.TEXT)
    private String description;

    /**
     * 用户头像 Logo
     */
    @Column(comment = "用户头像 url", type = MySqlTypeConstant.TEXT)
    private String userLogo;

    /**
     * 性别,读入系统后转化为 枚举类型
     */
    @Column(comment = "用户性别")
    private int sex;

    /**
     * 忽略父类中非数据库表字段
     */
    public transient int currentPage = 1;

    public transient int pageSize = 10;

    public transient int start;

    public transient LinkedHashMap<String, String> orderBy;
}
