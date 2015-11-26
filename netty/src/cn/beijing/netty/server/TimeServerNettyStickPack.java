/**
 * 
 */
package cn.beijing.netty.server;

import cn.beijing.netty.handler.TimeServerStackPackHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * LineBasedFrameDecoder 解决粘包问题
 * LineBasedFrameDecoder 依次遍历ByteBuf中的可读字节，判断看是否有"\n"或"\r\n"
 * 如果有，就以此位置为结束位置，从可读索引到结束位置区间的字节就组成了一行。
 * 它是以换行符为结束标志的解码器，支持携带结束符和不携带结束符两种解码方式，同时支持配置单行的最大长度。
 * 如果连续读取到最大长度后仍然没有发现换行符，就会抛出异常，同时忽略掉之前读到的异常码流。
 * 
 * StringDecoder 将接收到的对象转换为字符串，然后继续调用后面的handler。
 * 
 * LineBasedFrameDecoder+StringDecoder组合就是按行切换的文本解码器，用来支持TCP的粘包和拆包。
 * @author zukai 2015-11-26
 */
public class TimeServerNettyStickPack {
	public static void main(String[] args) throws Exception {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		new TimeServerNettyStickPack().bind(port);
	}

	private void bind(int port) throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChildChannelHandler());
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

		@Override
		protected void initChannel(SocketChannel sc) throws Exception {
			sc.pipeline().addLast(new LineBasedFrameDecoder(1024));
			sc.pipeline().addLast(new StringDecoder());
			sc.pipeline().addLast(new TimeServerStackPackHandler());
		}
		
	}
}
