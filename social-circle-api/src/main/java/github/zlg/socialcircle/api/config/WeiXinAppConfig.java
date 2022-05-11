package github.zlg.socialcircle.api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: social-circle-main
 * @description: 微信小程序相关配置信息加载类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-18 16:16
 **/
@Data
@Configuration
// TODO 从运行结果看 configuration 注解的类也被作为一个 bean
public class WeiXinAppConfig {

    /**
     * 微信小程序 app id
     */
    @Value("${Weixin.applicationId}")
    private String appId;

    @Value("${Weixin.applicationSercetId}")
    private String secretId;

    @Bean(name = "weiXinAppInfo")
    public WeiXinAppConfig weiXinAppConfig() {
        return this;
    }

}
