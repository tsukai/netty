/**
 * 
 */
package cn.beijing.netty.udp.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ThreadLocalRandom;

/**
 * @author zukai 2015-12-03
 */
public class ChineseProverbServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private static final String[] DICTIONARY = {"远亲不如近邻，近邻不抵对门。","交人交心，浇花浇根。","书本不常翻，犹如一块砖。"
		,"静坐常思己过，闲谈莫论他非","人无横财不发，马无夜草不肥。"};
	
	private String nextQuote(){
		int quoteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
		return DICTIONARY[quoteId];
	}
	@Override
	protected void messageReceived(ChannelHandlerContext ctx,
			DatagramPacket packet) throws Exception {
		String req = packet.content().toString(CharsetUtil.UTF_8);
		System.out.println(req);
		if("?".equals(req)){
			ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("result: "+nextQuote(),CharsetUtil.UTF_8), packet.sender()));
		}
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
		cause.printStackTrace();
	}

}
