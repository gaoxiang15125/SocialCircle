package github.zlg.socialcircle.api.util;

import github.zlg.socialcircle.module.define.selfexception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

/**
 * @program: social-circle-main
 * @description: http 请求发送工具类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-18 11:46
 **/
@Component
public class HttpUtils {

    RestTemplate restTemplate;

    @Autowired
    public HttpUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 同步发送 get 请求
     * @param url 请求地址
     * @param params 请求参数
     * @param clazz 返回结果类型
     * @param <T> 泛型
     * @return 对应类型的返回结果
     * @throws Exception
     */
    public <T>T requestForGet(String url, Map<String, Object> params, Class<T> clazz) throws MyException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.parseMediaType("application/json; charset=utf-8"));
        HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<T> response;
        response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, clazz, params);
        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new MyException("访问结果不符合预期，错误信息为：" + response.getBody().toString());
    }

    public <T> T requestForPost(String url, Map<String, Object> params, Class<T> clazz) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<T> response = null;
        response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, clazz);
        return response.getStatusCode().is2xxSuccessful() ? response.getBody() : null;
    }
}
