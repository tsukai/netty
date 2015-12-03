/**
 * 
 */
package cn.beijing.netty.websocket.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * WebSocketServerHandshaker 握手处理类，构造握手响应消息返回给客户端，
 * 同时将WebSocket相关的编码和解码类动态添加到ChannelPipeline中，用于WebScoket消息的编解码
 * @author zukai 2015-12-03
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

	private static final Logger logger = Logger.getLogger(WebSocketServerHandler.class.getName());
	private WebSocketServerHandshaker handshaker;
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		//传统的http接入  第一次握手请求消息有HTTP协议承载
		if(msg instanceof FullHttpRequest){
			handleHttpRequest(ctx,(FullHttpRequest)msg);
		}
		//websocket接入
		else if(msg instanceof WebSocketFrame){
			handleWebSocketFrame(ctx,(WebSocketFrame)msg);
		}
	}
	private void handleWebSocketFrame(ChannelHandlerContext ctx,
			WebSocketFrame frame) {
		//判断是否是关闭链路的指令
		if(frame instanceof CloseWebSocketFrame){
			handshaker.close(ctx.channel(), (CloseWebSocketFrame)frame.retain());
			return;
		}
		//判断是否是ping消息
		if(frame instanceof PingWebSocketFrame){
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		//本例仅支持文本消息，不支持二进制消息
		if(!(frame instanceof TextWebSocketFrame)){
			throw new UnsupportedOperationException(String.format("%s frame types not supported.", frame.getClass().getName()));
		}
		//返回应答消息
		String request = ((TextWebSocketFrame)frame).text();
		if(logger.isLoggable(Level.FINE)){
			logger.fine(String.format("%s received %s", ctx.channel(),request));
		}
		ctx.channel().write(new TextWebSocketFrame(request+
				", 欢迎使用Netty WebSocket服务，现在时刻："+new Date().toString()));
	}
	private void handleHttpRequest(ChannelHandlerContext ctx,
			FullHttpRequest req) {
		//如果HTTP解码失败，返回HTTP异常
		if(!req.getDecoderResult().isSuccess() || !"websocket".equals(req.headers().get("Upgrade"))){
			sendHttpResponse(ctx,req,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}
		//构造握手响应返回，本机测试
		//握手工厂
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:7070/websocket", null, false);
		handshaker = wsFactory.newHandshaker(req);
		if(handshaker == null){
			WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
		}else{
			handshaker.handshake(ctx.channel(), req);
		}
	}
	
	private void sendHttpResponse(ChannelHandlerContext ctx,
			FullHttpRequest req, DefaultFullHttpResponse resp) {
		//返回应答给客户端
		if(resp.getStatus().code() != 200){
			ByteBuf buf = Unpooled.copiedBuffer(resp.getStatus().toString(),CharsetUtil.UTF_8);
			resp.content().writeBytes(buf);
			buf.release();
			HttpHeaders.setContentLength(resp, resp.content().readableBytes());
		}
		//如果是非Keep-Alive，关闭连接
		ChannelFuture feture = ctx.channel().writeAndFlush(resp);
		if(!HttpHeaders.isKeepAlive(req) || resp.getStatus().code() != 200){
			feture.addListener(ChannelFutureListener.CLOSE);
		}
		
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
