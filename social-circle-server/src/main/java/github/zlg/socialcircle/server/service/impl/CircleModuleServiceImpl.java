package github.zlg.socialcircle.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import github.zlg.socialcircle.module.define.selfexception.MyException;
import github.zlg.socialcircle.module.dto.socialcircle.CircleModuleDto;
import github.zlg.socialcircle.module.entity.content.CircleModule;
import github.zlg.socialcircle.module.mapper.module.CircleModuleMapper;
import github.zlg.socialcircle.module.util.DataChangeUtil;
import github.zlg.socialcircle.module.util.SecretUtil;
import github.zlg.socialcircle.server.config.SocialCircleConfig;
import github.zlg.socialcircle.server.service.CircleModuleService;
import github.zlg.socialcircle.server.service.UsualService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: social-circle-main
 * @description: 圈子板块服务实现类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-13 15:55
 **/
@Slf4j
@Service
public class CircleModuleServiceImpl implements CircleModuleService {

    CircleModuleMapper circleModuleMapper;

    UsualService usualService;

    SocialCircleConfig socialCircleConfig;

    @Autowired
    public CircleModuleServiceImpl(CircleModuleMapper circleModuleMapper,
                                   UsualService usualService,
                                   SocialCircleConfig socialCircleConfig) {
        this.circleModuleMapper = circleModuleMapper;
        this.usualService = usualService;
        this.socialCircleConfig = socialCircleConfig;
    }

    /**
     * 板块数量存在上限限制
     */
    @Override
    public List<CircleModuleDto> addCircleModule(CircleModuleDto circleModuleDto, String openId) throws MyException {

        // 检查板块上限，超出上限禁止创建新板块(前端同样进行拦载)
        if(getModuleContentNumInCircle(circleModuleDto.getCircleId()) >= socialCircleConfig.getSocialModuleNumLimit()) {
            throw new MyException("当前板块数量达到上限");
        }

        // 将板块信息转化后存储到数据库,主动装载板块创建者信息
        CircleModule circleModule = DataChangeUtil.changeDtoToEntity(circleModuleDto);
        circleModule.setCreatorOpenId(openId);

        circleModuleMapper.insert(circleModule);

        List<CircleModuleDto> circleModuleDtoList = usualService.getCircleModuleInfo(circleModule.getCircleId());
        return circleModuleDtoList;
    }

    @Override
    public boolean deleteCircleModule(CircleModuleDto circleModuleDto) {
        // 板块影响太多，不提供删除操作
        return false;
    }

    @Override
    public List<CircleModuleDto> updateCircleModule(CircleModuleDto circleModuleDto, String openId) throws MyException {
        // 更新板块信息，因为数据库内容存在较大出入，采取更新特定字段的策略
        // 仅更新 名称、描述、类型 需要前端保证传递正确的 圈子 id
        CircleModule circleModule = DataChangeUtil.changeDtoToEntity(circleModuleDto);
        // 更新圈子板块相关信息
        UpdateWrapper<CircleModule> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(CircleModule::getId, circleModule.getId())
                .set(CircleModule::getModuleName, circleModule.getModuleName())
                .set(CircleModule::getModuleDescription, circleModule.getModuleDescription())
                .set(CircleModule::getCircleType, circleModule.getCircleType())
                .set(CircleModule::getCreatorOpenId, openId);
        circleModuleMapper.update(circleModule, updateWrapper);

        List<CircleModuleDto> circleModuleDtoList = usualService.getCircleModuleInfo(circleModule.getCircleId());
        return circleModuleDtoList;
    }

    /**
     * 该方法目前看来也不会被调用
     * @param socialCircleId 板块 id
     * @return 对应板块id 的板块相关信息
     */
    @Override
    public List<CircleModuleDto> queryCircleModule(String socialCircleId) {

        return usualService.getCircleModuleInfo(socialCircleId);
    }

    /**
     * 获取指定圈子已存在的板块数量
     * @param circleId
     * @return
     */
    private int getModuleContentNumInCircle(String circleId) {
        QueryWrapper<CircleModule> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CircleModule::getCircleId, circleId);

        return circleModuleMapper.selectCount(queryWrapper);
    }

}
