package github.gx.netty.chat.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @program: social-circle-main
 * @description: 自定义编码实现类
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-21 18:16
 **/
public class IMEncoder extends MessageToByteEncoder<IMMessage> {
    /**
     * 网络传输时 对应的操作
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, IMMessage msg, ByteBuf out)
            throws Exception {
        // 使用 messagePack 进行序列化操作
        out.writeBytes(new MessagePack().write(msg));
    }

    public String encode(IMMessage msg){
        /**
         * 可以这么写的原因：字段数量少，字段顺序固定；我觉得真的来还是使用 fastJson 更好
         * 效率影响 应该可以忽略不记
         */
        if(null == msg){ return ""; }
        String prex = "[" + msg.getCmd() + "]" + "[" + msg.getTime() + "]";
        if(IMP.LOGIN.getName().equals(msg.getCmd()) ||
                IMP.FLOWER.getName().equals(msg.getCmd())){
            prex += ("[" + msg.getSender() + "][" + msg.getTerminal() + "]");
        }else if(IMP.CHAT.getName().equals(msg.getCmd())){
            prex += ("[" + msg.getSender() + "]");
        }else if(IMP.SYSTEM.getName().equals(msg.getCmd())){
            prex += ("[" + msg.getOnline() + "]");
        }
        /**
         * 看起来是对传递的内容有特殊的处理
         */
        if(!(null == msg.getContent() || "".equals(msg.getContent()))){
            prex += (" - " + msg.getContent());
        }
        return prex;
    }
}
