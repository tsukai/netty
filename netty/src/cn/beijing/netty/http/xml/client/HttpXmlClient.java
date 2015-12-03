/**
 * 
 */
package cn.beijing.netty.http.xml.client;

import java.net.InetSocketAddress;

import cn.beijing.netty.http.xml.decoder.HttpXmlResponseDecoder;
import cn.beijing.netty.http.xml.encoder.HttpXmlRequestEncoder;
import cn.beijing.netty.http.xml.handler.HttpXmlClientHandler;
import cn.beijing.netty.http.xml.pojo.Order;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * @author zukai 2015-12-02
 */
public class HttpXmlClient {
	public static void main(String[] args) throws Exception {
		int port = 7070;
		if(args != null && args.length > 0){
			port = Integer.valueOf(args[0]);
		}
		new HttpXmlClient().connect(port);
	}

	private void connect(int port) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY,true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel sc)
							throws Exception {
						sc.pipeline().addLast("http-decoder",new HttpResponseDecoder());
						sc.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
						sc.pipeline().addLast("xml-decoder",new HttpXmlResponseDecoder(Order.class,true));
						sc.pipeline().addLast("http-encoder",new HttpRequestEncoder());
						sc.pipeline().addLast("xml-encoder",new HttpXmlRequestEncoder());
						sc.pipeline().addLast("xmlClientHandler",new HttpXmlClientHandler());
					}
				});
			ChannelFuture f = b.connect(new InetSocketAddress(port)).sync();
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
}
