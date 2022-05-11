package github.zlg.socialcircle.module.util;

import static org.junit.Assert.*;

/**
 * @program: social-circle-main
 * @description:
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-04-28 17:59
 **/
public class SecretUtilTest {

    @org.junit.Test
    public void creatAESKey() {
    }

    @org.junit.Test
    public void creatCipher() {
    }

    @org.junit.Test
    public void encryptId() {
        // 实际使用发现不能解析加密得到的结果，这里尝试一下
        String infos = SecretUtil.encryptId("有趣的假设");
        infos = SecretUtil.decryptId(infos);
        infos = SecretUtil.encryptId("580d714c4095906245fe1c186a0610d6");
        infos = SecretUtil.decryptId(infos);
    }

    @org.junit.Test
    public void decryptId() {
    }
}