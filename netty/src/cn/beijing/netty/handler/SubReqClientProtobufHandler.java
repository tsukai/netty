/**
 * 
 */
package cn.beijing.netty.handler;

import java.util.ArrayList;
import java.util.List;

import cn.beijing.netty.model.SubscribeReqProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zukai 2015-11-28
 * require jdk1.7+
 */
public class SubReqClientProtobufHandler extends ChannelHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 10; i++) {
			ctx.write(subReq(i));
		}
		ctx.flush();
	}

	private SubscribeReqProto.SubscribeReq subReq(int i) {
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqId(i);
		builder.setUserName("zukai");
		builder.setPhoneNumber("138********");
		List<String> address = new ArrayList<>();
		address.add("Haidian Beijing");
		address.add("Chaoyang Beijing");
		builder.addAllAddress(address);
		return builder.build();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println("Receive server response: ["+msg+"]");
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
