package github.zlg.socialcircle.server.config;

import github.zlg.socialcircle.server.security.UserRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: social-circle-main
 * @description: Shiro 身份认证鉴权操作 Bean 配置类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-23 15:17
 **/
@Configuration
public class ShiroConfig {
    /**
     * 创建Realm bean会让方法返回的对象放入到spring的环境，以便使用
     */
    @Bean(name = "userRealm")
    public UserRealm getRealm(){
        return new UserRealm();
    }
    /**
     * 创建DefaultWebSecurityManager
     */
    @Bean(name = "defaultWebSecurityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager defaultWebSecurityManager=new DefaultWebSecurityManager();
        //关联realm，使用我们编写的鉴权操作 作为鉴权实现，感觉上是 装饰者模式
        defaultWebSecurityManager.setRealm(userRealm);
        return defaultWebSecurityManager;
    }
    /**
     * 创建shiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        //设置一个安全管理器来关联SecurityManager
        // 这个 filter 可以直接作为拦截器使用吗？？
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //TODO 暂时使用代码进行配置，肯定是应该通过配置文件配置的
        // 为什么使用 linkedMap ？？
        Map<String,String> filterMap = new LinkedHashMap<>();
        /**
         * 五种过滤器：
         * anon: 无需认证
         * authc: 权限认证后访问
         * user: 如果使用 rememberMe 功能可直接访问
         * perms: 必须得到资源权限才可以访问
         * role: 必须得到角色权限才可以访问
         */
        // url 地址应该是子地址全名吧  控制器+方法 url 就可以了
        filterMap.put("/testShiro/update", "authc");
        filterMap.put("/testShiro/add", "authc");

        shiroFilterFactoryBean.setLoginUrl("/user/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }
}
