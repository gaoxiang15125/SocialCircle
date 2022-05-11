package github.zlg.socialcircle.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import github.zlg.socialcircle.module.define.DatabaseEnum;
import github.zlg.socialcircle.module.define.selfexception.MyException;
import github.zlg.socialcircle.module.dto.modulecontent.ModuleContentDto;
import github.zlg.socialcircle.module.entity.content.ModuleContent;
import github.zlg.socialcircle.module.entity.like.LikeContentRecord;
import github.zlg.socialcircle.module.entity.like.ThumbUpContentRecord;
import github.zlg.socialcircle.module.mapper.like.LikeContentRecordMapper;
import github.zlg.socialcircle.module.mapper.like.ThumbUpContentRecordMapper;
import github.zlg.socialcircle.module.mapper.module.ModuleContentMapper;
import github.zlg.socialcircle.module.util.DataChangeUtil;
import github.zlg.socialcircle.server.service.ModuleContentService;
import github.zlg.socialcircle.server.service.UsualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: social-circle-main
 * @description: 板块内容具体接口实现类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-20 13:02
 **/
@Service
public class ModuleContentServiceImpl implements ModuleContentService {

    ModuleContentMapper moduleContentMapper;

    UsualService usualService;

    LikeContentRecordMapper likeContentRecordMapper;

    ThumbUpContentRecordMapper thumbUpContentRecordMapper;

    @Autowired
    public ModuleContentServiceImpl(ModuleContentMapper moduleContentMapper,
                                    UsualService usualService,
                                    LikeContentRecordMapper likeContentRecordMapper,
                                    ThumbUpContentRecordMapper thumbUpContentRecordMapper) {
        this.moduleContentMapper = moduleContentMapper;
        this.usualService = usualService;
        this.likeContentRecordMapper = likeContentRecordMapper;
        this.thumbUpContentRecordMapper = thumbUpContentRecordMapper;
    }

    @Override
    public List<ModuleContentDto> queryModuleContentByPageWithModuleId(String moduleId, int pageNum, int contentNum, DatabaseEnum.ORDER_TYPE orderType) {
        int start = pageNum * contentNum;

        List<ModuleContent> moduleContents = null;
        if(DatabaseEnum.ORDER_TYPE.DESC == orderType) {
            moduleContentMapper.getContentOrderByTimeDESCWithModuleId(moduleId, start, contentNum);
        } else {
            moduleContentMapper.getContentOrderByTimeASCWithModuleId(moduleId, start, contentNum);
        }
        return changeModuleListToDto(moduleContents);
    }

    @Override
    public int getModuleContentNumByModuleId(String circleModuleId) {
        QueryWrapper<ModuleContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleContent::getCircleModuleId, circleModuleId);
        return moduleContentMapper.selectCount(queryWrapper);
    }

    @Override
    public int getModuleContentNumByCircleId(String circleId) {
        QueryWrapper<ModuleContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleContent::getSocialCircleId, circleId);
        return moduleContentMapper.selectCount(queryWrapper);
    }

    @Override
    public ModuleContentDto addModuleContent(ModuleContentDto moduleContentDto, String circleId, String userUUID, String moduleId) {
        ModuleContent moduleContent = DataChangeUtil.changeDtoToEntity(moduleContentDto);
        moduleContent.setCreatorId(userUUID);
        moduleContent.setCircleModuleId(moduleId);
        moduleContent.setSocialCircleId(circleId);
        moduleContentMapper.insert(moduleContent);
        return DataChangeUtil.changeEntityToDto(moduleContent);
    }

    @Override
    public boolean updateModuleContent(ModuleContentDto moduleContentDto) {
        ModuleContent moduleContent = DataChangeUtil.changeDtoToEntity(moduleContentDto);
        UpdateWrapper<ModuleContent> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(ModuleContent::getId, moduleContent.getId())
                .set(ModuleContent::getContentInfo, moduleContent.getContentInfo());

        // TODO mybatisPlus 官方说不能复用指啥？ 对象不能重复使用吧，I guess
        return moduleContentMapper.update(moduleContent, updateWrapper) > 0;
    }

    @Override
    @Transactional
    public boolean deleteModuleContent(String moduleContentId) {
        // TODO 同时删除点赞与收藏记录 ？？
        // 删除点赞与收藏记录
        QueryWrapper<ThumbUpContentRecord> thumbUpWrapper = new QueryWrapper<>();
        thumbUpWrapper.lambda().eq(ThumbUpContentRecord::getModuleContentId, moduleContentId);
        thumbUpContentRecordMapper.delete(thumbUpWrapper);

        QueryWrapper<LikeContentRecord> likeContentWrapper = new QueryWrapper<>();
        likeContentWrapper.lambda().eq(LikeContentRecord::getModuleContentId, moduleContentId);
        likeContentRecordMapper.delete(likeContentWrapper);

        return moduleContentMapper.deleteById(moduleContentId) > 0;
    }

    @Override
    @Transactional
    public ModuleContentDto thumbUpModuleContent(String moduleContentId, String userUUID) throws MyException {
        // 能否点赞所有对象判断逻辑相同，因此合并到此方法内
        if(isUserCouldThumbContent(moduleContentId, userUUID)) {
            throw new MyException("不可以重复点赞同一内容");
        }

        // 写入点赞记录，同时点赞数量 +1； 使用了乐观锁，不必担心写覆盖问题
        // 只能先读再写
        ModuleContent moduleContent = moduleContentMapper.selectById(moduleContentId);
        ThumbUpContentRecord thumbUpContentRecord = new ThumbUpContentRecord();
        thumbUpContentRecord.setUserId(userUUID);
        thumbUpContentRecord.setModuleContentId(moduleContentId);
        thumbUpContentRecordMapper.insert(thumbUpContentRecord);

        // 修改点赞数，同步操作交给乐观锁
        moduleContent.setThumbUpNum(moduleContent.getThumbUpNum() + 1);
        moduleContentMapper.updateById(moduleContent);

        return DataChangeUtil.changeEntityToDto(moduleContent);
    }

    @Override
    @Transactional
    public ModuleContentDto collectionModuleContent(String moduleContentId, String userUUID) throws MyException {
        // 能否点赞所有对象判断逻辑相同，因此合并到此方法内
        if(isUserCouldCollectContent(moduleContentId, userUUID)) {
            throw new MyException("不可以重复收藏同一内容");
        }

        // 写入点赞记录，同时点赞数量 +1； 使用了乐观锁，不必担心写覆盖问题
        // 只能先读再写
        ModuleContent moduleContent = moduleContentMapper.selectById(moduleContentId);
        LikeContentRecord likeContentRecord = new LikeContentRecord();
        likeContentRecord.setUserId(userUUID);
        likeContentRecord.setModuleContentId(moduleContentId);
        likeContentRecordMapper.insert(likeContentRecord);

        // 修改点赞数，同步操作交给乐观锁
        moduleContent.setThumbUpNum(moduleContent.getThumbUpNum() + 1);
        moduleContentMapper.updateById(moduleContent);

        return DataChangeUtil.changeEntityToDto(moduleContent);
    }

    @Override
    public boolean isUserManagerModuleContent(String moduleContentId, String userUUID) {
        QueryWrapper<ModuleContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ModuleContent::getId, moduleContentId)
                .eq(ModuleContent::getCreatorId, userUUID);
        return moduleContentMapper.selectCount(queryWrapper) > 0;
    }


    /**
     * 将 moduleContentList 转化为 moduleContentDtoList 返回
     * @param moduleContents
     * @return
     */
    private List<ModuleContentDto> changeModuleListToDto(List<ModuleContent> moduleContents) {
        List<ModuleContentDto> moduleContentDtoList = new ArrayList<>();
        for(ModuleContent moduleContent: moduleContents) {
            moduleContentDtoList.add(DataChangeUtil.changeEntityToDto(moduleContent));
        }
        return moduleContentDtoList;
    }

    /**
     * 判断用户是否可以点赞帖子
     * @param moduleContentId 帖子 ID
     * @param userUUID 用户唯一识别 ID
     * @return true 用户可以点赞 false 用户无法重读点赞
     */
    private boolean isUserCouldThumbContent(String moduleContentId, String userUUID) {
        // 数据库为空返回 true 否则返回 false
        QueryWrapper<ThumbUpContentRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ThumbUpContentRecord::getModuleContentId, moduleContentId)
                .eq(ThumbUpContentRecord::getUserId, userUUID);

        return thumbUpContentRecordMapper.selectCount(queryWrapper) == 0;
    }

    /**
     * 判断用户是否可以收藏帖子
     * @param moduleContentId 帖子 ID
     * @param userUUID 用户唯一识别 ID
     * @return true 用户可以收藏 false 用户已经收藏过帖子了
     */
    private boolean isUserCouldCollectContent(String moduleContentId, String userUUID) {
        QueryWrapper<LikeContentRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(LikeContentRecord::getModuleContentId, moduleContentId)
                .eq(LikeContentRecord::getUserId, userUUID);
        return likeContentRecordMapper.selectCount(queryWrapper) == 0;
    }

}
