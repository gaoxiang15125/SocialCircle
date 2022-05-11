package github.zlg.socialcircle.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: social-circle-main
 * @description: 微信小程序 api 用户信息、身份验证实体类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-18 16:30
 **/
@Data
public class WeiXinUserVo extends WeiXinVo implements Serializable {

    /**
     * 用户唯一标识
     */
    String openid;

    /**
     * 会话密钥
     */
    String session_key;

    /**
     *用户开放平台唯一标识
     */
    String unionid;
}
