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
 * @description: 圈子模块相关信息存储表
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-14 11:42
 **/
@Data
@Table
public class CircleModule extends PrefixTable {

    /**
     * 所属圈子
     */
    @Column(comment = "所属圈子id")
    private String circleId;

    /**
     * 板块创建者
     */
    @Column(comment = "板块创建者id")
    private String creatorOpenId;

    /**
     * 板块类型：0图文内容；1 订单; 2 视频
     */
    @Column(comment = "板块类型")
    private int circleType;

    /**
     * 板块名称
     */
    @Column(comment = "板块名称")
    private String moduleName;

    /**
     * 板块描述
     */
    @Column(comment = "板块具体描述信息", type = MySqlTypeConstant.TEXT)
    private String moduleDescription;

    

    /**
     * 忽略父类中非数据库表字段
     */
    public transient int currentPage = 1;

    public transient int pageSize = 10;

    public transient int start;

    public transient LinkedHashMap<String, String> orderBy;
}
