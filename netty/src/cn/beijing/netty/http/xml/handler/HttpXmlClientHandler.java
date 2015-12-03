/**
 * 
 */
package cn.beijing.netty.http.xml.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import cn.beijing.netty.http.xml.pojo.OrderFactory;
import cn.beijing.netty.http.xml.protocal.HttpXmlRequest;
import cn.beijing.netty.http.xml.protocal.HttpXmlResponse;

/**
 * @author zukai 2015-12-02
 */
public class HttpXmlClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		HttpXmlRequest request = new HttpXmlRequest(null, OrderFactory.create(123));
		ctx.writeAndFlush(request);
	}
	@Override
	protected void messageReceived(ChannelHandlerContext ctx,
			HttpXmlResponse msg) throws Exception {
		System.out.println("The client receive response of http headers is: "+msg.getResponse().headers().names());
		System.out.println("The client receive response of body is: "+msg.getResult());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	
}
