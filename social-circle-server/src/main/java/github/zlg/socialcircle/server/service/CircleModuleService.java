package github.zlg.socialcircle.server.service;

import github.zlg.socialcircle.module.define.selfexception.MyException;
import github.zlg.socialcircle.module.dto.socialcircle.CircleModuleDto;

import java.util.List;

/**
 * @program: social-circle-main
 * @description: 圈子板块服务相关接口
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-13 15:50
 **/
public interface CircleModuleService {

    /**
     * 为圈子添加新板块，限制板块数量，通过配置文件指定上限
     */
    List<CircleModuleDto> addCircleModule(CircleModuleDto circleModuleDto, String openId) throws MyException;

    /**
     * 删除圈子板块 (考虑到影响太大，不再对外提供）
     */
    boolean deleteCircleModule(CircleModuleDto circleModuleDto);

    /**
     * 更新圈子板块相关信息 TODO 为所有类型转化添加 key 值转化操作
     */
    List<CircleModuleDto> updateCircleModule(CircleModuleDto circleModuleDto, String openId) throws MyException;

    /**
     * 查询指定板块，该接口大概率调不到，开出来 以备不时之需
     */
    List<CircleModuleDto> queryCircleModule(String socialCircleId);

}
