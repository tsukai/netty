/**
 * 
 */
package cn.beijing.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import cn.beijing.netty.handler.TimeServerHalfPackHandler;
import cn.beijing.netty.handler.TimeServerNettyHandler;

/**
 * @author zukai 2015-11-25
 */
public class TimeServerNetty {
	public static void main(String[] args) throws Exception {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		new TimeServerNetty().bind(port);
	}

	private void bind(int port) throws Exception {
		//配置服务端的NIO线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup,workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChildChannelHandler());
			//绑定端口，同步等待成功
			ChannelFuture f = b.bind(port).sync();
			//等待服务端监听端口关闭
			f.channel().closeFuture().sync();
		}finally{
			//释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	private class ChildChannelHandler extends ChannelInitializer<io.netty.channel.socket.SocketChannel>{

		@Override
		protected void initChannel(io.netty.channel.socket.SocketChannel arg0)
				throws Exception {
//			arg0.pipeline().addLast(new TimeServerNettyHandler());
			arg0.pipeline().addLast(new TimeServerHalfPackHandler());//读半包
		}

		
	}
}
