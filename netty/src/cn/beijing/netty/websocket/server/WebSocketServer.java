/**
 * 
 */
package cn.beijing.netty.websocket.server;

import cn.beijing.netty.websocket.handler.WebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author zukai 2015-12-03
 */
public class WebSocketServer {
	public static void main(String[] args) throws Exception {
		int port = 7070;
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				
			}
		}
		new WebSocketServer().run(port);
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
						ChannelPipeline pipeline = sc.pipeline();
						pipeline.addLast("http-codec",new HttpServerCodec());//将请求和应答消息编码为HTTP消息
						pipeline.addLast("aggregator",new HttpObjectAggregator(65536));//将HTTP消息的多个部分组合成一条完整的HTTP消息
						pipeline.addLast("http-chunked",new ChunkedWriteHandler());//向客户端发送HTML5文件，主要用于支持浏览器和服务端进行WebSocket通信
						pipeline.addLast("handler",new WebSocketServerHandler());
					}
				});
			Channel ch = b.bind(port).sync().channel();
			System.out.println("Web socket server started at port: "+port);
			System.out.println("Open your browser and navigate to http://localhost:"+port+"/");
			ch.closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
