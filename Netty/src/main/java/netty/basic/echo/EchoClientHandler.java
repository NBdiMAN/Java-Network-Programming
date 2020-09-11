package netty.basic.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;

/**
 * @author wangyang
 * @date 2020/9/8 12:17
 * @description:
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 客户端读取数据以后干什么
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("Client accept:" + byteBuf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 客户端被通知channel活跃以后,做什么
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //向服务器发送数据
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,Netty",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
