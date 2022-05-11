package github.zlg.socialcircle.module.entity.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.command.BaseModel;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Queue;

/**
 * @program: social-circle-main
 * @description: 定义表中相同字段，方便对其进行管理
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-15 15:10
 **/
@Data
public abstract class PrefixTable extends BaseModel {

    /**
     * 表默认 Id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @IsKey
    @Column(length = 64)
    private String id;

    @Column(comment = "最后一次更新的时间")
    private LocalDateTime creatTime;

}
