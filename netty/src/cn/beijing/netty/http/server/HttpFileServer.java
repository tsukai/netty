/**
 * 
 */
package cn.beijing.netty.http.server;

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
import io.netty.handler.stream.ChunkedWriteHandler;
import cn.beijing.netty.http.handler.HttpFileServerHandler;

/**
 * HttpObjectAggregator 将多个消息转换为单一的FullHttpRequest或者FullHttpResponse
 * 原因是HTTP解码器在每个HTTP消息中会生成多个消息对象。
 * 
 * ChunkedWriteHandler 支持异步发送大的码流（例如大的文件传输），但不占用过多的内存，防止Java内存溢出错误。
 * @author zukai 2015-11-28
 */
public class HttpFileServer {
	private final static String DEFAULT_URL = "/src/cn/beijing/netty";
	public static void main(String[] args) throws Exception {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		String url = DEFAULT_URL;
		if(args != null && args.length > 1){
			url = args[1];
		}
		new HttpFileServer().run(port,url);
	}
	private void run(final int port, final String url) throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel sc)
							throws Exception {
						sc.pipeline().addLast("http-decoder",new HttpRequestDecoder());
						sc.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
						sc.pipeline().addLast("http-encoder",new HttpResponseEncoder());
						sc.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
						sc.pipeline().addLast("fileServerHandler",new HttpFileServerHandler(url));
					}
				});
			ChannelFuture f = b.bind("192.168.1.104",port).sync();
			System.out.println("HTTP文件目录服务器启动，网址是："+"http://192.168.1.104:"+port+url);
			f.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
