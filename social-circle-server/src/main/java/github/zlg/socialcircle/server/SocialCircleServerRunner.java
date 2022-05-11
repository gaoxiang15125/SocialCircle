package github.zlg.socialcircle.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @program: social-circle-main
 * @description: 社交圈子启动类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-17 16:45
 **/
@SpringBootApplication
@EnableSwagger2
// TODO 可不可以将config 扫描路径转移到 配置文件
// 猜测需要重写 spring boot 的 配置加载方法
@ComponentScan(basePackages = {"github.zlg.socialcircle.server", "github.zlg.socialcircle.api"})
public class SocialCircleServerRunner {

    public void Runners(String[] args) {
        SpringApplication.run(SocialCircleServerRunner.class, args);
    }

    public static void main(String[] args) {
        SpringApplication.run(SocialCircleServerRunner.class, args);
    }
}
