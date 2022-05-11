package github.zlg.socialcircle.server.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @program: social-circle-main
 * @description: 社交圈圈用户自定义配置信息，从配置文件加载
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-13 16:08
 **/
@Getter
@Component
public class SocialCircleConfig {

    /**
     * 圈子内模块上限
     */
    @Value("${SocialCircle.Socialmodule.numLimit}")
    Integer socialModuleNumLimit;

    @Value("${SocialCircle.NoticeMessage.timeOutLimit}")
    Integer noticeCancelTimeLimit;
}
