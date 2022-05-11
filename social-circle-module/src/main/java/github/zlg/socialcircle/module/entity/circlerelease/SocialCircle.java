package github.zlg.socialcircle.module.entity.circlerelease;

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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @program: socialcircle
 * @description: 圈子定义信息实体类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-14 11:16
 **/
@Data
@Table
public class SocialCircle extends PrefixTable {

    /**
     * 圈子名称
     */
    @Column(comment = "圈子名称")
    private String circleName;

    /**
     * 圈子介绍
     */
    @Column(comment = "圈子描述信息", type = MySqlTypeConstant.TEXT)
    private String circleDescription;

    /**
     * 经度值
     */
    @Column(comment = "圈子经度值")
    private double longitude;

    /**
     * 纬度值
     */
    @Column(comment = "圈子纬度值")
    private double latitude;

    /**
     * 圈子头像
     */
    @Column(comment = "圈子头像", type = MySqlTypeConstant.TEXT)
    private String circleLogo;

    /**
     * 圈子头像
     */
    @Column(comment = "圈子头像")
    private LocalDateTime circleLogoTime;

    private transient double distance;

    /**
     * 忽略父类中非数据库表字段
     */
    public transient int currentPage = 1;

    public transient int pageSize = 10;

    public transient int start;

    public transient LinkedHashMap<String, String> orderBy;
}
