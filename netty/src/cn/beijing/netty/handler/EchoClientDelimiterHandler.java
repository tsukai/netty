/**
 * 
 */
package cn.beijing.netty.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zukai 2015-11-26
 */
public class EchoClientDelimiterHandler extends ChannelHandlerAdapter {
	private int counter;
	static final String ECHO_REQ = "Hi,A Tsu.Welcome to Netty.$_";
	public EchoClientDelimiterHandler(){}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 100; i++) {
			ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println("This is "+ ++counter + " times receive server : ["+msg+"]");
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
