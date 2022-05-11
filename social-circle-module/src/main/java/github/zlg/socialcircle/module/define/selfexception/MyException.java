package github.zlg.socialcircle.module.define.selfexception;

import lombok.Data;

/**
 * @program: social-circle-main
 * @description: 自定义异常类型
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-14 16:26
 **/
@Data
public class MyException extends Exception {

    // 方便将来扩展或者做些修改

    public MyException(){
        super();
    }

    public MyException(String errorMessage) {
        super(errorMessage);
    }
}
