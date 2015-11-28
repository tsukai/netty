/**
 * 
 */
package cn.beijing.netty.handler;

import cn.beijing.netty.model.SubscribeReq;
import cn.beijing.netty.model.SubscribeResp;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zukai 2015-11-28
 */
public class SubReqServerMarshallingHandler extends ChannelHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		SubscribeReq req = (SubscribeReq) msg;
		if("Netty".equalsIgnoreCase(req.getUserName())){
			System.out.println("Server accpet client subscribe req: ["+req.toString()+"]");
			ctx.writeAndFlush(resp(req.getSubReqId()));
		}
	}

	private Object resp(int subReqId) {
		SubscribeResp resp = new SubscribeResp();
		resp.setSubReqId(subReqId);
		resp.setRespCode(0);
		resp.setDesc("Netty book order succeed,3 days later,sent to the designated address");
		return resp;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
