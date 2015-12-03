/**
 * 
 */
package cn.beijing.netty.udp.server;

import cn.beijing.netty.udp.handler.ChineseProverbServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @author zukai 2015-12-03
 */
public class ChineseProverbServer {
	public static void main(String[] args) throws Exception {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		new ChineseProverbServer().run(port);
	}

	private void run(int port) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)
				.handler(new ChineseProverbServerHandler());
			b.bind(port).sync().channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
}
