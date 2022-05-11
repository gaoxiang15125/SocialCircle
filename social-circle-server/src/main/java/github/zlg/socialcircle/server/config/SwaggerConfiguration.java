package github.zlg.socialcircle.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @program: social-circle-main
 * @description: Swagger 配置类，用于自动生成接口文档
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-22 15:23
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    /**
     * 扫描包路径
     */
    @Value("${swagger2.package-url}")
    private String packageUrl;

    /**
     * 开发者姓名
     */
    @Value("${swagger2.name}")
    private String developName;

    /**
     * 仓库 url
     */
    @Value("${swagger2.url}")
    private String developUrl;

    /**
     * 开发者联系方式
     */
    @Value("${swagger2.email}")
    private String developEmail;

    /**
     * 文档标题
     */
    @Value("${swagger2.title}")
    private String title;

    /**
     * 文档描述信息
     */
    @Value("${swagger2.description}")
    private String description;

    /**
     * 版本信息
     */
    @Value("${swagger2.version}")
    private String version;

    @Bean
    public Docket createRestApi() {
        // 创建 Docket 对象
        return new Docket(DocumentationType.SWAGGER_2) // 文档类型，使用 Swagger2
                .apiInfo(this.apiInfo()) // 设置 API 信息
                // 扫描 Controller 包路径，获得 API 接口
                .select()
                .apis(RequestHandlerSelectors.basePackage(packageUrl))
                .paths(PathSelectors.any())
                // 构建出 Docket 对象
                .build();
    }

    /**
     * 创建 API 信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version) // 版本号
                .contact(new Contact(developName, developUrl, developEmail)) // 联系人
                .build();
    }
}
