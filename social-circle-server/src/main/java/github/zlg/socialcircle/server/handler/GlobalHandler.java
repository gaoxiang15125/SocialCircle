package github.zlg.socialcircle.server.handler;

import github.zlg.socialcircle.module.define.ResponseEnum;
import github.zlg.socialcircle.module.http.ZlgResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: social-circle-main
 * @description: 全局Controller 处理类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-15 16:18
 **/
@Slf4j
@RestControllerAdvice
public class GlobalHandler {

    // 这里就是通用的异常处理器了,所有预料之外的Exception异常都由这里处理
    @ExceptionHandler(Exception.class)
    public ZlgResponse<Object> exceptionHandler(Exception e) {
        ZlgResponse<Object> response = new ZlgResponse<>();
        // 为了方便调试，错误直接通知前端
        response.setCode(ResponseEnum.PARAM_ERROR.getCode());
        response.setData(null);
        response.setMessage(e.getMessage());
        log.error(e.getMessage(), e);
        return response;
    }
}
