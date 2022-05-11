package github.zlg.socialcircle.server;

/**
 * @program: social-circle-main
 * @description: 通过main函数得方式启动程序
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-11-10 13:58
 **/
public class TestRunner {

    public static void main(String[] args) {
        SocialCircleServerRunner socialCircleServerRunner = new SocialCircleServerRunner();
        socialCircleServerRunner.Runners(args);
    }
}
