package github.zlg.socialcircle.api.vo;

import lombok.Data;

/**
 * @program: social-circle-main
 * @description: 微信小程序父类属性
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-18 20:25
 **/
@Data
public abstract class WeiXinVo {

    /**
     * -1 系统繁忙  0 成功 40029 code 无效 45011频率限制
     */
    private int errcode;

    private String errmsg;
}
