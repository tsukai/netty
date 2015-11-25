/**
 * 
 */
package cn.beijing.netty.handler;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zukai 2015-11-25
 */
public class TimeClientNettyHandler extends ChannelHandlerAdapter {
	private static final Logger logger = Logger.getLogger(TimeClientNettyHandler.class.getName());
	private final ByteBuf firstMessage;
	public TimeClientNettyHandler() {
		byte[] bytes = "QUERY TIME ORDER".getBytes();
		firstMessage = Unpooled.buffer(bytes.length);
		firstMessage.writeBytes(bytes);
	}
	
	//当客户端和服务端tcp链路建立成功之后调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(firstMessage);//讲请求消息发送给服务端
	}
	
	//服务端返回应答消息是调用
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req,"UTF-8");
		System.out.println("Now is : "+body);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.warning("Unexcepted exception from downstream: "+cause.getMessage());
		ctx.close();
	}
}
