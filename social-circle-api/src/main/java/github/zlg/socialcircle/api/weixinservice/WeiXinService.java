package github.zlg.socialcircle.api.weixinservice;

import github.zlg.socialcircle.api.config.WeiXinAppConfig;
import github.zlg.socialcircle.api.define.WeiXinEnum;
import github.zlg.socialcircle.api.util.HttpUtils;
import github.zlg.socialcircle.module.define.selfexception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: social-circle-main
 * @description: 用户登录相关 api 调用
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-18 16:14
 **/
@Component
public class WeiXinService implements UserLogin{

    HttpUtils httpUtils;

    WeiXinAppConfig weiXinAppInfo;

    @Autowired
    public WeiXinService(HttpUtils httpUtils, WeiXinAppConfig weiXinAppInfo) {
        this.httpUtils = httpUtils;
        this.weiXinAppInfo = weiXinAppInfo;
    }

    @Override
    public String login(String token) throws MyException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("appid", weiXinAppInfo.getAppId());
        paramsMap.put("secret", weiXinAppInfo.getSecretId());

        paramsMap.put("js_code", token);
        paramsMap.put("grant_type", "authorization_code");

        String url = WeiXinEnum.WEIXIN_URL.USER_TOKEN.getWholeUrl()
                + "?appid={appid}&secret={secret}&js_code={js_code}&grant_type={grant_type}";

        String result = httpUtils.requestForGet(url,
                                                    paramsMap,
                                                    String.class);
        return result;
    }
}
