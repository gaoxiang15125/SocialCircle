package github.zlg.socialcircle.module.http;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import github.zlg.socialcircle.module.define.ResponseEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: social-circle-main
 * @description: http 返回实体类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-22 16:49
 **/
@Data
@Builder
@AllArgsConstructor
@ApiModel(value = "Response", description = "数据返回包装类")
public class ZlgResponse<T> implements Serializable {

    /**
     * 结果码
     */
    @Column(comment = "返回结果码", value = "200 请求成功;-1 参数不合法;444 用户权限不正确;404 路径不存在;405 用户密码错误")
    int code;

    /**
     * 返回信息
     */
    @Column(comment = "具体返回结果")
    T data;

    /**
     * 错误描述信息
     */
    @Column(comment = "错误描述信息")
    String message;


    public ZlgResponse() {}

    public void setStatusByEnum(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

}
