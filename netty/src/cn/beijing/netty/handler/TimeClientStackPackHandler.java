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
 * @author zukai 2015-11-26
 */
public class TimeClientStackPackHandler extends ChannelHandlerAdapter {
	private static final Logger logger = Logger.getLogger(TimeClientStackPackHandler.class.getName());
	private int counter;
	private byte[] req;
	public TimeClientStackPackHandler(){
		req = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ByteBuf message = null;
		for (int i = 0; i < 100; i++) {
			message = Unpooled.buffer(req.length);
			message.writeBytes(req);
			ctx.writeAndFlush(message);
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		String body = (String) msg;
		System.out.println("Now is :"+body+";the counter is: "+ ++counter);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.warning("Unexpected exception from downsteam: "+cause.getMessage());
		ctx.close();
	}
}
