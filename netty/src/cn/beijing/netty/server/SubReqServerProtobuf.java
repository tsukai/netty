/**
 * 
 */
package cn.beijing.netty.server;

import cn.beijing.netty.handler.SubReqServerPtotobufHandler;
import cn.beijing.netty.model.SubscribeReqProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * ProtobufVarint32FrameDecoder 主要用于半包处理
 * ProtobufDecoder 参数是com.google.protobuf.MessageLite告诉decoder需要解码的目标类是什么
 * ProtobufDecoder仅仅负责解码，不支持读半包
 * 读半包处理方案：
 * 1.ProtobufVarint32FrameDecoder
 * 2.继承通用半包解码器LengthFieldBasedFrameDecoder
 * 3.继承ByteToMessageDecoder,自己处理半包信息
 * @author zukai 2015-11-28
 */
public class SubReqServerProtobuf {
	public static void main(String[] args) throws Exception {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		new SubReqServerProtobuf().bind(port);
	}

	private void bind(int port) throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 100)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel sc)
							throws Exception {
						sc.pipeline().addLast(new ProtobufVarint32FrameDecoder());
						sc.pipeline().addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
						sc.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
						sc.pipeline().addLast(new ProtobufEncoder());
						sc.pipeline().addLast(new SubReqServerPtotobufHandler());
					}
				});
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
