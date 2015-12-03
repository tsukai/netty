/**
 * 
 */
package cn.beijing.netty.udp.client;

import java.net.InetSocketAddress;

import cn.beijing.netty.udp.handler.ChineseProverbClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

/**
 * @author zukai 2015-12-03
 */
public class ChineseProverbClient {
	public static void main(String[] args) throws Exception {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		new ChineseProverbClient().connect(port);
	}

	private void connect(int port) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)
				.handler(new ChineseProverbClientHandler());
			
			Channel ch = b.bind(0).sync().channel();
			//向网段内的所有机器广播UDP消息 255.255.255.255
			ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("?",CharsetUtil.UTF_8),
					new InetSocketAddress("127.0.0.1", port))).sync();
			if(!ch.closeFuture().await(15000)){
				System.out.println("查询超时");
			}
		}finally{
			group.shutdownGracefully();
		}
	}
}
