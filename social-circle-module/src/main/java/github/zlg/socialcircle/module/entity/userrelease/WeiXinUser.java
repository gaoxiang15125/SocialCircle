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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * @program: socialcircle
 * @description: 微信登录相关数据
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-16 11:41
 **/
@Data
@Table
public class WeiXinUser extends PrefixTable {

    /**
     * 微信端唯一识别 ID，非 UUID 格式，因此额外存储
     */
    @Column(comment = "微信端唯一识别ID")
    private String openId;

    /**
     * 用户昵称
     */
    @Column(comment = "用户昵称")
    private String userName;

    /**
     * 用户头像
     */
    @Column(comment = "用户头像 url", type = MySqlTypeConstant.TEXT)
    private String userLogo;

    /**
     * 用户性别
     */
    @Column(comment = "用户性别")
    private int sex;

    /**
     * 用户所在国家
     */
    @Column(comment = "用户所在国家")
    private String country;

    /**
     * 用户所在省份
     */
    @Column(comment = "用户所在省份")
    private String province;

    /**
     * 用户所在城市
     */
    @Column(comment = "用户所在城市")
    private String city;

    /**
     * 属性语言 zh_CN en
     */
    @Column(comment = "语言")
    private String language;

    /**
     * 忽略父类中非数据库表字段
     */
    public transient int currentPage = 1;

    public transient int pageSize = 10;

    public transient int start;

    public transient LinkedHashMap<String, String> orderBy;
}
