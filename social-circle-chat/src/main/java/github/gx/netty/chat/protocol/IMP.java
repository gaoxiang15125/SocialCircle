package github.gx.netty.chat.protocol;

/**
 * @program: social-circle-main
 * @description: Instant Message Protocol 协议设计枚举类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-21 16:58
 **/
public enum  IMP {

    /** 系统消息 */
    SYSTEM("SYSTEM"),
    /** 登录指令 */
    LOGIN("LOGIN"),
    /** 登出指令 */
    LOGOUT("LOGOUT"),
    /** 聊天消息 */
    CHAT("CHAT"),
    /** 送鲜花 */
    FLOWER("FLOWER");

    private String name;

    public static boolean isIMP(String content){

        return content.matches("^\\[(SYSTEM|LOGIN|LOGOUT|CHAT)\\]");
    }

    IMP(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public String toString(){
        return this.name;
    }
}
