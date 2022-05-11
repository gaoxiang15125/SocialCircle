package github.zlg.socialcircle.module.define;

import lombok.Getter;

/**
 * @program: social-circle-main
 * @description: 结果状态码枚举类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-24 12:45
 **/
@Getter
public enum ResponseEnum {

    SUCCESS(200, "请求成功"),
    PARAM_ERROR(-1, "请求参数不合法"),
    ACCOUNT_ERROR(444, "用户权限不正确"),
    ACCOUNT_NOT_FIND(404, "用户账号不存在"),
    PASSWORD_NOT_RIGHT(405, "用户密码不正确"),
    ERROR(999, null),
    // 数据库所冲突报错
    HIGH_CALLBACK_ERROR(-2, "短时间内重读提交，修改失败");

    String message;
    int code;

    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
