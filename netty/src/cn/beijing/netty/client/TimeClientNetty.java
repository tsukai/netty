/**
 * 
 */
package cn.beijing.netty.client;

import cn.beijing.netty.handler.TimeClientHalfPackHandler;
import cn.beijing.netty.handler.TimeClientNettyHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author zukai 2015-11-25
 */
public class TimeClientNetty {
	public static void main(String[] args) throws Exception{
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		new TimeClientNetty().connect(port,"127.0.0.1");
	}

	private void connect(int port, String host) throws Exception{
		//配置客户端Nio线程组
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
//						ch.pipeline().addLast(new TimeClientNettyHandler());
						ch.pipeline().addLast(new TimeClientHalfPackHandler());//读半包
					}
					
				});
			//发起异步连接操作
			ChannelFuture f = b.connect(host,port).sync();
			//等待客户端链路关闭
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
}
