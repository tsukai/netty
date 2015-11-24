/**
 * 
 */
package cn.beijing.netty.server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zukai 2015-11-24
 */
public class MultiplexerTimeServer implements Runnable {
	private Selector selector;
	private ServerSocketChannel serverChannel;
	private volatile boolean stop;
	
	/**
	 * 初始化多路复用器，绑定监听端口
	 * @param port
	 */
	public MultiplexerTimeServer(int port){
		try {
			//创建多路复用器
			selector = Selector.open();
			//打开ServerSocketChannel，用于监听客户端连接，他是所有客户端连接的父管道
			serverChannel = ServerSocketChannel.open();
			//设置为非阻塞模式
			serverChannel.configureBlocking(false);
			//绑定端口
			serverChannel.socket().bind(new InetSocketAddress(port), 1024);
			//讲serverchannel注册到多路复用器上，监听ACCPET事件
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("The time server is start in port : "+port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void stop(){
		this.stop = true;
	}
	@Override
	public void run() {
		//多路复用器在线程run方法的无限循环体内轮询准备就绪的key
		while(!stop){
			try {
				selector.select(1000);
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				SelectionKey key = null;
				while(it.hasNext()){
					key = it.next();
					it.remove();
					try {
						handleInput(key);
					} catch (Exception e) {
						if(null != key){
							key.cancel();
							if(key.channel() != null){
								key.channel().close();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//多路复用器关闭后，所有注册在上面的Channel和Pipe等资源都会被自动去注册并关闭
		if(selector != null){
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			//处理新接入的请求消息
			if(key.isAcceptable()){
				//accept the new connection
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				//多路复用器监听到有新的客户端接入，处理新的接入请求
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				//add the new connection to the selector 监听读操作，用来读取客户端发送的网络消息
				sc.register(selector, SelectionKey.OP_READ);
			}
			if(key.isReadable()){
				//read the data
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if(readBytes > 0){
					readBuffer.flip();//将缓冲区当前的limit设置为position，将position设置为0，用于后续对缓冲区的读取操作
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body = new String(bytes,"UTF-8");
					System.out.println("The time Server receive order: "+body);
					String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? 
							new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
					doWrite(sc,currentTime);
				}else if(readBytes < 0){
					//对端链路关闭
					key.cancel();
					sc.close();
				}else{
					;//读到0字节，忽略
				}
			}
		}
	}

	private void doWrite(SocketChannel sc, String response) throws IOException {
		if(response != null && response.trim().length() > 0){
			byte[] bytes = response.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			sc.write(writeBuffer);
		}
	}

}
