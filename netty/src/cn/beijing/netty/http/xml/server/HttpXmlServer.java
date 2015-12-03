/**
 * 
 */
package cn.beijing.netty.http.xml.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.net.InetSocketAddress;

import cn.beijing.netty.http.xml.decoder.HttpXmlRequestDecoder;
import cn.beijing.netty.http.xml.encoder.HttpXmlResponseEncoder;
import cn.beijing.netty.http.xml.handler.HttpXmlServerHandler;
import cn.beijing.netty.http.xml.pojo.Order;

/**
 * @author zukai 2015-12-02
 */
public class HttpXmlServer {
	public static void main(String[] args) throws Exception {
		int port = 7070;
		if(args != null && args.length > 0){
			port = Integer.valueOf(args[0]);
		}
		new HttpXmlServer().run(port);
	}

	private void run(int port) throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel sc)
							throws Exception {
						sc.pipeline().addLast("http-decoder",new HttpRequestDecoder());
						sc.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
						sc.pipeline().addLast("xml-decoder",new HttpXmlRequestDecoder(Order.class,true));
						sc.pipeline().addLast("http-encoder",new HttpResponseEncoder());
						sc.pipeline().addLast("xml-encoder",new HttpXmlResponseEncoder());
						sc.pipeline().addLast("xmlServerHandler",new HttpXmlServerHandler());
					}
				});
			ChannelFuture f  = b.bind(new InetSocketAddress(port)).sync();
			f.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
