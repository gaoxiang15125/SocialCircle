package github.zlg.socialcircle.module.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;


/**
 * @program: social-circle-main
 * @description: 负责堆数据库检索key 进行加密，避免对外暴漏数据库存储策略
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-29 19:03
 **/
@Slf4j
public class SecretUtil {

    static Key aesKey = creatAESKey();
    static Cipher cipher = creatCipher();

    /**
     * 创建密钥 每次启动项目均不相同
     * @return
     */
    public static Key creatAESKey() {
        // 生产 KEY
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();
            log.info("创建的密钥内容为：", keyBytes);
            return new SecretKeySpec(keyBytes, "AES");
        } catch (NoSuchAlgorithmException e) {
            log.error("创建加密 key 过程出现错误", e);
        }
        return null;
    }

    /**
     * 设置加密模式
     * @return
     */
    public static Cipher creatCipher() {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.error("创建加密算法执行类出现错误", e);
        }
        return cipher;
    }

    /**
     * 加密数据库 tableId
     * @param id 数据库内存储的 id
     * @return
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String encryptId(String id) {
        // 理论上讲数据库 id 不会为空，需要注意插入后 Id 会不会存储返回值
//         如果为空，应该抛出错误 TODO
        try {
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] result = cipher.doFinal(id.getBytes());
            // 避免出现需要 url编码 的字符串
            return Base64.encodeBase64URLSafeString(result);
        } catch (Exception e) {
            log.error("加密 tableId 过程出现错误", e);
        }
        return null;
    }

    /**
     * 解密 tableId，
     * @param tableId Dto 传递的 id
     * @return 数据库实际存储的 id
     */
    public static String decryptId(String tableId) {
        if(tableId == null || tableId.isEmpty()) {
            return null;
        }
        byte[] result = null;
        try{
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            result = cipher.doFinal(Base64.decodeBase64(tableId));
            return new String(result);
        } catch (Exception e) {
            log.error("解密 tableId 过程出现错误", e);
        }
        return null;
    }
}
