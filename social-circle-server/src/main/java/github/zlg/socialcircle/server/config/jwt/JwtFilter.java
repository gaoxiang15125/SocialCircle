//package github.zlg.socialcircle.server.config.jwt;
//
//import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//
///**
// * @program: social-circle-main
// * @description: JWT 核心过滤器
// * @author: gaoxiang
// * @email: 630268696@qq.com
// * @create: 2021-04-05 18:54
// **/
//public class JwtFilter extends BasicHttpAuthenticationFilter {
//
//    /**
//     * 判断用户是否想要进行 需要验证的操作
//     * 检测header里面是否包含Authorization字段即可
//     */
//    @Override
//    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
//        String auth = getAuthzHeader(request);
//        return auth != null && !auth.equals("");
//
//    }
//    /**
//     * 此方法调用登陆，验证逻辑
//     */
//    @Override
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//        if (isLoginAttempt(request, response)) {
//            JwtToken token = new JwtToken(getAuthzHeader(request));
//            getSubject(request, response).login(token);
//        }
//        return true;
//    }
//    /**
//     * 提供跨域支持
//     */
//    @Override
//    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
//        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
//        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
//        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
//        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
//            httpServletResponse.setStatus(HttpStatus.OK.value());
//            return false;
//        }
//        return super.preHandle(request, response);
//    }
//}
