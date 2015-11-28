/**
 * 
 */
package cn.beijing.netty.client;

import java.net.InetSocketAddress;

import cn.beijing.netty.factory.MarshallingCodecFactory;
import cn.beijing.netty.handler.SubReqClientMarshallingHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author zukai 2015-11-28
 */
public class SubReqClientMarshalling {
	public static void main(String[] args) throws Exception {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		new SubReqClientMarshalling().connect(port,"127.0.0.1");
	}

	private void connect(int port, String host) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap  b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel sc)
							throws Exception {
						sc.pipeline().addLast(MarshallingCodecFactory.buildMarshallingDecoder());
						sc.pipeline().addLast(MarshallingCodecFactory.buildMarshallingEncoder());
						sc.pipeline().addLast(new SubReqClientMarshallingHandler());
					}
				});
			ChannelFuture f = b.connect(new InetSocketAddress(host,port)).sync();
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
}
