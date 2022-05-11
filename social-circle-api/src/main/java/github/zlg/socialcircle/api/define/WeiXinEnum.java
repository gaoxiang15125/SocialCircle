package github.zlg.socialcircle.api.define;

import lombok.Getter;

/**
 * @program: social-circle-main
 * @description: 微信小程序 api 相关枚举信息
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-18 20:28
 **/
public class WeiXinEnum {

    /**
     * 使用微信作为登录手段，不再需要维护密码
     */
    public static final String DEFAULT_PASSWORD = "DEFAULT_PASSWORD";

    /**
     * 微信 api 调用 默认请求头
     */
    public static final String LOCAL_HOST = "https://api.weixin.qq.com";

    @Getter
    public enum ERROR_CODE {
        SYSTEM_BUSY(-1, "系统繁忙"), SUCCESS(0, "请求成功"), CODE_TIMEOUT(40029, "code 无效"),TURNS_LIMIT(45011, "频率限制");

        int code;
        String codeMessage;

        ERROR_CODE(int type, String sexStr) {
            this.code = type;
            this.codeMessage = sexStr;
        }

        public ERROR_CODE getSexByCode(int code) {
            for (ERROR_CODE sexType : ERROR_CODE.values()) {
                if (sexType.getCode() == code) {
                    return sexType;
                }
            }
            return SYSTEM_BUSY;
        }

        public ERROR_CODE getSexByStr(String sexStr) {
            for (ERROR_CODE sexType : ERROR_CODE.values()) {
                if (sexType.getCodeMessage().equals(sexStr)) {
                    return sexType;
                }
            }
            return SYSTEM_BUSY;
        }
    }

    @Getter
    public enum WEIXIN_URL {

        // 用户登录验证
        USER_TOKEN("/sns/jscode2session"),
        // 获取用户信息
        USER_INFO_GETTER("");


        String urlInfo;

        WEIXIN_URL(String partUrl) {
            this.urlInfo = partUrl;
        }

        public String getWholeUrl() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LOCAL_HOST);
            stringBuilder.append(urlInfo);
            return stringBuilder.toString();
        }
    }

}
