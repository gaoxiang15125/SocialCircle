package github.zlg.socialcircle.module.mapper.module;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import github.zlg.socialcircle.module.dto.modulecontent.ModuleContentDto;
import github.zlg.socialcircle.module.entity.content.ModuleContent;

import java.util.List;

/**
 * @program: social-circle-main
 * @description: 板块子选项描述类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-18 11:20
 **/
public interface ModuleContentMapper extends BaseMapper<ModuleContent> {

    /**
     * 根据 模块 Id 按照时间倒叙分页获取 帖子
     * @param moduleId
     * @param start
     * @param contentNum
     * @return
     */
    List<ModuleContent> getContentOrderByTimeDESCWithModuleId(String moduleId, int start, int contentNum);

    /**
     * 根据 模块 Id 按照时间顺序分页获取 帖子
     * @param moduleId
     * @param start
     * @param end
     * @return
     */
    List<ModuleContent> getContentOrderByTimeASCWithModuleId(String moduleId, int start, int end);
}
