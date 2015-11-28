/**
 * 
 */
package cn.beijing.netty.handler;

import cn.beijing.netty.model.SubscribeReqProto;
import cn.beijing.netty.model.SubscribeReqProto.SubscribeReq;
import cn.beijing.netty.model.SubscribeRespProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zukai 2015-11-28
 */
public class SubReqServerPtotobufHandler extends ChannelHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		SubscribeReqProto.SubscribeReq req = (SubscribeReq) msg;
		if("zukai".equalsIgnoreCase(req.getUserName())){
			System.out.println("Server accept client subscribe req: ["+req.toString()+"]");
			ctx.writeAndFlush(resp(req.getSubReqId()));
		}
	}

	private SubscribeRespProto.SubscribeResp resp(int subReqId) {
		SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
		builder.setSubReqId(subReqId);
		builder.setRespCode(0);
		builder.setDesc("Netty book order succeed,3 days later,sent to the designated address.");
		return builder.build();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
