package github.zlg.socialcircle.module.define.selfexception;

/**
 * @program: social-circle-main
 * @description: 数据库读取数据为空异常
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-17 15:26
 **/
public class DataBaseReadException extends Exception {

    public DataBaseReadException(){
        super();
    }

    public DataBaseReadException(String errorMessage) {
        super(errorMessage);
    }
}
