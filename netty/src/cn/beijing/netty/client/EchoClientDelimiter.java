/**
 * 
 */
package cn.beijing.netty.client;

import java.net.InetSocketAddress;

import cn.beijing.netty.handler.EchoClientDelimiterHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * DelimiterBasedFrameDecoder可以自动完成以分隔符作为码流结束标识的消息的解码
 * @author zukai 2015-11-26
 */
public class EchoClientDelimiter {
	public static void main(String[] args) throws Exception {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		new EchoClientDelimiter().connect(port,"127.0.0.1");
	}

	private void connect(int port, String host) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel sc)
							throws Exception {
						ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
						sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
						sc.pipeline().addLast(new StringDecoder());
						sc.pipeline().addLast(new EchoClientDelimiterHandler());
					}
				});
			ChannelFuture f = b.connect(new InetSocketAddress(host, port)).sync();
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
}
