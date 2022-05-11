package github.gx.netty.chat.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.MessagePack;
import org.msgpack.MessageTypeException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: social-circle-main
 * @description: 自定义IMP解码器
 * @author: gaoxiang
 * @email: 630268696@qq.com
 * @create: 2021-03-21 17:04
 **/
@Slf4j
public class IMDecoder extends ByteToMessageDecoder {

    /**
     * 该类实现了两个功能：
     * 1. 解析网络传输的内容，将结果放入结果集 供之后处理
     * 2. 将网络传出的 byte 流转化为对象
     */
    //解析IM写一下请求内容的正则
    private Pattern pattern = Pattern.compile("^\\[(.*)\\](\\s\\-\\s(.*))?");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try{
            //先获取可读字节数
            final int length = in.readableBytes();
            final byte[] array = new byte[length];
            String content = new String(array,in.readerIndex(),length);

            //空消息不解析
            if(!(null == content || "".equals(content.trim()))){
                if(!IMP.isIMP(content)){
                    // 应该是扔掉当前的访问请求
                    ctx.channel().pipeline().remove(this);
                    return;
                }
            }

            in.getBytes(in.readerIndex(), array, 0, length);
            out.add(new MessagePack().read(array,IMMessage.class));
            in.clear();
        }catch(MessageTypeException e){
            ctx.channel().pipeline().remove(this);
        }
    }

    /**
     * 字符串解析成自定义即时通信协议
     * @param msg
     * @return
     */
    public IMMessage decode(String msg){
        if(null == msg || "".equals(msg.trim())){ return null; }
        try{
            Matcher m = pattern.matcher(msg);
            String header = "";
            String content = "";
            if(m.matches()){
                header = m.group(1);
                content = m.group(3);
            }

            String [] headers = header.split("\\]\\[");
            long time = 0;
            try{
                time = Long.parseLong(headers[1]);
            } catch(Exception e){
                log.error("解析网络传输的流过程出现错误", e);
            }

            String nickName = headers[2];
            //昵称最多十个字, 这里不再对昵称进行检查，交由前端处理
            nickName = nickName.length() < 16 ? nickName : nickName.substring(0, 15);

            /**
             * 根据信号内容构造不同的消息对象 可以看出 字符串解析工作是我们指定地
             * 理论上可以支持不同语言间消息传输
             */
            if(msg.startsWith("[" + IMP.LOGIN.getName() + "]")){
                return new IMMessage(headers[0],headers[3],time,nickName);
            }else if(msg.startsWith("[" + IMP.CHAT.getName() + "]")){
                return new IMMessage(headers[0],time,nickName,content);
            }else if(msg.startsWith("[" + IMP.FLOWER.getName() + "]")){
                return new IMMessage(headers[0],headers[3],time,nickName);
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
