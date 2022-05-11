//package github.zlg.socialcircle.module.util.secret;
//
///**
// * @program: social-circle-main
// * @description: 常规的加解密实现，为了避免前端使用 encode 对输入进行编码，这里使用自定义的加密实现
// * @author: gaoxiang
// * @email: 630268696@qq.com
// * @create: 2021-06-06 14:50
// **/
//public class NormalSecret implements SecretDefine<char[]> {
//    // 使用单例模式实现加解密实现类
//    char[] key;
//    static NormalSecret normalSecret;
//    static int CHAR_SIZE = 256;
//
//    private NormalSecret() {
//        key = creatKey();
//    }
//
//    // 随机生成 key 大小与内容都随机
//    public char[] creatKey() {
//        // 保底长度为 5 预测长度为 25
//        int length = (int) (Math.random()*40) + 5;
//        char[] key = new char[length];
//        for(int i=0;i<length;i++) {
//            key[i] = (char) (Math.random() * CHAR_SIZE);
//        }
//        return key;
//    }
//
//
//    // 为了简单，这里直接将字符串与 key 内容进行做差，得到的数字作为结果
//    // 很明显得到的结果会很长，但是实际上这点负担不算什么
//    @Override
//    public String encryptId(String id) {
//        for()
//        return null;
//    }
//
//    @Override
//    public String decryptId(String tableId) {
//        return null;
//    }
//}
