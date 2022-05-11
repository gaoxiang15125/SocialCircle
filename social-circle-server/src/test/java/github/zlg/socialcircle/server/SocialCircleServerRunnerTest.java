package github.zlg.socialcircle.server;

import github.zlg.socialcircle.api.config.WeiXinAppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @program: social-circle-main
 * @description:
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-30 11:13
 **/
@RunWith(SpringRunner.class)
public class SocialCircleServerRunnerTest {

    @Autowired
    WeiXinAppConfig weiXinAppConfig;

    @Test
    public void testWeiXinConfig() {
        System.out.println(weiXinAppConfig.getAppId());
        System.out.println(weiXinAppConfig.getSecretId());
    }
}